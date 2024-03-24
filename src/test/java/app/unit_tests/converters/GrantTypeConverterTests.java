package app.unit_tests.converters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import app.converters.GrantTypeConverter;
import app.enums.GrantType;

public class GrantTypeConverterTests {

	private GrantTypeConverter converter = new GrantTypeConverter();
	
	@Test
	public void convert_success() {
		// Arrange
		List<String> grantTypes = Stream.of(GrantType.values())
										.map(grantType -> grantType.toString())
										.toList();
		List<GrantType> expected = Stream.of(GrantType.values()).toList();
		
		// Act
		List<GrantType> actual = new ArrayList<>();
		for (String grantType : grantTypes) {
			actual.add(converter.convert(grantType));
		}
		
		// Assert
		assertAll(() -> {
			for (int i = 0; i < expected.size(); i++) {
				assertEquals(expected.get(i), actual.get(i));
			}
		});
	}
	
	@Test
	public void convert_invalidGrantType_exception() {
		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> {
			converter.convert("invalid grant type");
		});
	}
}
