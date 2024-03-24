package app.entities;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class AuthorizationCode {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String code;
	private String redirectUri;
	private LocalDateTime datetimeIssued;
	private boolean used;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="client_id")
	private Client client;
	
	@OneToOne(mappedBy="authorizationCode")
	private Token token;
	
	public AuthorizationCode() {
	}

	public AuthorizationCode(int id, String code, LocalDateTime datetimeIssued, boolean used) {
		super();
		this.id = id;
		this.code = code;
		this.datetimeIssued = datetimeIssued;
		this.used = used;
	}

	public AuthorizationCode(String code, String redirectUri, LocalDateTime datetimeIssued, Client client) {
		super();
		this.code = code;
		this.redirectUri = redirectUri;
		this.datetimeIssued = datetimeIssued;
		this.client = client;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public LocalDateTime getDatetimeIssued() {
		return datetimeIssued;
	}

	public void setDatetimeIssued(LocalDateTime datetimeIssued) {
		this.datetimeIssued = datetimeIssued;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(client, code, datetimeIssued, id, redirectUri);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorizationCode other = (AuthorizationCode) obj;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return client.getId() == other.client.getId() && Objects.equals(code, other.code)
				&& Objects.equals(datetimeIssued.format(formatter), other.datetimeIssued.format(formatter)) 
				&& id == other.id
				&& Objects.equals(redirectUri, other.redirectUri);
	}

	@Override
	public String toString() {
		return "AuthorizationCode [id=" + id + ", code=" + code + ", redirectUri=" + redirectUri + ", datetimeIssued="
				+ datetimeIssued + ", client=" + client + "]";
	}
	
}
