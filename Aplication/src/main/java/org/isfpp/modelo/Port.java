package org.isfpp.modelo;

import org.isfpp.logica.Utils;

import java.util.Objects;

public class Port {
    private Boolean inUse;
    private PortType portType;
    private Equipment equipment;
    private String MACAdress;

    public Port(PortType portType, Equipment equipment) {
        super();
        setInUse(false);
        setPortType(portType);
        setEquipment(equipment);
        MACAdress = Utils.generarMAC();
    }

    public PortType getPortType() {
        return portType;
    }

    public void setPortType(PortType portType) {
        this.portType = portType;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Boolean isInUse(){
        return inUse;
    }

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
}
