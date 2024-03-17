package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
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
	
	@AfterEach
	public void cleanup() {
		userRepository.deleteAll();
	}
	
	@Test
	public void save_success( ) {
		// Arrange
		User user = new User("email", "password", true, Role.USER);
		
		// Act
		userRepository.save(user);
		
		// Assert
		Optional<User> savedUser = userRepository.findById(user.getId());
		assertTrue(savedUser.isPresent());
		assertEquals(user, savedUser.get());
	}
}
