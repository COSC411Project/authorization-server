package app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.AuthorizationCode;

public interface IAuthorizationCodeRepository extends ListCrudRepository<AuthorizationCode, Integer> {
	
	Optional<AuthorizationCode> findByCode(String code);
	List<AuthorizationCode> findByClientIdAndRedirectUri(int clientId, String redirectUri);
	
}
