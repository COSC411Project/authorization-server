package app.security.client;

import app.exceptions.ClientNotFoundException;

public interface IClientDetailsManager {

	SecurityClient getClient(String identifier) throws ClientNotFoundException;
	
}
