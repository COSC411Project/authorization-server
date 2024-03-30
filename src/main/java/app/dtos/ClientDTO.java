package app.dtos;

import java.util.List;

import app.enums.GrantType;
import app.enums.Scope;

public class ClientDTO {

	private final int id;
	private final String applicationName;
	private final String identifier;
	private final String secret;
	private final boolean requiresConsent;
	private final List<GrantType> grantTypes;
	private final List<Scope> scopes;
	private final List<String> redirectUris;
	
	public ClientDTO(int id, String applicationName, String identifier, String secret, boolean requiresConsent, List<Scope> scopes, List<String> redirectUris, List<GrantType> grantTypes) {
		super();
		this.id = id;
		this.applicationName = applicationName;
		this.identifier = identifier;
		this.secret = secret;
		this.requiresConsent = requiresConsent;
		this.grantTypes = grantTypes;
		this.scopes = scopes;
		this.redirectUris = redirectUris;
	}

	public int getId() {
		return id;
	}
	
	public String getApplicationName() {
		return applicationName;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public boolean isRequiresConsent() {
		return requiresConsent;
	}

	public List<GrantType> getGrantTypes() {
		return grantTypes;
	}

	public List<Scope> getScopes() {
		return scopes;
	}

	public List<String> getRedirectUris() {
		return redirectUris;
	}
	
}
