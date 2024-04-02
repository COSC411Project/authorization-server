package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import app.entities.User;
import app.enums.Role;
import app.repositories.IUserRepository;

@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryTests {

	@Autowired
	private IUserRepository userRepository;
	
	private User user;
	
	@BeforeEach
	public void setup() {
		user = new User("email", "password", true, Role.USER, LocalDate.now());
	}
	
	@AfterEach
	public void cleanup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void save_success( ) {
		// Act
		userRepository.save(user);
		
		// Assert
		Optional<User> savedUser = userRepository.findById(user.getId());
		assertTrue(savedUser.isPresent());
		assertEquals(user, savedUser.get());
	}
	
	@Test
	public void findByEmail_success() {
		// Arrange
		userRepository.save(user);
		
		// Act
		Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
		
		// Assert
		assertTrue(savedUser.isPresent());
	}
	
	@Test
	public void deleteByEmail_success() {
		// Arrange
		userRepository.save(user);
		
		// Act
		userRepository.deleteByEmail(user.getEmail());
		
		// Assert
		Optional<User> savedUser = userRepository.findById(user.getId());
		assertFalse(savedUser.isPresent());
	}
}
