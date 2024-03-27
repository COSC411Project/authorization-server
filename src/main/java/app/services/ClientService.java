package app.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import app.dtos.TokenDTO;
import app.entities.AuthorizationCode;
import app.entities.Client;
import app.enums.Scope;
import app.models.Authorization;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.repositories.ITokenRepository;
import app.utils.TimeUtil;
import jakarta.transaction.Transactional;

@Component
public class ClientService implements IClientService {

	private final IClientRepository clientRepository;
	private final IAuthorizationCodeRepository authorizationCodeRepository;
	private final PasswordEncoder passwordEncoder;
	private final ITokenRepository tokenRepository;
	
	public ClientService(IClientRepository clientRepository, 
						 IAuthorizationCodeRepository authorizationCodeRepository, 
						 PasswordEncoder passwordEncoder,
						 ITokenRepository tokenRepository) {
		this.clientRepository = clientRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
	}
	
	@Override
	@Transactional
	public String generateAuthorizationCode(String clientId, String redirectUri) {
		String code = UUID.randomUUID().toString();
		Client client = clientRepository.findByIdentifier(clientId).get();
		LocalDateTime datetime = TimeUtil.now();
		
		AuthorizationCode authorizationCode = new AuthorizationCode(code, redirectUri, datetime, client);
		authorizationCodeRepository.save(authorizationCode);
		
		return code;
	}

	@Override
	public boolean isValidAuthorizationCode(String code, String clientId, String clientSecret, String redirectUri) {
		Optional<AuthorizationCode> savedCode = authorizationCodeRepository.findByCode(code);
		if (!savedCode.isPresent()) {
			return false;
		}
		
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		if (!client.isPresent() || !passwordEncoder.matches(clientSecret, client.get().getSecret())) {
			return false;
		}
		
		Optional<AuthorizationCode> latestCode = authorizationCodeRepository.findByClientIdAndRedirectUri(client.get().getId(), redirectUri)
																			.stream()
																			.max((c1, c2) -> c1.getDatetimeIssued().compareTo(c2.getDatetimeIssued()));
		if (!latestCode.isPresent()) {
			return false;
		} else if (latestCode.get().isUsed()) {
			tokenRepository.invalidateToken(latestCode.get().getId());
			return false;
		} else if (!latestCode.get().getCode().equals(code)) {
			savedCode = authorizationCodeRepository.findByCode(code);
			
			if (savedCode.isPresent()) {
				tokenRepository.invalidateToken(savedCode.get().getId());
			}
			return false;
		}
		
		return true;
	}

	@Override
	public TokenDTO generateToken(Authentication authentication, String clientId, Scope scope) {
		
		
		return null;
	}

}
