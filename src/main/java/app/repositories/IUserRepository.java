package app.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.User;
import jakarta.transaction.Transactional;

public interface IUserRepository extends ListCrudRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	@Transactional
	void deleteByEmail(String email);
	
}
