package app.converters;

import java.util.Base64;

import org.springframework.core.convert.converter.Converter;

import app.dtos.AuthorizationDTO;

public class AuthorizationDTOConverter implements Converter<String, AuthorizationDTO> {

	@Override
	public AuthorizationDTO convert(String source) {
		byte[] decodedBytes = Base64.getDecoder().decode(source);
		String decodedString = new String(decodedBytes);
		
		String[] components = decodedString.split(":");
		if (components.length != 2) {
			throw new IllegalArgumentException();
		}
		
		return new AuthorizationDTO(components[0], components[1]);
	}

}
