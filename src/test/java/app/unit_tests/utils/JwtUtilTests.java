package app.unit_tests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.auth0.jwt.interfaces.DecodedJWT;

import app.utils.JwtUtil;

public class JwtUtilTests {
	
	private int userId = 1;
	private List<String> authorities = List.of("READ");

	@Test
	public void generate_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		
		// Act
		String jwt = JwtUtil.generate((RSAKey) keyPair.getPrivate(), userId, authorities);

		// Assert
		assertEquals(3, jwt.split("\\.").length);
	}
	
	@Test
	public void decode_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		
		String jwt = JwtUtil.generate((RSAKey) keyPair.getPrivate(), userId, authorities);
		
		// Act
		DecodedJWT decodedJWT = JwtUtil.decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		// Assert
		assertEquals(userId, decodedJWT.getClaim("user").asInt());
		assertEquals(authorities, decodedJWT.getClaim("authorities").asList(String.class));
	}
	
	@Test
	public void decode_null() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair maliciousKeyPair = generator.generateKeyPair();			// Test situations where an attack tries to impersonate a user
		
		String jwt = JwtUtil.generate((RSAKey) maliciousKeyPair.getPrivate(), userId, authorities);
		
		KeyPair originalKeyPair = generator.generateKeyPair();
		
		// Act
		DecodedJWT decodedJWT = JwtUtil.decode(jwt, (RSAPublicKey) originalKeyPair.getPublic());
		
		// Assert
		assertNull(decodedJWT);
	}
}
