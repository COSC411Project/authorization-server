package app.security.user;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import app.entities.User;
import app.exceptions.EmailNotFoundException;
import app.repositories.IUserRepository;

public class CustomUserDetailsManager implements UserDetailsManager {
	
	private IUserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public CustomUserDetailsManager(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		throw new UsernameNotFoundException(username);
	}
	
	public UserDetails loadUserByEmail(String email) throws EmailNotFoundException {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new EmailNotFoundException();
		}
		
		return new SecurityUser(user.get());
	}

	@Override
	public void createUser(UserDetails user) {
		SecurityUser securityUser = (SecurityUser) user;
		userRepository.save(securityUser.getUser());
	}

	@Override
	public void updateUser(UserDetails user) {
		SecurityUser securityUser = (SecurityUser) user;
		userRepository.save(securityUser.getUser());
	}

	@Override
	public void deleteUser(String email) {
		userRepository.deleteByEmail(email);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		SecurityContext context = SecurityContextHolder.getContext();
		SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
		if (!passwordEncoder.matches(oldPassword, securityUser.getPassword())) {
			throw new IllegalArgumentException();
		}
		
		User user = securityUser.getUser();
		user.setPassword(newPassword);
		userRepository.save(user);
	}

	@Override
	public boolean userExists(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

}
