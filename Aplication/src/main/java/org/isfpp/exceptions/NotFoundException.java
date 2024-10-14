package org.isfpp.exceptions;

public class NotFoundException extends RuntimeException {

/**
 * Excepción que indica que un recurso no ha sido encontrado.
 * 
 * @param message El mensaje de detalle de la excepción.
 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(String message) {
		super(message);
	}
	

}
