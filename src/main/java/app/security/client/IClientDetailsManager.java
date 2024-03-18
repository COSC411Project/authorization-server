package app.security.client;

import app.exceptions.ClientNotFoundException;

public interface IClientDetailsManager {

	SecurityClient getClient(String clientIdentifier) throws ClientNotFoundException;
	String generateAuthorizationCode(String clientId, String redirectUri);
	
}
