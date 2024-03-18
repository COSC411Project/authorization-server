package app.security.client;

import app.enums.GrantType;
import app.enums.ResponseType;
import app.enums.Scope;

public interface IClientDetails {

	boolean requiresConsent();
	boolean supportsGrantType(GrantType grantType);
	boolean supportsScope(Scope scope);
	boolean supportsRedirectUri(String redirectUri);
	String getRedirectUri();
	
}
