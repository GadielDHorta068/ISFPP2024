package org.isfpp.modelo;

import java.util.Objects;

public class WireType {
	private String code;
	private String description;
	private int speed;

	/**
	 * Constructor por defecto.
	 */
	public WireType(){}

	/**
	 * Constructor con parámetros.
	 * @param code El código del tipo de alambre.
	 * @param description La descripción del tipo de alambre.
	 * @param speed La velocidad del tipo de alambre.
	 */
	public WireType(String code, String description, int speed) {
		super();
		setCode(code);
		setDescription(description);
		setSpeed(speed);
	}

	/**
	 * Obtiene el código del tipo de alambre.
	 * @return El código del tipo de alambre.
	 */
	public String getCode() {return code;}

	/**
	 * Configura el código del tipo de alambre.
	 * @param code El código del tipo de alambre.
	 */
	public void setCode(String code) {this.code = code;}

	/**
	 * Obtiene la descripción del tipo de alambre.
	 * @return La descripción del tipo de alambre.
	 */
	public String getDescription() {return description;}

	/**
	 * Configura la descripción del tipo de alambre.
	 * @param description La descripción del tipo de alambre.
	 */
	public void setDescription(String description) {this.description = description;}

	/**
	 * Obtiene la velocidad del tipo de alambre.
	 * @return La velocidad del tipo de alambre.
	 */
	public int getSpeed() {return speed;}

	/**
	 * Configura la velocidad del tipo de alambre.
	 * @param speed La velocidad del tipo de alambre.
	 */
	public void setSpeed(int speed) {this.speed = speed;}

	@Override
	public int hashCode() {return Objects.hash(code);}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof WireType other))
			return false;
        return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return code +  description  + speed ;
	}
}