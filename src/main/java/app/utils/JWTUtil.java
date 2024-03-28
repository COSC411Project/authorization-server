package app.utils;

import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import app.models.JwtOptions;

public class JWTUtil {

	public static String generate(JwtOptions options) {
		long nanoSeconds = convertToNanoSeconds(options.getSecondsTillExpiration());
		
		Algorithm algorithm = Algorithm.RSA256(options.getPrivateKey());
		
		Builder builder = JWT.create()
							 .withExpiresAt(Date.valueOf(LocalDate.now()))
							 .withExpiresAt(Instant.now().plusNanos(nanoSeconds))
							 .withClaim("user", options.getUserId())
							 .withClaim("authorities", options.getAuthorities());
							 
		if (options.getClientId() != null) {
			builder.withClaim("client", options.getClientId());
		}
		
		if (options.getScope() != null) {
			builder.withClaim("scope", options.getScope().name());
		}
							 
		return builder.sign(algorithm);
	}
	
	private static Long convertToNanoSeconds(int seconds) {
		return seconds * 1_000_000_000L;
	}
	
	public static DecodedJWT decode(String token, RSAPublicKey publicKey) {
		Algorithm algorithm = Algorithm.RSA256(publicKey);
		JWTVerifier verifier = JWT.require(algorithm).build();
		
		try {
			return verifier.verify(token);
		} catch (Exception ex) {
			return null;
		}
	}
}
