package org.isfpp.modelo;

import java.util.Objects;

/**
 * Clase que representa un tipo de equipo.
 */
public class EquipmentType {

    /**
     * Código del tipo de equipo.
     */
    private String code;

    /**
     * Descripción del tipo de equipo.
     */
    private String description;

    /**
     * Constructor por defecto.
     */
    public EquipmentType() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param code        Código del tipo de equipo.
     * @param description Descripción del tipo de equipo.
     */
    public EquipmentType(String code, String description) {
        super();
        setCode(code);
        setDescription(description);
    }

    /**
     * Obtiene el código del tipo de equipo.
     *
     * @return el código del tipo de equipo.
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código del tipo de equipo.
     *
     * @param code el nuevo código del tipo de equipo.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene la descripción del tipo de equipo.
     *
     * @return la descripción del tipo de equipo.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del tipo de equipo.
     *
     * @param description la nueva descripción del tipo de equipo.
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
        if (!(obj instanceof EquipmentType other))
            return false;
        return Objects.equals(code, other.code);
    }

    @Override
    public String toString() {
        return "Codigo: '" + code + '\'' + " " +
                "descripcion: '" + description + "'";
    }
}