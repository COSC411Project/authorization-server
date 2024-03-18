package app.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Component;

import app.entities.AuthorizationCode;
import app.entities.Client;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;

@Component
public class ClientService implements IClientService {

	private final IClientRepository clientRepository;
	private final IAuthorizationCodeRepository authorizationCodeRepository;
	
	public ClientService(IClientRepository clientRepository, IAuthorizationCodeRepository authorizationCodeRepository) {
		this.clientRepository = clientRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
	}
	
	@Override
	public String generateAuthorizationCode(String clientId, String redirectUri) {
		String code = UUID.randomUUID().toString();
		Client client = clientRepository.findByIdentifier(clientId).get();
		AuthorizationCode authorizationCode = new AuthorizationCode(code, redirectUri, LocalDate.now(), client);
		
		authorizationCodeRepository.save(authorizationCode);
		
		return code;
	}

}
