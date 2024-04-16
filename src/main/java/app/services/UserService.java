package app.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import app.dtos.UserDTO;
import app.entities.User;
import app.repositories.IUserRepository;
import app.utils.Mapper;

@Service
public class UserService implements IUserService {
	
	private final IUserRepository userRepository;
	
	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	@Override
	public UserDTO getUserInfo(int userId) {
		Optional<User> savedUser = userRepository.findById(userId);
		if (!savedUser.isPresent()) {
			return null;
		}
		
		return Mapper.map(savedUser.get());
	}

}
