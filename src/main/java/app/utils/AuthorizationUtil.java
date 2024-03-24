package app.utils;

import java.util.Base64;

import app.exceptions.UnauthorizedException;
import app.models.Authorization;

public class AuthorizationUtil {

	public static Authorization parseHeader(String header) throws UnauthorizedException {
		String[] components = header.split(" ");
		if (!components[0].toLowerCase().equals("basic")) {
			throw new UnauthorizedException();
		}
		
		byte[] decodedBytes = Base64.getDecoder().decode(components[1]);
		String decodedString = new String(decodedBytes);
		
		String[] clientCredentials = decodedString.split(":");
		if (components.length != 2) {
			throw new UnauthorizedException();
		}
		
		return new Authorization(clientCredentials[0], clientCredentials[1]);
	}
}
