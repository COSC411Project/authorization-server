package app.repositories;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.User;

public interface UserRepository extends ListCrudRepository<User, Integer> {

}
