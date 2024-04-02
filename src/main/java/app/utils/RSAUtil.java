package app.utils;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RSAUtil {

	public RSAKey getPrivateKey(String privateKeyPath) {
		try (FileInputStream inputStream = new FileInputStream(privateKeyPath)) {
			byte[] bytes = inputStream.readAllBytes();
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAKey) keyFactory.generatePrivate(keySpec);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		return null;
	}

	public static PublicKey getPublicKey(String publicKeyPath) {
		try (FileInputStream inputStream = new FileInputStream(publicKeyPath)) {
			byte[] bytes = inputStream.readAllBytes();
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (Exception ex) {
		}
		
		return null;
	}
}
