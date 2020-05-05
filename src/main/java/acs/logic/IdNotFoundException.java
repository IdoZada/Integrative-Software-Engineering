package acs.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1632234012428544886L;

	public IdNotFoundException() {
		super();
	}

	public IdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public IdNotFoundException(String message) {
		super(message);
	}

	public IdNotFoundException(Throwable cause) {
		super(cause);
	}
	

}
