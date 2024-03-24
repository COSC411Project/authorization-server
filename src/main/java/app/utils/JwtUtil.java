package app.utils;

import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {

	public static String generate(RSAKey privateKey, int userId, List<String> authorities) {
		Algorithm algorithm = Algorithm.RSA256(privateKey);
		return JWT.create()
				  .withExpiresAt(Date.valueOf(LocalDate.now()))
				  .withExpiresAt(Instant.now().plusNanos(1_800_000_000_000L))
				  .withClaim("user", userId)
				  .withClaim("authorities", authorities)
				  .sign(algorithm);
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
