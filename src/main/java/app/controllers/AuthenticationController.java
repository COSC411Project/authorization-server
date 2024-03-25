package app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.TokenDTO;
import app.enums.GrantType;
import app.enums.ResponseType;
import app.enums.Scope;
import app.exceptions.ClientNotFoundException;
import app.exceptions.InvalidRequestException;
import app.exceptions.UnauthorizedException;
import app.models.Authorization;
import app.security.client.IClientDetailsManager;
import app.security.client.SecurityClient;
import app.services.IClientService;
import app.utils.AuthorizationUtil;

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
	public ResponseEntity<TokenDTO> token(@RequestHeader("Authorization") String authorizationHeader,
										  @RequestParam(name = "grant_type") GrantType grantType,
										  @RequestParam String code,
										  @RequestParam(required=false) Scope scope,
										  @RequestParam(required = false, name = "redirect_uri") String redirectUri,
										  Authentication authentication) throws ClientNotFoundException, InvalidRequestException, UnauthorizedException {
		if (!isValidTokenRequest(authorizationHeader, grantType, code, scope, redirectUri)) {
			throw new InvalidRequestException();
		}
		
		int userId = (int) authentication.getPrincipal();
		
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public boolean isValidTokenRequest(String authorizationHeader, 
										GrantType grantType, 
										String code, 
										Scope scope, 
										String redirectUri) throws ClientNotFoundException, UnauthorizedException {
		Authorization authorization = AuthorizationUtil.parseHeader(authorizationHeader);
		
		SecurityClient securityClient = clientDetailsManager.getClient(authorization.getClientId());
		if (!securityClient.supportsGrantType(grantType) || 
			(grantType == GrantType.AUTHORIZATION_CODE && (code == null || code.isBlank())) ||
		    (scope != null && !securityClient.supportsScope(scope))) {
			return false;
		}
		
		if (code == null) return true;
		
		if (redirectUri == null) {
			redirectUri = securityClient.getRedirectUri();
		}
		
		boolean isValidAuthorizationCode = clientService.isValidAuthorizationCode(code, authorization.getClientId(), authorization.getClientSecret(), redirectUri);
		if (!isValidAuthorizationCode) {
			return false;
		}
		
		return true;
	}
	
}
