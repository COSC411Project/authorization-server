package app.unit_tests.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app.controllers.AuthenticationController;
import app.entities.Client;
import app.enums.GrantType;
import app.enums.ResponseType;
import app.enums.Scope;
import app.exceptions.ClientNotFoundException;
import app.exceptions.UnauthorizedException;
import app.models.Authorization;
import app.security.client.IClientDetailsManager;
import app.security.client.SecurityClient;
import app.services.IClientService;

public class AuthenticationControllerTests {

	private IClientDetailsManager clientDetailsManager;
	private IClientService clientService;
	private AuthenticationController authenticationController;
	private Authorization authorization;
	private String authorizationHeader;
	
	@BeforeEach
	public void setup() {
		clientDetailsManager = mock(IClientDetailsManager.class);
		clientService = mock(IClientService.class);
		authenticationController = new AuthenticationController(clientDetailsManager, clientService);
	
		authorization = new Authorization("client", "secret");
		
		String encodedCredentials = Base64.getEncoder().encodeToString("client:secret".getBytes());
		authorizationHeader += "Basic " + encodedCredentials;
	}
	
	@Test
	public void isValidAuthorizationRequest_true() {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		// Act
		boolean isValid = authenticationController.isValidAuthorizationRequest(securityClient, ResponseType.CODE, Scope.READ, "http://localhost");
	
		// Assert
		assertTrue(isValid);
	}
	
	@Test
	public void isValidAuthorizationRequest_grantTypeNotSupported_false() {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.CLIENT_CREDENTIALS);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		// Act
		boolean isValid = authenticationController.isValidAuthorizationRequest(securityClient, ResponseType.CODE, Scope.READ, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationRequest_redirectUriNotSupported_false() {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		// Act
		boolean isValid = authenticationController.isValidAuthorizationRequest(securityClient, ResponseType.CODE, Scope.READ, "http://google.com");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidAuthorizationRequest_scopeNotSupported_false() {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		// Act
		boolean isValid = authenticationController.isValidAuthorizationRequest(securityClient, ResponseType.CODE, Scope.READ_WRITE, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void token_clientNotValid_exception() {
		// Arrange
		when(clientService.isValidClient(anyString(), anyString())).thenReturn(false);
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			authenticationController.token(authorizationHeader, GrantType.AUTHORIZATION_CODE, "code", null, null, null);
		});
	}
	
	@Test
	public void isValidTokenRequest_true() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "code", Scope.READ, "http://localhost");
	
		// Assert
		assertTrue(isValid);
	}
	
	@Test
	public void isValidTokenRequest_redirectUriNotProvided_true() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "code", Scope.READ, null);
	
		// Assert
		assertTrue(isValid);
	}
	
	@Test
	public void isValidTokenRequest_grantTypeNotSupported_false() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.CLIENT_CREDENTIALS);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "code", Scope.READ, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidTokenRequest_codeNull_false() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, null, Scope.READ, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidTokenRequest_codeBlank_false() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "   ", Scope.READ, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidTokenRequest_scopeNotSupported_false() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(true);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "code", Scope.READ_WRITE, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
	
	@Test
	public void isValidTokenRequest_authorizationCodeNotValid_false() throws ClientNotFoundException, UnauthorizedException {
		// Arrange
		Set<GrantType> grantTypes = Set.of(GrantType.AUTHORIZATION_CODE);
		Set<Scope> scopes = Set.of(Scope.READ);
		Set<String> redirectUris = Set.of("http://localhost");
		Client client = new Client(grantTypes, scopes, redirectUris);
		SecurityClient securityClient = new SecurityClient(client);
		
		when(clientDetailsManager.getClient(anyString())).thenReturn(securityClient);
		when(clientService.isValidAuthorizationCode(anyString(), anyString(), anyString())).thenReturn(false);
		
		// Act
		boolean isValid = authenticationController.isValidTokenRequest(authorization, GrantType.AUTHORIZATION_CODE, "code", Scope.READ, "http://localhost");
	
		// Assert
		assertFalse(isValid);
	}
}
