package app.utils;

import java.util.HashSet;

import app.dtos.ClientDTO;
import app.dtos.ClientRegistrationDTO;
import app.dtos.UserDTO;
import app.entities.Client;
import app.entities.User;

public class Mapper {
	
	public static Client map(ClientRegistrationDTO clientRegistration) {
		return new Client(clientRegistration.getApplicationName(),
						  null, 
						  null, 
					      clientRegistration.isScopeRequired(), 
						  new HashSet<>(clientRegistration.getGrantTypes()),
						  new HashSet<>(clientRegistration.getScopes()),
						  new HashSet<>(clientRegistration.getRedirectUris()));
	}

	public static ClientDTO map(Client client, String originalSecret) {
		return new ClientDTO(client.getId(),
							 client.getApplicationName(),
							 client.getIdentifier(),
							 originalSecret,
							 client.requiresConsent(),
							 client.getScopes().stream().toList(),
							 client.getRedirectUris().stream().toList(),
							 client.getGrantTypes().stream().toList());
	}
	
	public static ClientDTO map(Client client) {
		return new ClientDTO(client.getId(),
							 client.getApplicationName(),
							 client.getIdentifier(),
							 null,
							 client.requiresConsent(),
							 client.getScopes().stream().toList(),
							 client.getRedirectUris().stream().toList(),
							 client.getGrantTypes().stream().toList());
	}
	
	public static UserDTO map(User user) {
		return new UserDTO(user.getEmail(),
						   user.getPhone(),
						   user.getCity(),
						   user.getState());
	}
	
}
