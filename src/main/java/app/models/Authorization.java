package app.models;

public class Authorization {

	private String clientId;
	private String clientSecret;
	
	public Authorization(String clientId, String clientSecret) {
		super();
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
