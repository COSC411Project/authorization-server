package app.integration_tests.services;

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

import app.entities.AuthorizationCode;
import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.services.ClientService;

@ActiveProfiles("test")
@SpringBootTest
public class ClientServiceTests {

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private IClientRepository clientRepository;
	
	@Autowired
	private IAuthorizationCodeRepository authorizationCodeRepository;
	
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
		authorizationCodeRepository.deleteAll();
		clientRepository.deleteAll();
	}
	
	@Test
	public void generateAuthorizationCode_success() {
		// Arrange
		clientRepository.save(client);
		String redirectUri = client.getRedirectUris().iterator().next();
		
		// Act
		String code = clientService.generateAuthorizationCode(client.getIdentifier(), redirectUri);
		
		// Assert
		Optional<AuthorizationCode> savedCode = authorizationCodeRepository.findByCode(code);
		assertTrue(savedCode.isPresent());
	}
}
