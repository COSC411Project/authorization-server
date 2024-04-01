package app.exceptions;

public class ApplicationNameTakenException extends Exception {

	public ApplicationNameTakenException(String applicationName) {
		super(applicationName + " already taken.");
	}
	
}
