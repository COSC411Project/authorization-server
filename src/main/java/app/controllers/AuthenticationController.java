package app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.converters.GrantTypeConverter;
import app.converters.ResponseTypeConverter;
import app.converters.ScopeConverter;
import app.dtos.AuthorizationDTO;
import app.dtos.TokenDTO;
import app.enums.GrantType;
import app.enums.ResponseType;
import app.enums.Scope;
import app.exceptions.ClientNotFoundException;
import app.exceptions.InvalidRequestException;
import app.security.client.IClientDetailsManager;
import app.security.client.SecurityClient;
import app.services.IClientService;

@RestController
@RequestMapping("/oauth2")
public class AuthenticationController {
	
	private final IClientDetailsManager clientDetailsManager;
	private final IClientService clientService;
	
	public AuthenticationController(IClientDetailsManager clientDetailsManager, IClientService clientService) {
		this.clientDetailsManager = clientDetailsManager;
		this.clientService = clientService;
	}

	@GetMapping("/authorize")
	public ResponseEntity<Object> authorize(@RequestParam(name="client_id") String clientId,
											@RequestParam(name="response_type") ResponseType responseType,
											@RequestParam(required = false) Scope scope,
											@RequestParam(required = false) String state,
											@RequestParam(required = false, name="redirect_uri") String redirectUri) throws InvalidRequestException, ClientNotFoundException {
		SecurityClient securityClient = clientDetailsManager.getClient(clientId);
		if (!isValidAuthorizationRequest(securityClient, responseType, scope, redirectUri)) {
			throw new InvalidRequestException();
		}
		
		if (redirectUri == null) {
			redirectUri = securityClient.getRedirectUri();
		}
		
		String code = clientService.generateAuthorizationCode(clientId, redirectUri);
		redirectUri = addParameters(redirectUri, code, state);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Location", redirectUri);
			
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}

	public boolean isValidAuthorizationRequest(SecurityClient securityClient, ResponseType responseType, Scope scope, String redirectUri) {
		GrantType grantType = getGrantType(responseType);
		if (!securityClient.supportsGrantType(grantType) || 
		   (redirectUri != null && !securityClient.supportsRedirectUri(redirectUri)) ||
		   (scope != null && !securityClient.supportsScope(scope))) {
			return false;
		}
		
		return true;
	}
	
	public GrantType getGrantType(ResponseType responseType) {
		switch (responseType) {
			case CODE:
				return GrantType.AUTHORIZATION_CODE;
			default:
				return null;
		}
	}
	
	private String addParameters(String redirectUri, String code, String state) {
		if (state != null) {
			return redirectUri + "?code=" + code + "&state=" + state;
		}
		
		return redirectUri + "?code=" + code;
	}
	
	@PostMapping("/token")
	public ResponseEntity<TokenDTO> token(@RequestHeader("Authorization") AuthorizationDTO authorization,
										  @RequestParam(name = "grant_type") GrantType grantType,
										  @RequestParam(required=false) Scope scope,
										  @RequestParam(required = false, name = "redirect_uri") String redirectUri) throws ClientNotFoundException, InvalidRequestException {
		if (!isValidTokenRequest(authorization, grantType, scope, redirectUri) ||
			!clientService.isValidAuthorizationCode(authorization.getClientId(), authorization.getClientSecret(), redirectUri)) {
			throw new InvalidRequestException();
		}
		
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private boolean isValidTokenRequest(AuthorizationDTO authorization, GrantType grantType, Scope scope, String redirectUri) throws ClientNotFoundException {
		SecurityClient securityClient = clientDetailsManager.getClient(authorization.getClientId());
		if (!securityClient.supportsGrantType(grantType) || 
		   (scope != null && !securityClient.supportsScope(scope))) {
			return false;
		}
		
		if (redirectUri == null) {
			redirectUri = securityClient.getRedirectUri();
		}
		
		boolean isValidAuthorizationCode = clientService.isValidAuthorizationCode(authorization.getClientId(), authorization.getClientSecret(), redirectUri);
		if (!isValidAuthorizationCode) {
			return false;
		}
		
		return true;
	}
}
