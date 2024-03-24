package app.unit_tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Base64;

import org.junit.jupiter.api.Test;

import app.exceptions.UnauthorizedException;
import app.models.Authorization;
import app.utils.AuthorizationUtil;

public class AuthorizationUtilTests {

	private String clientId = "client";
	private String clientSecret = "secret";
	
	@Test
	public void parseHeader_success() throws UnauthorizedException {
		// Arrange
		String clientCredentials = clientId + ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		String header = "Basic " + encodedCredentials;
		
		// Act
		Authorization authorization = AuthorizationUtil.parseHeader(header);
		
		// Assert
		assertEquals(clientId, authorization.getClientId());
		assertEquals(clientSecret, authorization.getClientSecret());
	}
	
	@Test
	public void parseHeader_noBasic_exception() {
		// Arrange
		String clientCredentials = clientId + ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			AuthorizationUtil.parseHeader(encodedCredentials);
		});
	}
	
	@Test
	public void parseHeader_missingClientSecret_exception() {
		// Arrange
		String clientCredentials = clientId + ":";
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			AuthorizationUtil.parseHeader(encodedCredentials);
		});
	}
	
	@Test
	public void parseHeader_missingClientId_exception() {
		// Arrange
		String clientCredentials = ":" + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			AuthorizationUtil.parseHeader(encodedCredentials);
		});
	}
	
	@Test
	public void parseHeader_missingColon_exception() {
		// Arrange
		String clientCredentials = clientId + clientSecret;
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			AuthorizationUtil.parseHeader(encodedCredentials);
		});
	}
	
	@Test
	public void parseHeader_multipleColons_exception() {
		// Arrange
		String clientCredentials = clientId + ":" + clientSecret + ":";
		String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		
		// Act and Assert
		assertThrows(UnauthorizedException.class, () -> {
			AuthorizationUtil.parseHeader(encodedCredentials);
		});
	}
}
