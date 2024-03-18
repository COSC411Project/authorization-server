package app.converters;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import app.enums.ResponseType;

@Component
public class ResponseTypeConverter implements Converter<String, ResponseType> {

	@Override
	public ResponseType convert(String source) {
		source = source.toLowerCase();
		
		for (ResponseType type : ResponseType.values()) {
			if (type.toString().toLowerCase().equals(source)) {
				return type;
			}
		}
		
		throw new IllegalArgumentException();
	}

}
