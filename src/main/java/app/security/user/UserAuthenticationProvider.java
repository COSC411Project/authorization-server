 package app.security.user;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	private final CustomUserDetailsManager userDetailsManager;
	private final PasswordEncoder passwordEncoder;
	
	public UserAuthenticationProvider(CustomUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
		this.userDetailsManager = userDetailsManager;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			String email = (String) authentication.getPrincipal();
			SecurityUser securityUser = (SecurityUser) userDetailsManager.loadUserByEmail(email);
			
			if (passwordEncoder.matches(securityUser.getPassword(), (String) authentication.getCredentials())) {
				return new UsernamePasswordAuthenticationToken(securityUser.getUser().getId(), securityUser.getPassword(), securityUser.getAuthorities());
			}
		} catch (Exception ex) {
		}
		
		throw new BadCredentialsException("Invalid email or password.");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
