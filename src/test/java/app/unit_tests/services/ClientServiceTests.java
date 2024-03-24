package app.unit_tests.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.entities.AuthorizationCode;
import app.entities.Client;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.repositories.ITokenRepository;
import app.services.ClientService;

public class ClientServiceTests {

	private IClientRepository clientRepository;
	private IAuthorizationCodeRepository authorizationCodeRepository;
	private PasswordEncoder passwordEncoder;
	private ITokenRepository tokenRepository;
	
	private ClientService clientService;
	private String code = "code";
	private String clientSecret = "secret";
	
	@BeforeEach
	public void setup() {
		clientRepository = mock(IClientRepository.class);
		authorizationCodeRepository = mock(IAuthorizationCodeRepository.class);
		passwordEncoder = mock(PasswordEncoder.class);
		tokenRepository = mock(ITokenRepository.class);
		
		clientService = new ClientService(clientRepository,
										  authorizationCodeRepository,
										  passwordEncoder,
										  tokenRepository);
	}
	
	@Test
	public void isValidAuthorizationCode_true() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		
		Client client = new Client();
		client.setId(1);
		client.setSecret(clientSecret);
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.of(client));
		
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		AuthorizationCode authorizationCode = new AuthorizationCode(1, code, LocalDateTime.now(), false);
		when(authorizationCodeRepository.findByClientIdAndRedirectUri(anyInt(), anyString())).thenReturn(List.of(authorizationCode));
		
		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertTrue(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_codeNotPresent_false() {
		// Arrange
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_clientNotPresent_false() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.empty());
		
		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_passwordDoesntMatch_false() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		
		Client client = new Client();
		client.setId(1);
		client.setSecret(clientSecret);
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.of(client));
		
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_latestCodeNotPresent_false() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		
		Client client = new Client();
		client.setId(1);
		client.setSecret(clientSecret);
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.of(client));
		
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		when(authorizationCodeRepository.findByClientIdAndRedirectUri(anyInt(), anyString())).thenReturn(List.of());
		
		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_latestCodeIsUsed_false() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		
		Client client = new Client();
		client.setId(1);
		client.setSecret(clientSecret);
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.of(client));
		
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		AuthorizationCode authorizationCode = new AuthorizationCode(1, code, LocalDateTime.now(), true);
		when(authorizationCodeRepository.findByClientIdAndRedirectUri(anyInt(), anyString())).thenReturn(List.of(authorizationCode));
		
		// Act
		boolean isValid = clientService.isValidAuthorizationCode(code, "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationCode_latestCodeDoesntEqualCode_false() {
		// Arrange		
		when(authorizationCodeRepository.findByCode(anyString())).thenReturn(Optional.of(new AuthorizationCode()));
		
		Client client = new Client();
		client.setId(1);
		client.setSecret(clientSecret);
		when(clientRepository.findByIdentifier(anyString())).thenReturn(Optional.of(client));
		
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		AuthorizationCode authorizationCode = new AuthorizationCode(1, code, LocalDateTime.now(), false);
		when(authorizationCodeRepository.findByClientIdAndRedirectUri(anyInt(), anyString())).thenReturn(List.of(authorizationCode));
		
		// Act
		boolean isValid = clientService.isValidAuthorizationCode("different code", "identifer", clientSecret, "redirect uri");
	
		// Assert
		assertFalse(isValid);
	}
}
