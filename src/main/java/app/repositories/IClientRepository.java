package app.repositories;

import org.springframework.data.repository.ListCrudRepository;

import app.entities.Client;

public interface IClientRepository extends ListCrudRepository<Client, Integer> {

}
