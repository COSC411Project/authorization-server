package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;
import app.repositories.IClientRepository;

@ActiveProfiles("test")
@SpringBootTest
public class ClientRepositoryTests {

	@Autowired
	private IClientRepository clientRepository; 
	
	@AfterEach
	public void cleanup() {
		clientRepository.deleteAll();
	}
	
	@Test
	public void save_success() {
		// Arrange
		Client client = new Client("client", "secret", false, Set.of(GrantType.AUTHORIZATION_CODE), Set.of(Scope.READ));
		
		// Act
		clientRepository.save(client);
		
		// Assert
		Optional<Client> savedClient = clientRepository.findById(client.getId());
		
		assertTrue(savedClient.isPresent());
		assertEquals(client, savedClient.get());
	}
}
