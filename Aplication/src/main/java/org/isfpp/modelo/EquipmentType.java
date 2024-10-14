package org.isfpp.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de equipo.
 */
public class EquipmentType {
	private String code;
	private String description;

	/**
	 * Constructor por defecto.
	 */
	public EquipmentType(){}

	/**
	 * Constructor con parámetros.
	 * @param code El código del tipo de equipo.
	 * @param description La descripción del tipo de equipo.
	 */
	public EquipmentType(String code, String description) {
		super();
		setCode(code);
		setDescription(description);
	}
	
	/**
	 * Obtiene el código del tipo de equipo.
	 * @return El código del tipo de equipo.
	 */
	public String getCode() {return code;}
	
	/**
	 * Establece el código del tipo de equipo.
	 * @param code El código del tipo de equipo.
	 */
	public void setCode(String code) {this.code = code;}
	
	/**
	 * Obtiene la descripción del tipo de equipo.
	 * @return La descripción del tipo de equipo.
	 */
	public String getDescription() {return description;}
	
	/**
	 * Establece la descripción del tipo de equipo.
	 * @param description La descripción del tipo de equipo.
	 */
	public void setDescription(String description) {this.description = description;}

	@Override
	/**
	 * Calcula el código hash basado en el código del equipo.
	 * @return El código hash.
	 */
	public int hashCode() {return Objects.hash(code);}

	@Override
	/**
	 * Compara este objeto con otro para determinar la igualdad.
	 * @param obj El objeto con el que se compara.
	 * @return true si ambos objetos son iguales, false en caso contrario.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EquipmentType other))
			return false;
        return Objects.equals(code, other.code);
	}

	@Override
	/**
	 * Retorna una representación en cadena del objeto.
	 * @return Una cadena que representa el objeto.
	 */
    public String toString() {
        return  "Codigo: '" + code + '\'' + " " +
                "descripcion: '" + description + "'";
    }
}