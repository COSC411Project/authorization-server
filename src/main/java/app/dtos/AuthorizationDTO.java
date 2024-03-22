package app.dtos;

public class AuthorizationDTO {

	private String clientId;
	private String clientSecret;
	
	public AuthorizationDTO(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
}
