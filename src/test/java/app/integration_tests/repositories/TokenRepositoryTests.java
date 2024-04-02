package app.integration_tests.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.time.LocalDateTime;
import java.util.List;
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
import app.entities.Token;
import app.enums.GrantType;
import app.enums.Scope;
import app.models.JwtOptions;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.repositories.ITokenRepository;
import app.utils.JWTUtil;
import app.utils.TimeUtil;

@ActiveProfiles("test")
@SpringBootTest
public class TokenRepositoryTests {

	@Autowired
	private ITokenRepository tokenRepository;
	
	@Autowired
	private IClientRepository clientRepository;
	
	@Autowired
	private IAuthorizationCodeRepository authorizationCodeRepository;
	
	private Token token;
	private Client client;
	private AuthorizationCode authorizationCode;
	
	@BeforeEach
	public void setup() throws NoSuchAlgorithmException {
		String redirectUri = "http://localhost:5173/";
		client = new Client(UUID.randomUUID().toString(),
							"identifier", 
						    "secret", 
						    false, 
						    Set.of(GrantType.AUTHORIZATION_CODE), 
						    Set.of(Scope.READ), 
						    Set.of(redirectUri));
		
		LocalDateTime datetime = TimeUtil.now();
		authorizationCode = new AuthorizationCode("code", redirectUri, datetime, client);
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		RSAKey privateKey = (RSAKey) keyPair.getPrivate();
		
		JwtOptions options = new JwtOptions(privateKey, 1, List.of("READ"), "client", null, 30 * 60);
		String jwt = JWTUtil.generate(options);

		token = new Token(jwt, TimeUtil.now(), true);
		token.setClient(client);
		token.setAuthorizationCode(authorizationCode);
	}
	
	@AfterEach
	public void cleanup() {
		tokenRepository.deleteAll();
		authorizationCodeRepository.deleteAll();
		clientRepository.deleteAll();
	}
	
	@Test
	public void save_success() {
		// Act
		tokenRepository.save(token);
		
		// Assert
		Optional<Token> savedToken = tokenRepository.findById(token.getId());
		assertTrue(savedToken.isPresent());
		assertEquals(token, savedToken.get());
	}
	
	@Test
	public void invalidateToken_success() {
		// Arrange
		tokenRepository.save(token);
		
		// Act
		tokenRepository.invalidateToken(authorizationCode.getId());
		
		// Assert
		Optional<Token> savedToken = tokenRepository.findById(token.getId());
		assertFalse(savedToken.get().isValid());
	}
}
