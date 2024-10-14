package org.isfpp.modelo;

import org.isfpp.logica.Utils;

import java.util.Objects;

/**
 * La clase {@code Port} representa un puerto en un equipo.
 */
public class Port {
    private Boolean inUse;
    private PortType portType;
    private Equipment equipment;
    private final String MACAddress;

    /**
     * Constructor para crear una instancia de {@code Port}.
     *
     * @param portType  Tipo de puerto
     * @param equipment Equipo asociado al puerto
     */
    public Port(PortType portType, Equipment equipment) {
        super();
        setInUse(false);
        setPortType(portType);
        setEquipment(equipment);
        MACAddress = Utils.generarMAC();
    }

    /**
     * Obtiene el tipo de puerto.
     *
     * @return el tipo de puerto
     */
    public PortType getPortType() {
        return portType;
    }

    /**
     * Establece el tipo de puerto.
     *
     * @param portType el nuevo tipo de puerto
     */
    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    /**
     * Obtiene el equipo asociado al puerto.
     *
     * @return el equipo asociado
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Establece el equipo asociado al puerto.
     *
     * @param equipment el nuevo equipo asociado
     */
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Verifica si el puerto está en uso.
     *
     * @return {@code true} si el puerto está en uso, {@code false} en caso contrario
     */
    public Boolean isInUse() {
        return inUse;
    }

    /**
     * Establece el estado de uso del puerto.
     *
     * @param is {@code true} si el puerto está en uso, {@code false} en caso contrario
     */
    public void setInUse(Boolean is) {
        this.inUse = is;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Port port = (Port) o;
        return Objects.equals(portType, port.portType) && Objects.equals(equipment, port.equipment);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(portType);
    }

    @Override
    public String toString() {
        return portType.toString();
    }

    /**
     * Obtiene la dirección MAC del puerto.
     *
     * @return la dirección MAC
     */
    public String getMACAddress() {
        return MACAddress;
    }
}