package app.security.client;

import java.util.Optional;

import org.springframework.stereotype.Component;

import app.entities.Client;
import app.exceptions.ClientNotFoundException;
import app.repositories.IClientRepository;

@Component
public class CustomClientDetailsManager implements IClientDetailsManager {

	private final IClientRepository clientRepository;
	
	public CustomClientDetailsManager(IClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	@Override
	public SecurityClient getClient(String identifier) throws ClientNotFoundException {
		Optional<Client> client = clientRepository.findByIdentifier(identifier);
		if (!client.isPresent()) {
			throw new ClientNotFoundException();
		}
		
		return new SecurityClient(client.get());
	}

}
