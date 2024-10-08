package org.isfpp.modelo;

import java.util.Objects;

public class Connection {
    private WireType wire;
    private Port port1;
    private Port port2;

	public Connection(){}
	public Connection(Port port1, Port port2,WireType wire) {
		super();
		if (port1.getEquipment().equals(port2.getEquipment()))
			throw new IllegalArgumentException("ambos son iguales");
		if(port2.getEquipment().equals(port1.getEquipment()))
			throw new IllegalArgumentException("ambos puertos pertenecen al mismo equipo");
		if (port1.isInUse() || port2.isInUse())
			throw new IllegalArgumentException("uno o ambos puertos se encuentran en uso");

		setPort1(port1);
		port1.setInUse(true);
		setPort2(port2);
		port2.setInUse(true);
		setWire(wire);
	}


	public WireType getWire() {return wire;}

	public void setWire(WireType wire) {this.wire = wire;}

	public Port getPort1() {return port1;}
	
	public void setPort1(Port port1) {this.port1 = port1;}
	
	public Port getPort2() {return port2;}
	
	public void setPort2(Port port2) {this.port2 = port2;}

	@Override
	public int hashCode() {
		return Objects.hash(port1, port2);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Connection))
			return false;
		Connection other = (Connection) obj;
		if ((port1.equals(other.port1) && port2.equals(other.port2))
		|| (port1.equals(other.port2) && port2.equals(port1)))
			return true;
		return false;
	}

	@Override
	public String toString() {
		return STR."""
\{wire}
\{port1.toString()}
\{port2.toString()}""";
	}
}
