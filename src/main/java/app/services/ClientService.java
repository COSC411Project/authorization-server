package app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;
	
	public ClientService(IClientRepository clientRepository, IAuthorizationCodeRepository authorizationCodeRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
		this.passwordEncoder = passwordEncoder;
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

	@Override
	public boolean isValidAuthorizationCode(String clientId, String clientSecret, String redirectUri) {
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		if (!client.isPresent()) {
			return false;
		}
		
		Optional<AuthorizationCode> latestCode = authorizationCodeRepository.findByClientIdAndRedirectUri(client.get().getId(), redirectUri)
																			.stream()
																			.max((c1, c2) -> c1.getDatetimeIssued().compareTo(c2.getDatetimeIssued()));
		if (!latestCode.isPresent() || !passwordEncoder.matches(clientSecret, latestCode.get().getCode())) {
			return false;
		}
		
		return true;
	}

	
}
