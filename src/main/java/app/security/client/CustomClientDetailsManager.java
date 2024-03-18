package app.security.client;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import app.entities.AuthorizationCode;
import app.entities.Client;
import app.exceptions.ClientNotFoundException;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;

@Component
public class CustomClientDetailsManager implements IClientDetailsManager {

	private final IClientRepository clientRepository;
	private final IAuthorizationCodeRepository authorizationCodeRepository;
	
	public CustomClientDetailsManager(IClientRepository clientRepository, IAuthorizationCodeRepository authorizationCodeRepository) {
		this.clientRepository = clientRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
	}
	
	@Override
	public SecurityClient getClient(String identifier) throws ClientNotFoundException {
		Optional<Client> client = clientRepository.findByIdentifier(identifier);
		if (!client.isPresent()) {
			throw new ClientNotFoundException();
		}
		
		return new SecurityClient(client.get());
	}

	@Override
	public String generateAuthorizationCode(String clientId, String redirectUri) {
		String code = UUID.randomUUID().toString();
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		
		AuthorizationCode authorizationCode = new AuthorizationCode(code, redirectUri, LocalDateTime.now(), client.get());
		authorizationCodeRepository.save(authorizationCode);
		
		return code;
	}

}
