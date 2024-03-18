package app.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import app.enums.Scope;

@Component
public class ScopeConverter implements Converter<String, Scope> {

	@Override
	public Scope convert(String source) {
		source = source.toLowerCase();
		
		for (Scope scope : Scope.values()) {
			if (scope.toString().toLowerCase().equals(source)) {
				return scope;
			}
		}
		
		throw new IllegalArgumentException();
	}

}
