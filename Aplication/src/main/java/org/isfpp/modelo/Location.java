package org.isfpp.modelo;

import java.util.Objects;

/**
 * Clase que representa una ubicación con un código y una descripción.
 */
public class Location {
	private String code;
	private String description;

	/**
	 * Constructor por defecto.
	 */
	public Location(){}

	/**
	 * Constructor que inicializa el código y la descripción de la ubicación.
	 * @param code el código de la ubicación
	 * @param description la descripción de la ubicación
	 */
	public Location(String code, String description) {
		super();
		setCode(code);
		setDescription(description);
	}

	/**
	 * Obtiene el código de la ubicación.
	 * @return el código de la ubicación
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Establece el código de la ubicación.
	 * @param code el nuevo código de la ubicación
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Obtiene la descripción de la ubicación.
	 * @return la descripción de la ubicación
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Establece la descripción de la ubicación.
	 * @param description la nueva descripción de la ubicación
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return code + " \n" +
				 description;
	}
}