package org.isfpp.modelo;

import org.isfpp.logica.CalculoGraph;

import java.util.Objects;

/**
 * Clase que representa un Puerto.
 */
public class Port {
    private Boolean inUse;
    private PortType portType;
    private Equipment equipment;
    private final String MACAddress;

    /**
     * Constructor para la clase Port.
     * 
     * @param portType Tipo de puerto.
     * @param equipment Equipo asociado al puerto.
     */
    public Port(PortType portType, Equipment equipment) {
        super();
        setInUse(false);
        setPortType(portType);
        setEquipment(equipment);
        MACAddress = CalculoGraph.generarMAC();
    }

    /**
     * Obtiene el tipo de puerto.
     *
     * @return portType Tipo de puerto.
     */
    public PortType getPortType() {
        return portType;
    }

    /**
     * Establece el tipo de puerto.
     *
     * @param portType Tipo de puerto.
     */
    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    /**
     * Obtiene el equipo asociado al puerto.
     *
     * @return equipment Equipo asociado al puerto.
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Establece el equipo asociado al puerto.
     *
     * @param equipment Equipo a asociar.
     */
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    /**
     * Verifica si el puerto está en uso.
     *
     * @return inUse true si está en uso, false de lo contrario.
     */
    public Boolean isInUse(){
        return inUse;
    }

    /**
     * Establece el estado de uso del puerto.
     *
     * @param is Estado de uso del puerto.
     */
    public void setInUse(Boolean is){
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
     * Obtiene la dirección MAC asociada al puerto.
     *
     * @return MACAddress Dirección MAC.
     */
    public String getMACAddress(){
        return MACAddress;
    }
}