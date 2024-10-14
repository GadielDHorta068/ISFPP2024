package org.isfpp.modelo;

import java.util.Objects;

/**
 * La clase PortType representa un tipo de puerto con un código, descripción y velocidad.
 */
public class PortType {
    private String code;
    private String description;
    private int speed;

    /**
     * Constructor vacío de PortType.
     */
    public PortType() {
    }

    /**
     * Constructor con parámetros de PortType.
     *
     * @param code        El código del puerto.
     * @param description La descripción del puerto.
     * @param speed       La velocidad del puerto.
     */
    public PortType(String code, String description, int speed) {
        super();
        setCode(code);
        setDescription(description);
        setSpeed(speed);
    }

    /**
     * Obtiene el código del puerto.
     *
     * @return El código del puerto.
     */
    public String getCode() {
        return code;
    }

    /**
     * Establece el código del puerto.
     *
     * @param code El código del puerto.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Obtiene la descripción del puerto.
     *
     * @return La descripción del puerto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción del puerto.
     *
     * @param description La descripción del puerto.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la velocidad del puerto.
     *
     * @return La velocidad del puerto.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Establece la velocidad del puerto.
     *
     * @param speed La velocidad del puerto.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Genera un código hash para el objeto PortType basado en el código del puerto.
     *
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    /**
     * Compara este objeto con otro para determinar si son iguales.
     *
     * @param obj El objeto a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PortType other))
            return false;
        return Objects.equals(code, other.code);
    }

    /**
     * Devuelve una representación en cadena del objeto PortType.
     *
     * @return Una cadena que describe el objeto PortType.
     */
    @Override
    public String toString() {
        return code + " " + description + " " + speed;
    }
}