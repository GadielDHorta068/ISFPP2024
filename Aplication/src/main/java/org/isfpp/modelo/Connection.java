package org.isfpp.modelo;

import java.util.Objects;

public class Connection {
    private WireType wire;
    private Equipment equipment1;
    private Equipment equipment2;
    
	public Connection(WireType wire, Equipment equipment1, Equipment equipment2) {
		super();
		setEquipment1(equipment1);
		setEquipment2(equipment2);
		setWire(wire);
	}


	public WireType getWire() {return wire;}
	
	public void setWire(WireType wire) {this.wire = wire;}
	
	public Equipment getEquipment1() {return equipment1;}
	
	public void setEquipment1(Equipment equipment1) {this.equipment1 = equipment1;}
	
	public Equipment getEquipment2() {return equipment2;}
	
	public void setEquipment2(Equipment equipment2) {this.equipment2 = equipment2;}

	@Override
	public int hashCode() {
		return Objects.hash(equipment1, equipment2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;
		return Objects.equals(equipment1, other.equipment1) && Objects.equals(equipment2, other.equipment2);
	}

	@Override
	public String toString() {
		return "Conection [wire=" + wire + ", equipment1=" + equipment1 + ", equipment2=" + equipment2 + "]";
	}
}
