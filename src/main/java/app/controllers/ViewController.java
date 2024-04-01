package app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping({"/dev/clients/register", "/dev/clients", "/dev/clients/*", "/dev/users"})
	public String clients() {
		return "/dev/index.html";
	}
	
	@GetMapping("/login**")
	public String login() {
		return "/login/index.html";
	}
	
}
