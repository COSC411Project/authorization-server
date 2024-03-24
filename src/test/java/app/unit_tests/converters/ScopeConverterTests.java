package app.unit_tests.converters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import app.converters.ScopeConverter;
import app.enums.Scope;

public class ScopeConverterTests {

	private ScopeConverter converter = new ScopeConverter();
	
	@Test
	public void convert_success() {
		// Arrange
		List<String> scopes = Stream.of(Scope.values())
									.map(scope -> scope.toString())
									.toList();
		List<Scope> expected = Stream.of(Scope.values()).toList();
		
		// Act
		List<Scope> actual = new ArrayList<>();
		for (String scope : scopes) {
			actual.add(converter.convert(scope));
		}
		
		// Assert
		assertAll(() -> {
			for (int i = 0; i < expected.size(); i++) {
				assertEquals(expected.get(i), actual.get(i));
			}
		});
	}
	
	@Test
	public void convert_invalidScope_exception() {
		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> {
			converter.convert("invalid scope");
		});
	}
}
