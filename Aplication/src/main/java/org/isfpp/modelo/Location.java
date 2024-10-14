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
    public Location() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param code        el código de la ubicación.
     * @param description la descripción de la ubicación.
     */
    public Location(String code, String description) {
        super();
        setCode(code);
        setDescription(description);
    }

    /**
     * Obtiene el código de la ubicación.
     *
     * @return el código.
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código de la ubicación.
     *
     * @param code el código a establecer.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene la descripción de la ubicación.
     *
     * @return la descripción.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la ubicación.
     *
     * @param description la descripción a establecer.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Calcula el código hash de la ubicación basado en el código.
     *
     * @return el código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    /**
     * Compara esta ubicación con otro objeto para verificar la igualdad.
     *
     * @param obj el objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
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

    /**
     * Retorna una representación en cadena de la ubicación.
     *
     * @return una cadena que representa la ubicación.
     */
    @Override
    public String toString() {
        return code + " \n" +
                description;
    }


}