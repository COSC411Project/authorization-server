package app.entities;

import java.util.List;
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
import jakarta.persistence.OneToMany;

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
	
	@ElementCollection(targetClass=String.class, fetch=FetchType.EAGER)
	@CollectionTable(name="client_redirect_uri")
	@Column(name="redirect_uri")
	private Set<String> redirectUris;
	
	@OneToMany(mappedBy="client")
	private List<AuthorizationCode> codes;
	
	@OneToMany(mappedBy="client")
	private List<Token> tokens;
	
	public Client() {	
	}
	
	public Client(String identifier, String secret, boolean requiresConsent, Set<GrantType> grantTypes, Set<Scope> scopes, Set<String> redirectUris) {
		super();
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
	
	public boolean requiresConsent() {
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

	public Set<String> getRedirectUris() {
		return redirectUris;
	}

	public void setRedirectUris(Set<String> redirectUris) {
		this.redirectUris = redirectUris;
	}

	public List<AuthorizationCode> getCodes() {
		return codes;
	}

	public void setCodes(List<AuthorizationCode> codes) {
		this.codes = codes;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public boolean isRequiresConsent() {
		return requiresConsent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(grantTypes, id, identifier, redirectUris, requiresConsent, scopes, secret);
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
		return Objects.equals(grantTypes, other.grantTypes) && id == other.id
				&& Objects.equals(identifier, other.identifier) && Objects.equals(redirectUris, other.redirectUris)
				&& requiresConsent == other.requiresConsent && Objects.equals(scopes, other.scopes)
				&& Objects.equals(secret, other.secret);
	}
	
}
