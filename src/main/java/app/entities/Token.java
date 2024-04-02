package app.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import app.enums.Scope;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Token {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String token;
	private LocalDateTime datetimeIssued;
	private int expiresIn;
	private boolean valid;
	
	@Enumerated(value=EnumType.STRING)
	private Scope scope;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="client_id")
	private Client client;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="authorization_code_id")
	private AuthorizationCode authorizationCode;
	
	public Token() {
	}

	public Token(String token, LocalDateTime datetimeIssued, boolean valid) {
		super();
		this.token = token;
		this.datetimeIssued = datetimeIssued;
		this.valid = valid;
	}

	public Token(String token, LocalDateTime datetimeIssued, int expiresIn, boolean valid, Client client,
			AuthorizationCode authorizationCode, Scope scope) {
		super();
		this.token = token;
		this.datetimeIssued = datetimeIssued;
		this.expiresIn = expiresIn;
		this.valid = valid;
		this.client = client;
		this.authorizationCode = authorizationCode;
		this.scope = scope;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getDatetimeIssued() {
		return datetimeIssued;
	}

	public void setDatetimeIssued(LocalDateTime datetimeIssued) {
		this.datetimeIssued = datetimeIssued;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public AuthorizationCode getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(AuthorizationCode authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authorizationCode, client, datetimeIssued, id, token, valid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		return ((authorizationCode == null && other.authorizationCode == null) || authorizationCode.getId() == other.authorizationCode.getId())
				&& ((client == null && other.client == null) || client.getId() ==  other.client.getId())
				&& Objects.equals(datetimeIssued, other.datetimeIssued) 
				&& id == other.id
				&& Objects.equals(token, other.token) && valid == other.valid;
	}
	
	
	
}
