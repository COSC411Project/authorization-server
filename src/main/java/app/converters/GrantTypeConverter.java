package app.converters;

import org.springframework.core.convert.converter.Converter;

import app.enums.GrantType;

public class GrantTypeConverter implements Converter<String, GrantType> {

	@Override
	public GrantType convert(String source) {
		source = source.toLowerCase();
		for (GrantType grantType : GrantType.values()) {
			if (source.equals(grantType.toString().toLowerCase())) {
				return grantType;
			}
		}
		
		throw new IllegalArgumentException();
	}

}
