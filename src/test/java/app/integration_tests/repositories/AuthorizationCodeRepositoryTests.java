package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
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

@ActiveProfiles("test")
@SpringBootTest
public class AuthorizationCodeRepositoryTests {

	@Autowired
	private IClientRepository clientRepository;
	
	@Autowired
	private IAuthorizationCodeRepository authorizationCodeRepository;
	
	@AfterEach
	public void cleanup() {
		authorizationCodeRepository.deleteAll();
		clientRepository.deleteAll();
	}
	
	@Test
	public void save_success() {
		// Arrange
		String redirectUri = "http://localhost:5173/";
		Client client = new Client("identifier", 
								   "secret", 
								   false, 
								   Set.of(GrantType.AUTHORIZATION_CODE), 
								   Set.of(Scope.READ), 
								   Set.of(redirectUri));
		
		AuthorizationCode code = new AuthorizationCode("code", redirectUri, LocalDate.now(), client);
		
		// Act
		authorizationCodeRepository.save(code);
		
		// Assert
		Optional<AuthorizationCode> savedCode = authorizationCodeRepository.findById(code.getId());
		assertTrue(savedCode.isPresent());
		assertEquals(code, savedCode.get());
	}
}
