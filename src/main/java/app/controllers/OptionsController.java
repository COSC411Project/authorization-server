package app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.enums.GrantType;
import app.enums.Role;
import app.enums.Scope;

@RestController
@RequestMapping("/api/options")
public class OptionsController {

	@GetMapping("/grant-types")
	public GrantType[] grantTypes() {
		return GrantType.values();
	}
	
	@GetMapping("/scopes")
	public Scope[] scopes() {
		return Scope.values();
	}
	
	@GetMapping("/roles")
	public Role[] roles() {
		return Role.values();
	}
	
}
