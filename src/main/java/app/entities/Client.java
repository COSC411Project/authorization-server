package app.entities;

import java.util.Objects;
import java.util.Set;

import app.enums.GrantType;
import app.enums.Scope;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String identifier;
	private String secret;
	private boolean requiresConsent;
	
	@ElementCollection(targetClass=GrantType.class, fetch=FetchType.EAGER)
	@CollectionTable(name="client_grant_type")
	@Enumerated(value=EnumType.STRING)
	@Column(name="grant_type")
	private Set<GrantType> grantTypes;
	
	@ElementCollection(targetClass=Scope.class, fetch=FetchType.EAGER)
	@CollectionTable(name="client_scope")
	@Enumerated(value=EnumType.STRING)
	@Column(name="scope")
	private Set<Scope> scopes;
	
	public Client() {	
	}
	
	public Client(String identifier, String secret, boolean requiresConsent, Set<GrantType> grantTypes, Set<Scope> scopes) {
		super();
		this.identifier = identifier;
		this.secret = secret;
		this.requiresConsent = requiresConsent;
		this.grantTypes = grantTypes;
		this.scopes = scopes;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getSecret() {
		return secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public boolean isRequiresConsent() {
		return requiresConsent;
	}
	
	public void setRequiresConsent(boolean requiresConsent) {
		this.requiresConsent = requiresConsent;
	}

	public Set<GrantType> getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(Set<GrantType> grantTypes) {
		this.grantTypes = grantTypes;
	}

	public Set<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(Set<Scope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(grantTypes, id, identifier, requiresConsent, scopes, secret);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return grantTypes.equals(other.grantTypes) && id == other.id
				&& Objects.equals(identifier, other.identifier) && requiresConsent == other.requiresConsent
				&& scopes.equals(other.scopes) && Objects.equals(secret, other.secret);
	}
	
}
