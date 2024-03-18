package app.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import app.entities.AuthorizationCode;
import app.entities.Client;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.utils.TimeUtils;

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
		LocalDateTime datetime = TimeUtils.now();
		
		AuthorizationCode authorizationCode = new AuthorizationCode(code, redirectUri, datetime, client);
		authorizationCodeRepository.save(authorizationCode);
		
		return code;
	}

}
