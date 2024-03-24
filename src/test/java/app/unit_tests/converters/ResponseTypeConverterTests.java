package app.unit_tests.converters;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import app.converters.ResponseTypeConverter;
import app.enums.ResponseType;

public class ResponseTypeConverterTests {

	private ResponseTypeConverter converter = new ResponseTypeConverter();
	
	@Test
	public void convert_success() {
		// Arrange
		List<String> responseTypes = Stream.of(ResponseType.values())
										   .map(responseType -> responseType.toString())
										   .toList();
		List<ResponseType> expected = Stream.of(ResponseType.values()).toList();
		
		// Act
		List<ResponseType> actual = new ArrayList<>();
		for (String responseType : responseTypes) {
			actual.add(converter.convert(responseType));
		}
		
		// Assert
		assertAll(() -> {
			for (int i = 0; i < expected.size(); i++) {
				assertEquals(expected.get(i), actual.get(i));
			}
		});
	}
	
	@Test
	public void convert_invalidResponseType_exception() {
		// Act
		assertThrows(IllegalArgumentException.class, () -> {
			converter.convert("invalid response type");
		});
	}
}
