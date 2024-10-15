package org.isfpp.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de puerto.
 */
public class PortType {
	private String code;
	private String description;
	private int speed;

	/**
     * Constructor por defecto.
     */
	public PortType(){}

	/**
     * Constructor con parámetros.
     * @param code Código del puerto.
     * @param description Descripción del puerto.
     * @param speed Velocidad del puerto.
     */
	public PortType(String code, String description, int speed) {
		super();
		setCode(code);
		setDescription(description);
		setSpeed(speed);
	}
	
	/**
     * Obtiene el código del puerto.
     * @return El código del puerto.
     */
	public String getCode() {return code;}
	
	/**
     * Establece el código del puerto.
     * @param code El código del puerto.
     */
	public void setCode(String code) {this.code = code;}
	
	/**
     * Obtiene la descripción del puerto.
     * @return La descripción del puerto.
     */
	public String getDescription() {return description;}
	
	/**
     * Establece la descripción del puerto.
     * @param description La descripción del puerto.
     */
	public void setDescription(String description) {this.description = description;}
	
	/**
     * Obtiene la velocidad del puerto.
     * @return La velocidad del puerto.
     */
	public int getSpeed() {return speed;}
	
	/**
     * Establece la velocidad del puerto.
     * @param speed La velocidad del puerto.
     */
	public void setSpeed(int speed) {this.speed = speed;}

	@Override
	public int hashCode() {return Objects.hash(code);}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PortType other))
			return false;
        return Objects.equals(code, other.code);
	}

	/**
     * Devuelve una representación en cadena del objeto.
     * @return Una cadena que representa el objeto.
     */
	@Override
	public String toString() {
		return code + " "+description + " "+speed;
	}
}