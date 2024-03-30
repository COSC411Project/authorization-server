package app.services;

import org.springframework.security.core.Authentication;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.dtos.TokenDTO;
import app.enums.Scope;
import app.exceptions.KeyNotFoundException;

public interface IClientService {

	String generateAuthorizationCode(String clientId, String redirectUri);
	boolean isValidAuthorizationCode(String code, String clientId, String clientSecret, String redirectUri);
	TokenDTO generateToken(Authentication authentication, String clientId, Scope scope) throws KeyNotFoundException;
	ClientDTO register(ClientRegistrationDTO clientRegistration);
	
}
