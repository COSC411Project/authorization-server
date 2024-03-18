package app.entities;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AuthorizationCode {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String code;
	private String redirectUri;
	private LocalDate datetimeIssued;
	
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="client_id")
	private Client client;
	
	public AuthorizationCode() {
	}

	public AuthorizationCode(String code, String redirectUri, LocalDate datetimeIssued, Client client) {
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

	public LocalDate getDatetimeIssued() {
		return datetimeIssued;
	}

	public void setDatetimeIssued(LocalDate datetimeIssued) {
		this.datetimeIssued = datetimeIssued;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
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
		return client.getId() == other.client.getId() && Objects.equals(code, other.code)
				&& Objects.equals(datetimeIssued, other.datetimeIssued) && id == other.id
				&& Objects.equals(redirectUri, other.redirectUri);
	}
	
}
