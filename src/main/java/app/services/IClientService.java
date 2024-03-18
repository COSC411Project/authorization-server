package app.services;

public interface IClientService {

	String generateAuthorizationCode(String clientId, String redirectUri);
	
}
