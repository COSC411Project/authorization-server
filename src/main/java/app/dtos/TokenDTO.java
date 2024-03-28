package app.dtos;

import app.enums.Scope;

public class TokenDTO {

	private String accessToken;
	private String tokenType;
	private int expiresIn;
	private Scope scope;
	private String refreshToken;
	
	public TokenDTO(String accessToken, String tokenType, int expiresIn, Scope scope, String refreshToken) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.scope = scope;
		this.refreshToken = refreshToken;
	}

	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
