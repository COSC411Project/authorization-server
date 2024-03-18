package app.integration_tests.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationController {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	IClientRepository clientRepository;
	
	@Autowired
	IAuthorizationCodeRepository authorizationCodeRepository;
	
	private Client client;
	
	@BeforeEach
	public void setup() {
		client = new Client("client", "secret", false, Set.of(GrantType.AUTHORIZATION_CODE), Set.of(Scope.READ), Set.of("http://localhost:5173"));
		clientRepository.save(client);
	}
	
	@AfterEach
	public void cleanup() {
		authorizationCodeRepository.deleteAll();
		clientRepository.deleteAll();
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
	
}
