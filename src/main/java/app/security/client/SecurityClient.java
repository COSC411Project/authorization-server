package app.security.client;

import app.entities.Client;
import app.enums.GrantType;
import app.enums.Scope;

public class SecurityClient implements IClientDetails {
	
	private final Client client;
	
	public SecurityClient(Client client) {
		this.client = client;
	}

	@Override
	public boolean requiresConsent() {
		return client.requiresConsent();
	}

	@Override
	public boolean supportsGrantType(GrantType grantType) {
		if (grantType == null) return false;
		
		return client.getGrantTypes().contains(grantType);
	}

	@Override
	public boolean supportsScope(Scope scope) {
		return client.getScopes().contains(scope);
	}

	@Override
	public boolean supportsRedirectUri(String redirectUri) {
		return client.getRedirectUris().contains(redirectUri);
	}

	@Override
	public String getRedirectUri() {
		return client.getRedirectUris().iterator().next();
	}

}
