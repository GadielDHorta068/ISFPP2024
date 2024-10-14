package org.isfpp.modelo;

import java.util.Objects;

/**
 * Representa una conexión entre dos puertos utilizando un tipo de cable.
 */
public class Connection {
    private WireType wire;
    private Port port1;
    private Port port2;

    /**
     * Constructor por defecto.
     */
    public Connection() {
    }

    /**
     * Constructor que inicializa una conexión con dos puertos y un tipo de cable.
     *
     * @param port1 El primer puerto.
     * @param port2 El segundo puerto.
     * @param wire  El tipo de cable.
     * @throws IllegalArgumentException Si cualquier puerto ya está en uso o ambos puertos
     *                                  pertenecen al mismo equipo.
     */
    public Connection(Port port1, Port port2, WireType wire) {
        super();

        // Verificamos que ambos puertos no estén en uso
        if (port1.isInUse()) {
            throw new IllegalArgumentException("El puerto 1 ya está en uso");
        }
        if (port2.isInUse()) {
            throw new IllegalArgumentException("El puerto 2 ya está en uso");
        }

        // Verificamos que los puertos no pertenezcan al mismo equipo
        if (port1.getEquipment().equals(port2.getEquipment())) {
            throw new IllegalArgumentException("Ambos puertos pertenecen al mismo equipo");
        }

        // Configuramos los puertos y marcamos como en uso
        setPort1(port1);
        port1.setInUse(true);
        setPort2(port2);
        port2.setInUse(true);
        setWire(wire);
    }

    /**
     * Obtiene el tipo de cable de la conexión.
     *
     * @return El tipo de cable.
     */
    public WireType getWire() {
        return wire;
    }

    /**
     * Establece el tipo de cable de la conexión.
     *
     * @param wire El tipo de cable.
     */
    public void setWire(WireType wire) {
        this.wire = wire;
    }

    /**
     * Obtiene el primer puerto de la conexión.
     *
     * @return El primer puerto.
     */
    public Port getPort1() {
        return port1;
    }

    /**
     * Establece el primer puerto de la conexión.
     *
     * @param port1 El primer puerto.
     */
    public void setPort1(Port port1) {
        this.port1 = port1;
    }

    /**
     * Obtiene el segundo puerto de la conexión.
     *
     * @return El segundo puerto.
     */
    public Port getPort2() {
        return port2;
    }

    /**
     * Establece el segundo puerto de la conexión.
     *
     * @param port2 El segundo puerto.
     */
    public void setPort2(Port port2) {
        this.port2 = port2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(port1, port2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Connection other))
            return false;
        return (port1.equals(other.port1) && port2.equals(other.port2))
                || (port1.equals(other.port2) && port2.equals(port1));
    }

    @Override
    public String toString() {
        return wire + "\n" + port1.toString() + "\n" + port2.toString();
    }
}