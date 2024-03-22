package app.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.AuthorizationCode;

public interface IAuthorizationCodeRepository extends ListCrudRepository<AuthorizationCode, Integer> {

	List<AuthorizationCode> findByClientIdAndRedirectUri(String clientId, String redirectUri);
	
}
