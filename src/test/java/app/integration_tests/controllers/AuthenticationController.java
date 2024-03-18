package app.integration_tests.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;
import app.repositories.IClientRepository;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthenticationController {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	IClientRepository clientRepository;
	
	private Client client;
	
	@BeforeEach
	public void setup() {
		client = new Client("client", "secret", false, Set.of(GrantType.AUTHORIZATION_CODE), Set.of(Scope.READ), Set.of("http://localhost:5173"));
		clientRepository.save(client);
	}
	
	@AfterEach
	public void cleanup() {
		clientRepository.deleteAll();
	}
	
	@WithMockUser
	@Test
	public void authorize_success() throws Exception {
		String uri = String.format("/oauth2/authorize?client_id=%s&response_type=code", client.getIdentifier());
		String redirectUri = client.getRedirectUris().iterator().next();
		mockMvc.perform(get(uri))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(header().string("Location", containsString(redirectUri + "?code=")));
	}
}
