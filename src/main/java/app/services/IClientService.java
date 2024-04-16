package app.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.dtos.TokenDTO;
import app.enums.Scope;
import app.exceptions.ApplicationNameTakenException;
import app.exceptions.KeyNotFoundException;

public interface IClientService {

	String generateAuthorizationCode(String clientId, String redirectUri);
	boolean isValidAuthorizationCode(String code, String clientId, String redirectUri);
	
	TokenDTO generateToken(Authentication authentication, String clientId, Scope scope) throws KeyNotFoundException;
	void saveToken(String clientId, String code, TokenDTO tokenDTO);
	boolean latestTokenForUser(String clientId, Integer userId);
	TokenDTO getLatestToken(String clientId);
	
	public boolean isValidClient(String clientId, String clientSecret);
	List<ClientDTO> getClients();
	ClientDTO register(ClientRegistrationDTO clientRegistration) throws ApplicationNameTakenException;
	
}
