package app.services;

import app.dtos.UserDTO;

public interface IUserService {

	UserDTO getUserInfo(int userId);
	
}
