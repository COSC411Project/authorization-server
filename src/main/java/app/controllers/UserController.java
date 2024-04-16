package app.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.UserDTO;
import app.exceptions.UnauthorizedException;
import app.services.IUserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Value("${rsa.public-key.path}")
	private String publicKeyPath;
	
	private final IUserService userService;
	
	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) throws UnauthorizedException {
		int userId = (int) authentication.getPrincipal();
		UserDTO userInfo = userService.getUserInfo(userId);
		
		return new ResponseEntity<>(userInfo, HttpStatus.OK);
	}

}
