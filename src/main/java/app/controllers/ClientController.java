package app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.services.IClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private IClientService clientService;
	
	public ClientController(IClientService clientService) {
		this.clientService = clientService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<ClientDTO> register(@RequestBody ClientRegistrationDTO clientRegistrationDTO) {
		ClientDTO registeredClient = clientService.register(clientRegistrationDTO);
		return new ResponseEntity<>(registeredClient, HttpStatus.CREATED);
	}
}
