package org.isfpp.modelo;

import java.util.Objects;

/**
 * La clase WireType representa un tipo de cable con un código, descripción y velocidad.
 */
public class WireType {
    private String code;
    private String description;
    private int speed;

    public WireType() {
    }

    /**
     * Constructor para la clase WireType.
     *
     * @param code        El código único del tipo de cable.
     * @param description Una breve descripción del tipo de cable.
     * @param speed       La velocidad del tipo de cable medida en unidades adecuadas.
     */
    public WireType(String code, String description, int speed) {
        super();
        setCode(code);
        setDescription(description);
        setSpeed(speed);
    }

    /**
     * Devuelve el código único del tipo de cable.
     *
     * @return el código del tipo de cable
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código único del tipo de cable.
     *
     * @param code el código del tipo de cable
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Devuelve la descripción del tipo de cable.
     *
     * @return la descripción del tipo de cable
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del tipo de cable.
     *
     * @param description la descripción del tipo de cable
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve la velocidad del tipo de cable.
     *
     * @return la velocidad del tipo de cable
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Establece la velocidad del tipo de cable.
     *
     * @param speed la velocidad del tipo de cable
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Devuelve el valor del código hash para este objeto.
     *
     * @return el valor del código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    /**
     * Compara este objeto con el objeto especificado.
     *
     * @param obj el objeto a comparar
     * @return true si los objetos son iguales, de lo contrario false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof WireType other))
            return false;
        return Objects.equals(code, other.code);
    }

    /**
     * Devuelve la representación en cadena de este objeto.
     *
     * @return la representación en cadena del objeto
     */
    @Override
    public String toString() {
        return code + description + speed;
    }
}