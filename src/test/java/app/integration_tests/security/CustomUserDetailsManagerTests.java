package app.integration_tests.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import app.entities.User;
import app.enums.Role;
import app.exceptions.EmailNotFoundException;
import app.repositories.IUserRepository;
import app.security.user.CustomUserDetailsManager;

@ActiveProfiles("test")
@SpringBootTest
public class CustomUserDetailsManagerTests {

	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserDetailsManager userDetailsManager;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		String password = passwordEncoder.encode("password");
		user = new User("email", password, true, Role.USER);
		userRepository.save(user);
	}
	
	@AfterEach
	public void cleanup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void loadUserByEmail_success() throws EmailNotFoundException {
		// Arrange
		SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole());
		
		// Act
		UserDetails userDetails = userDetailsManager.loadUserByEmail(user.getEmail());
		
		// Assert
		assertNotNull(userDetails);
		assertEquals(expectedAuthority, userDetails.getAuthorities().iterator().next());
	}
	
	@Test
	public void loadUserByEmail_emailNotFound_exception() {
		// Act and Assert
		assertThrows(EmailNotFoundException.class, () -> {
			userDetailsManager.loadUserByEmail("random email");
		});
	}
	
	@Test
	public void deleteUser_success() {
		// Act
		userDetailsManager.deleteUser(user.getEmail());
		
		// Assert
		Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
		assertFalse(savedUser.isPresent());
	}
	
	@Test
	public void userExists_true() {
		// Act
		boolean exists = userDetailsManager.userExists(user.getEmail());
		
		// Assert
		assertTrue(exists);
	}
	
	@Test
	public void userExists_false() {
		// Act
		boolean exists = userDetailsManager.userExists("random email");
		
		// Assert
		assertFalse(exists);
	}
}
