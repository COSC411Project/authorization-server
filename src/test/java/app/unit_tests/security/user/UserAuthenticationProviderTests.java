package app.unit_tests.security.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.entities.User;
import app.enums.Role;
import app.exceptions.EmailNotFoundException;
import app.security.user.CustomUserDetailsManager;
import app.security.user.SecurityUser;
import app.security.user.UserAuthenticationProvider;

public class UserAuthenticationProviderTests {
	
	private CustomUserDetailsManager userDetailsManager;
	private PasswordEncoder passwordEncoder;
	
	private UserAuthenticationProvider provider;
	
	@BeforeEach
	public void setup() {
		userDetailsManager = mock(CustomUserDetailsManager.class);
		passwordEncoder = mock(PasswordEncoder.class);
		
		provider = new UserAuthenticationProvider(userDetailsManager, passwordEncoder);
	}
	
	@Test
	public void authenticate_success() throws EmailNotFoundException {
		// Arrange
		User user = new User(1, "email", "password", Role.USER);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user.getEmail());
		when(authentication.getCredentials()).thenReturn(user.getPassword());
		
		SecurityUser securityUser = new SecurityUser(user);
		when(userDetailsManager.loadUserByEmail(anyString())).thenReturn(securityUser);
	
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
		
		// Act
		Authentication authenticationToken = provider.authenticate(authentication);
		
		// Assert
		assertEquals(user.getId(), authenticationToken.getPrincipal());
		assertEquals(expectedAuthority, authenticationToken.getAuthorities().iterator().next());
	}
	
	@Test
	public void authenticate_emailNotFound_exception() throws EmailNotFoundException {
		// Arrange
		User user = new User(1, "email", "password", Role.USER);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user.getEmail());
		when(authentication.getCredentials()).thenReturn(user.getPassword());
		
		when(userDetailsManager.loadUserByEmail(anyString())).thenThrow(EmailNotFoundException.class);
		
		// Act and Assert
		assertThrows(BadCredentialsException.class, () -> {
			provider.authenticate(authentication);
		});
	}
	
	@Test
	public void authentication_passwordDoesntMatch_exception() throws EmailNotFoundException {
		// Arrange
		User user = new User(1, "email", "password", Role.USER);
		
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(user.getEmail());
		when(authentication.getCredentials()).thenReturn("wrong password");
		
		SecurityUser securityUser = new SecurityUser(user);
		when(userDetailsManager.loadUserByEmail(anyString())).thenReturn(securityUser);
	
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
		
		// Act and Assert
		assertThrows(BadCredentialsException.class, () -> {
			provider.authenticate(authentication);
		});
	}
	
	@Test
	public void supports_true() {
		// Act
		boolean supports = provider.supports(UsernamePasswordAuthenticationToken.class);
		
		// Assert
		assertTrue(supports);
	}
	
}
