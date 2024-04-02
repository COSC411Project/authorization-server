package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
	
	private Client client;
	
	@BeforeEach
	public void setup() {
		client = new Client(UUID.randomUUID().toString(),
							UUID.randomUUID().toString(), 
						   "secret", 
						   false, 
						   Set.of(GrantType.AUTHORIZATION_CODE), 
						   Set.of(Scope.READ),
						   Set.of("http://localhost:5173/"));
	}
	
	@AfterEach
	public void cleanup() {
		clientRepository.deleteAll();
	}
	
	@Test
	public void save_success() {
		// Act
		clientRepository.save(client);
		
		// Assert
		Optional<Client> savedClient = clientRepository.findById(client.getId());
		
		assertTrue(savedClient.isPresent());
		assertEquals(client, savedClient.get());
	}
	
	@Test
	public void findByIdentifier_success() {
		// Arrange
		clientRepository.save(client);
		
		// Act
		Optional<Client> savedClient = clientRepository.findByIdentifier(client.getIdentifier());
		
		// Assert
		assertTrue(savedClient.isPresent());
	}
}
