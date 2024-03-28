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

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.enums.Scope;
import app.models.JwtOptions;
import app.utils.JWTUtil;

public class JWTUtilTests {
	
	private int userId = 1;
	private List<String> authorities = List.of("READ");
	private String clientId = "client";
	private Scope scope = Scope.READ;
	
	@Test
	public void generate_clientAndScopeNotProvided_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		RSAKey privateKey = (RSAKey) keyPair.getPrivate();

		JwtOptions options = new JwtOptions(privateKey, userId, authorities, null, null, 10);
		
		// Act
		String jwt = JWTUtil.generate(options);
		
		// Assert
		DecodedJWT decodedJWT = decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		int actualUserId = decodedJWT.getClaim("user").asInt();
		List<String> actualAuthorities = decodedJWT.getClaim("authorities").asList(String.class);
		String actualClientId = decodedJWT.getClaim("client").asString();
		String scope = decodedJWT.getClaim("scope").asString();
		
		assertEquals(userId, actualUserId);
		assertEquals(authorities.get(0), actualAuthorities.get(0));
		assertEquals(null, actualClientId);
		assertEquals(null, scope);
	}
	
	private DecodedJWT decode(String jwt, RSAPublicKey publicKey) {
		Algorithm algorithm = Algorithm.RSA256(publicKey);
		JWTVerifier verifier = JWT.require(algorithm)
								  .build();
		return verifier.verify(jwt);
	}
	
	@Test
	public void generate_clientProvided_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		RSAKey privateKey = (RSAKey) keyPair.getPrivate();

		JwtOptions options = new JwtOptions(privateKey, userId, authorities, clientId, null, 10);
		
		// Act
		String jwt = JWTUtil.generate(options);

		// Assert
		DecodedJWT decodedJWT = decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		int actualUserId = decodedJWT.getClaim("user").asInt();
		List<String> actualAuthorities = decodedJWT.getClaim("authorities").asList(String.class);
		String actualClientId = decodedJWT.getClaim("client").asString();
		String scope = decodedJWT.getClaim("scope").asString();
		
		assertEquals(userId, actualUserId);
		assertEquals(authorities.get(0), actualAuthorities.get(0));
		assertEquals(clientId, actualClientId);
		assertEquals(null, scope);
	}
	
	@Test
	public void generate_scopeProvided_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		RSAKey privateKey = (RSAKey) keyPair.getPrivate();

		JwtOptions options = new JwtOptions(privateKey, userId, authorities, null, scope, 10);
		
		// Act
		String jwt = JWTUtil.generate(options);

		// Assert
		DecodedJWT decodedJWT = decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		int actualUserId = decodedJWT.getClaim("user").asInt();
		List<String> actualAuthorities = decodedJWT.getClaim("authorities").asList(String.class);
		String actualClientId = decodedJWT.getClaim("client").asString();
		String scope = decodedJWT.getClaim("scope").asString();
		
		assertEquals(userId, actualUserId);
		assertEquals(authorities.get(0), actualAuthorities.get(0));
		assertEquals(null, actualClientId);
		assertEquals(scope, scope);
	}

	@Test
	public void generate_clientAndScopeProvided_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		RSAKey privateKey = (RSAKey) keyPair.getPrivate();

		JwtOptions options = new JwtOptions(privateKey, userId, authorities, clientId, scope, 10);
		
		// Act
		String jwt = JWTUtil.generate(options);

		// Assert
		DecodedJWT decodedJWT = decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		int actualUserId = decodedJWT.getClaim("user").asInt();
		List<String> actualAuthorities = decodedJWT.getClaim("authorities").asList(String.class);
		String actualClientId = decodedJWT.getClaim("client").asString();
		String scope = decodedJWT.getClaim("scope").asString();
		
		assertEquals(userId, actualUserId);
		assertEquals(authorities.get(0), actualAuthorities.get(0));
		assertEquals(clientId, actualClientId);
		assertEquals(scope, scope);
	}
	
	@Test
	public void decode_success() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		
		JwtOptions options = new JwtOptions((RSAKey) keyPair.getPrivate(), userId, authorities, clientId, scope, 10);
		String jwt = JWTUtil.generate(options);
		
		// Act
		DecodedJWT decodedJWT = JWTUtil.decode(jwt, (RSAPublicKey) keyPair.getPublic());
		
		// Assert
		assertEquals(userId, decodedJWT.getClaim("user").asInt());
		assertEquals(authorities, decodedJWT.getClaim("authorities").asList(String.class));
		assertEquals(clientId, decodedJWT.getClaim("client").asString());
		assertEquals(scope.name(), decodedJWT.getClaim("scope").asString());
	}
	
	@Test
	public void decode_null() throws NoSuchAlgorithmException {
		// Arrange
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair originalKeyPair = generator.generateKeyPair();
		KeyPair maliciousKeyPair = generator.generateKeyPair();			// Test situations where an attack tries to impersonate a user
	
		JwtOptions options = new JwtOptions((RSAKey) maliciousKeyPair.getPrivate(), userId, authorities, clientId, scope, 10);
		String jwt = JWTUtil.generate(options);
		
		// Act
		DecodedJWT decodedJWT = JWTUtil.decode(jwt, (RSAPublicKey) originalKeyPair.getPublic());
		
		// Assert
		assertNull(decodedJWT);
	}
}
