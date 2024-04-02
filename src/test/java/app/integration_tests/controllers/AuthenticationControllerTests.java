package app.integration_tests.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.services.ClientService;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationControllerTests {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	IClientRepository clientRepository;
	
	@Autowired
	IAuthorizationCodeRepository authorizationCodeRepository;
	
	@Autowired
	ClientService clientService;
	
	@Value("${rsa.private-key.path}")
	private String privateKeyPath;
	
	private Client client;
	private String code;
	
	@BeforeEach
	public void setup() throws NoSuchAlgorithmException {
		client = new Client(UUID.randomUUID().toString(), 
							UUID.randomUUID().toString(), 
							"secret", 
							false, 
							Set.of(GrantType.AUTHORIZATION_CODE), 
							Set.of(Scope.READ), 
							Set.of("http://localhost:5173"));
		clientRepository.save(client);
		
		code = clientService.generateAuthorizationCode(client.getIdentifier(), client.getRedirectUris().iterator().next());
		
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
		authorizationCodeRepository.deleteAll();
		clientRepository.deleteAll();
		new File(privateKeyPath).delete();
	}
	
	@Test
	public void authorize_notLoggedIn_failure() throws Exception {
		String uri = String.format("/oauth2/authorize?client_id=%s&response_type=code", client.getIdentifier());
		mockMvc.perform(get(uri))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(header().string("Location", containsString("http://localhost/login")));
	}
	
	@WithMockUser
	@Test
	public void authorize_noParams_failure() throws Exception {
		mockMvc.perform(get("/oauth2/authorize"))
			   .andExpect(status().is4xxClientError());
	}
	
	@WithMockUser
	@Test
	public void authorize_duplicateParameters_failure() throws Exception  {
		String uri = String.format("/oauth2/authorize?client_id=%s&response_type=code&client_id=sean", client.getIdentifier());
		mockMvc.perform(get(uri))
			   .andExpect(status().is4xxClientError());
	}
	
	@WithMockUser
	@Test
	public void authorize_noResponseType_failure() throws Exception {
		String uri = String.format("/oauth2/authorize?client_id=%s", client.getIdentifier());
		mockMvc.perform(get(uri))
			   .andExpect(status().is4xxClientError());
	}
	
	@WithMockUser
	@Test
	public void authorize_defaultRedirectUri_success() throws Exception {
		String uri = String.format("/oauth2/authorize?client_id=%s&response_type=code", client.getIdentifier());
		String redirectUri = client.getRedirectUris().iterator().next();
		mockMvc.perform(get(uri))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(header().string("Location", containsString(redirectUri + "?code=")));
	}

	@WithMockUser
	@Test
	public void authorize_matchingRedirectUri_success() throws Exception {
		String redirectUri = client.getRedirectUris().iterator().next();
		String uri = String.format("/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=%s", client.getIdentifier(), redirectUri);
		mockMvc.perform(get(uri))
		   .andExpect(status().is3xxRedirection())
		   .andExpect(header().string("Location", containsString(redirectUri + "?code=")));
	}
	
	@WithMockUser
	@Test
	public void token_success() throws Exception {
		String clientCredentials = client.getIdentifier() + ":" + client.getSecret();
		String authorization = Base64.getEncoder().encodeToString(clientCredentials.getBytes());
		String uri = String.format("/oauth2/token?grant_type=authorization_code&code=%s", code);
		
		var authentication = new UsernamePasswordAuthenticationToken(1, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
		
		mockMvc.perform(post(uri).header("Authorization", "Basic " + authorization)
								 .with(authentication(authentication)))
			   .andExpect(status().is2xxSuccessful());
	}
}
