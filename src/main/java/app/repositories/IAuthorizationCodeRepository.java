package app.repositories;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.AuthorizationCode;

public interface IAuthorizationCodeRepository extends ListCrudRepository<AuthorizationCode, Integer> {

}
