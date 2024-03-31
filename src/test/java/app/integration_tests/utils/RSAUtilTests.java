package app.integration_tests.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAKey;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import app.utils.RSAUtil;

@ActiveProfiles("test")
@SpringBootTest
public class RSAUtilTests {

	@Value("${rsa.private-key.path}")
	private String privateKeyPath;

	@Autowired
	private RSAUtil rsaUtil;
	
	@BeforeEach
	public void setup() throws NoSuchAlgorithmException {
		privateKeyPath = System.getProperty("user.home") + "/" + privateKeyPath;
		
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		
		KeyPair keyPair = generator.generateKeyPair();
		write(privateKeyPath, keyPair.getPrivate());
	}
	
	private static void write(String path, Key key) {
		try (FileOutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write(key.getEncoded());
		} catch (Exception e) {
		}
	}
	
	@AfterEach
	public void cleanup() {
		File file = new File(privateKeyPath);
		file.delete();
	}
	
	@Test
	public void getPrivateKey() {
		// Act
		RSAKey rsaKey = rsaUtil.getPrivateKey(privateKeyPath);
		
		// Assert
		assertNotNull(rsaKey);
	}
}
