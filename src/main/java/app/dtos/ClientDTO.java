package app.dtos;

import java.util.List;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(applicationName, grantTypes, id, identifier, redirectUris, requiresConsent, scopes, secret);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientDTO other = (ClientDTO) obj;
		return Objects.equals(applicationName, other.applicationName) && Objects.equals(grantTypes, other.grantTypes)
				&& id == other.id && Objects.equals(identifier, other.identifier)
				&& Objects.equals(redirectUris, other.redirectUris) && requiresConsent == other.requiresConsent
				&& Objects.equals(scopes, other.scopes) && Objects.equals(secret, other.secret);
	}
	
}
