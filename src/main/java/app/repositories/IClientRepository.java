package app.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.Client;

public interface IClientRepository extends ListCrudRepository<Client, Integer> {

	Optional<Client> findByIdentifier(String identifier);
	
}
