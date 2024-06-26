package app.security;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import app.entities.User;
import app.enums.AuthProvider;
import app.enums.Role;
import app.security.user.CustomUserDetailsManager;
import app.security.user.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OAuthConversionFilter extends OncePerRequestFilter {
	
	private final CustomUserDetailsManager userDetailsManager;
	
	public OAuthConversionFilter(CustomUserDetailsManager userDetailsManager) {
		this.userDetailsManager = userDetailsManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();

		if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			Collection<GrantedAuthority> authorities = token.getAuthorities();
			
			SecurityUser securityUser = getSecurityUser(authorities);
			if (securityUser == null) {
				filterChain.doFilter(request, response);
			}
			
			var usernamePasswordToken = new UsernamePasswordAuthenticationToken(securityUser.getUser().getId(), null, securityUser.getAuthorities());
			securityContext.setAuthentication(usernamePasswordToken);
		}
		
		filterChain.doFilter(request, response);
	}
	
	private SecurityUser getSecurityUser(Collection<GrantedAuthority> authorities) {
		UserInfo userInfo = getUserInfo(authorities);
		SecurityUser securityUser = null;
		
		String username = userInfo.getUsername();
		AuthProvider authProvider = userInfo.getAuthProvider();
		
		if (userDetailsManager.userExists(username, authProvider)) {
			securityUser = loadSecurityUser(username, authProvider);
		} else {
			User user = new User();
			user.setUsername(username);
			user.setAuthProvider(authProvider);
			user.setDateCreated(LocalDate.now());
			user.setRole(Role.USER);
			user.setEnabled(true);
			
			securityUser = new SecurityUser(user);
			userDetailsManager.createUser(securityUser);
		}
		
		return securityUser;
	}

	private UserInfo getUserInfo(Collection<GrantedAuthority> authorities) {
		String username = null;
		AuthProvider authProvider = null;
		
		for (GrantedAuthority authority : authorities) {
			// Represents OAuth through Google
			if (authority instanceof OidcUserAuthority) {
				OidcUserAuthority userAuthority = (OidcUserAuthority) authority;
				
				String email = (String) userAuthority.getAttributes().get("email");
				username = email.substring(0, email.indexOf("@"));
				authProvider = AuthProvider.GOOGLE;
				break;
			}
			
			// Represents OAuth through GitHub
			if (authority instanceof OAuth2UserAuthority) {
				OAuth2UserAuthority userAuthority = (OAuth2UserAuthority) authority;
				username = (String) userAuthority.getAttributes().get("login");
				authProvider = AuthProvider.GITHUB;
				break;
			}
		}
		
		return new UserInfo(username, authProvider);
	}
	
	private SecurityUser loadSecurityUser(String username, AuthProvider authProvider) {
		try {
			return (SecurityUser) userDetailsManager.loadUserByUsernameAndAuthProvider(username, authProvider);
		} catch (Exception ex) {
			return null;
		}
	}

	class UserInfo {
		private String username;
		private AuthProvider authProvider;
		
		public UserInfo(String username, AuthProvider authProvider) {
			this.username = username;
			this.authProvider = authProvider;
		}

		public String getUsername() {
			return username;
		}

		public AuthProvider getAuthProvider() {
			return authProvider;
		}
	}

}
