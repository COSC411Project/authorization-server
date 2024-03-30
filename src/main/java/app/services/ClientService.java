package app.services;

import java.security.interfaces.RSAKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.dtos.TokenDTO;
import app.entities.AuthorizationCode;
import app.entities.Client;
import app.enums.Scope;
import app.exceptions.KeyNotFoundException;
import app.models.JwtOptions;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.repositories.ITokenRepository;
import app.utils.JWTUtil;
import app.utils.RSAUtil;
import app.utils.TimeUtil;
import jakarta.transaction.Transactional;

@Component
public class ClientService implements IClientService {

	private final IClientRepository clientRepository;
	private final IAuthorizationCodeRepository authorizationCodeRepository;
	private final PasswordEncoder passwordEncoder;
	private final ITokenRepository tokenRepository;
	private final RSAUtil rsaUtil;
	
	public ClientService(IClientRepository clientRepository, 
						 IAuthorizationCodeRepository authorizationCodeRepository, 
						 PasswordEncoder passwordEncoder,
						 ITokenRepository tokenRepository, 
						 RSAUtil rsaUtil) {
		this.clientRepository = clientRepository;
		this.authorizationCodeRepository = authorizationCodeRepository;
		this.passwordEncoder = passwordEncoder;
		this.tokenRepository = tokenRepository;
		this.rsaUtil = rsaUtil;
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
	public TokenDTO generateToken(Authentication authentication, String clientId, Scope scope) throws KeyNotFoundException {
		RSAKey privateKey = rsaUtil.getPrivateKey();
		if (privateKey == null) {
			throw new KeyNotFoundException();
		}
		
		int userId = (int) authentication.getPrincipal();
		List<String> authorities = authentication.getAuthorities()
												 .stream()
												 .map(authority -> authority.getAuthority())
												 .toList();
		
		int secondsTillExpiration = 30 * 60;
		JwtOptions options = new JwtOptions(privateKey, userId, authorities, clientId, scope, secondsTillExpiration);
		
		String token = JWTUtil.generate(options);
		String refreshToken = null;
		
		return new TokenDTO(token, "Bearer", secondsTillExpiration, scope, refreshToken);
	}

	@Override
	public ClientDTO register(ClientRegistrationDTO clientRegistration) {
		Client client = map(clientRegistration);
		
		String secret = UUID.randomUUID().toString();
		
		client.setSecret(passwordEncoder.encode(secret));
		Client savedClient = clientRepository.save(client);
		
		return map(savedClient, secret);
	}
	
	public Client map(ClientRegistrationDTO clientRegistration) {
		String identifier = UUID.randomUUID().toString();
		return new Client(identifier, 
						  null, 
					      clientRegistration.isScopeRequired(), 
						  new HashSet<>(clientRegistration.getGrantTypes()),
						  new HashSet<>(clientRegistration.getScopes()),
						  new HashSet<>(clientRegistration.getRedirectUris()));
	}
	
	public ClientDTO map(Client client, String originalSecret) {
		return new ClientDTO(client.getId(),
							 client.getApplicationName(),
							 client.getIdentifier(),
							 originalSecret,
							 client.requiresConsent(),
							 client.getScopes().stream().toList(),
							 client.getRedirectUris().stream().toList(),
							 client.getGrantTypes().stream().toList());
	}
}
