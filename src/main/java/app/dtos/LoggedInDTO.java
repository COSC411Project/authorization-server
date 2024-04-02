package app.dtos;

public class LoggedInDTO {

	private boolean loggedIn;
	private TokenDTO token;
	
	public LoggedInDTO() {
	}
	
	public LoggedInDTO(TokenDTO token) {
		loggedIn = true;
		this.token = token;
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public TokenDTO getToken() {
		return token;
	}
	
	public void setToken(TokenDTO token) {
		this.token = token;
	}
	
}
