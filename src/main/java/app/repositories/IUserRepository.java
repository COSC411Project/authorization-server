package app.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.User;

public interface IUserRepository extends ListCrudRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	void deleteByEmail(String email);
	
}
