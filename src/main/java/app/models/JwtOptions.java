package app.models;

import java.security.interfaces.RSAKey;
import java.util.List;

import app.enums.Scope;

public class JwtOptions {

	private final RSAKey privateKey;
	private final int userId;
	private final List<String> authorities;
	private final String clientId;
	private final Scope scope;
	private final int secondsTillExpiration;
	
	public JwtOptions(RSAKey privateKey, int userId, List<String> authorities, String clientId, Scope scope, int secondsTillExpiration) {
		this.privateKey = privateKey;
		this.userId = userId;
		this.authorities = authorities;
		this.clientId = clientId;
		this.scope = scope;
		this.secondsTillExpiration = secondsTillExpiration;
	}

	public RSAKey getPrivateKey() {
		return privateKey;
	}

	public int getUserId() {
		return userId;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public String getClientId() {
		return clientId;
	}

	public Scope getScope() {
		return scope;
	}

	public int getSecondsTillExpiration() {
		return secondsTillExpiration;
	}
	
}
