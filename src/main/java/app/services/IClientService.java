package app.services;

public interface IClientService {

	String generateAuthorizationCode(String clientId, String redirectUri);
	boolean isValidAuthorizationCode(String clientId, String clientSecret, String redirectUri);
	
	
}
