package app.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.User;
import app.enums.AuthProvider;
import jakarta.transaction.Transactional;

public interface IUserRepository extends ListCrudRepository<User, Integer> {

	Optional<User> findByEmail(String email);
	
	Optional<User> findByUsernameAndAuthProvider(String username, AuthProvider authProvider);
	
	@Transactional
	void deleteByEmail(String email);
	
}
