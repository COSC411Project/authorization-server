package app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.TokenDTO;

@RestController
@RequestMapping("/oauth2")
public class AuthenticationController {

	@GetMapping("/authorize")
	public ResponseEntity authorize() {
		
		return new ResponseEntity(HttpStatus.TEMPORARY_REDIRECT);
	}
	
	@PostMapping("/token")
	public ResponseEntity<TokenDTO> token() {
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
