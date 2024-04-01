package app.dtos;

import java.util.List;

import app.enums.GrantType;
import app.enums.Scope;

public class ClientRegistrationDTO {

	private String applicationName;
	private boolean scopeRequired;
	private List<GrantType> grantTypes;
	private List<Scope> scopes;
	private List<String> redirectUris;
	
	public ClientRegistrationDTO(String applicationName, boolean scopeRequired, List<GrantType> grantTypes,
			List<Scope> scopes, List<String> redirectUris) {
		super();
		this.applicationName = applicationName;
		this.scopeRequired = scopeRequired;
		this.grantTypes = grantTypes;
		this.scopes = scopes;
		this.redirectUris = redirectUris;
	}

	public String getApplicationName() {
		return applicationName;
	}
	
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public boolean isScopeRequired() {
		return scopeRequired;
	}
	
	public void setScopeRequired(boolean scopeRequired) {
		this.scopeRequired = scopeRequired;
	}
	
	public List<GrantType> getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(List<GrantType> grantTypes) {
		this.grantTypes = grantTypes;
	}

	public List<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}

	public List<String> getRedirectUris() {
		return redirectUris;
	}
	
	public void setRedirectUris(List<String> redirectUris) {
		this.redirectUris = redirectUris;
	}
	
}
