package org.cz.home.persistence;

public class PersistenceRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -6306766770002844755L;

	public PersistenceRuntimeException(String message, String problem ) {
		super(message + " - " + problem);
	}

	public PersistenceRuntimeException(String message) {
		super(message);
	}
}
