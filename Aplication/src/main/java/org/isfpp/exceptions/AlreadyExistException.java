package org.isfpp.exceptions;

public class AlreadyExistException extends RuntimeException{

	/**
	 * Número de serie para la serialización de la clase.
	 */
	private static final long serialVersionUID = -7985515243323385752L;

	/**
	 * Constructor de la excepción que acepta un mensaje.
	 * 
	 * @param message El mensaje de la excepción.
	 */
	public AlreadyExistException(String message) {
		super(message);
	}

}