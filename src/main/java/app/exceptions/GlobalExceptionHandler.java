package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import app.dtos.ErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<Object> handleInvalidRequest() {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<ErrorDTO> handleClassNotFoundException() {
		return new ResponseEntity<>(new ErrorDTO("invalid_request"),HttpStatus.BAD_REQUEST);
	}
}
