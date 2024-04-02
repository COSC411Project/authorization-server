package app.services;

import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.dtos.TokenDTO;
import app.entities.AuthorizationCode;
import app.entities.Client;
import app.entities.Token;
import app.enums.Scope;
import app.exceptions.ApplicationNameTakenException;
import app.exceptions.KeyNotFoundException;
import app.models.JwtOptions;
import app.repositories.IAuthorizationCodeRepository;
import app.repositories.IClientRepository;
import app.repositories.ITokenRepository;
import app.utils.JWTUtil;
import app.utils.Mapper;
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
	
	@Value("${rsa.private-key.path}")
	private String privateKeyPath;
	
	@Value("${rsa.public-key.path}")
	private String publicKeyPath;
	
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
	
	public boolean isValidClient(String clientId, String clientSecret) {
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		if (!client.isPresent() || !passwordEncoder.matches(clientSecret, client.get().getSecret())) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isValidAuthorizationCode(String code, String clientId, String redirectUri) {
		Optional<AuthorizationCode> savedCode = authorizationCodeRepository.findByCode(code);
		if (!savedCode.isPresent()) {
			return false;
		}
		
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		
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
		String fullPrivateKeyPath = System.getProperty("user.home") + "/" + privateKeyPath;
		RSAKey privateKey = rsaUtil.getPrivateKey(fullPrivateKeyPath);
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
		
		String jwt = JWTUtil.generate(options);
		String refreshToken = null;
		
		return new TokenDTO(jwt, "Bearer", secondsTillExpiration, scope, refreshToken);
	}
	
	@Override
	public void saveToken(String clientId, String code, TokenDTO tokenDTO) {
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		Optional<AuthorizationCode> authorizationCode = authorizationCodeRepository.findByCode(code);
		
		Token token = new Token(tokenDTO.getAccessToken(), 
								TimeUtil.now(), 
								tokenDTO.getExpiresIn(), 
								true,
								client.get(),
								authorizationCode.get(),
								tokenDTO.getScope());
		tokenRepository.save(token);
	}
	
	@Override
	public boolean latestTokenForUser(String clientId, Integer userId) {
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		List<Token> tokens = client.get().getTokens();
		if (tokens.size() == 0) {
			return false;
		}
		
		String fullPublicKeyPath = System.getProperty("user.home") + publicKeyPath;
		RSAPublicKey publicKey = (RSAPublicKey) rsaUtil.getPrivateKey(fullPublicKeyPath);
		
		Token lastToken = tokens.get(tokens.size() - 1);
		if (lastToken.getDatetimeIssued().plusSeconds(lastToken.getExpiresIn()).isAfter(TimeUtil.now())) {
			return false;
		}
		
		String jwt = lastToken.getToken();
		DecodedJWT decodedJWT = JWTUtil.decode(jwt, publicKey);
		
		return decodedJWT.getClaim("user").asInt().equals(userId);
	}
	
	@Override
	@Transactional
	public TokenDTO getLatestToken(String clientId) {
		Optional<Client> client = clientRepository.findByIdentifier(clientId);
		List<Token> tokens = client.get().getTokens();
		if (tokens.size() == 0) {
			return null;
		}
		
		Token token = tokens.get(tokens.size() - 1);
		return new TokenDTO(token.getToken(), "Bearer", token.getExpiresIn(), token.getScope(), null);
	}

	@Override
	public ClientDTO register(ClientRegistrationDTO clientRegistration) throws ApplicationNameTakenException {
		Client client = Mapper.map(clientRegistration);
		if (applicationNameTaken(client.getApplicationName())) {
			throw new ApplicationNameTakenException(client.getApplicationName());
		}
		
		String identifier = UUID.randomUUID().toString();
		String secret = UUID.randomUUID().toString();
		
		client.setIdentifier(identifier);
		client.setSecret(passwordEncoder.encode(secret));
		
		Client savedClient = clientRepository.save(client);
		
		return Mapper.map(savedClient, secret);
	}
	
	
	
	private boolean applicationNameTaken(String applicationName) {
		Optional<Client> client = clientRepository.findByApplicationName(applicationName);
		if (client.isPresent()) {
			return true;
		}
		
		return false;
	}
	
	public List<ClientDTO> getClients() {
		List<Client> clients = clientRepository.findAll();
		return clients.stream()
					  .map(client -> Mapper.map(client))
					  .toList();
	}
	
}
