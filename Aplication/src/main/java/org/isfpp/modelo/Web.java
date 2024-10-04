package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

public class Web {
	private HashMap<String, Equipment> hardware;
	private ArrayList<org.isfpp.modelo.Connection> connections;
	private HashMap<String, Location> locations;
	private String nombre;
	private final HashMap<Object, EquipmentType> equipmentTypes;
	private final HashMap<String, WireType> wireTypes;
	private final HashMap<String, PortType> portTypes;

	public Web(String nombre) {
		this.nombre = nombre;
		this.hardware = new HashMap<>();
		this.connections = new ArrayList<>();
		this.locations = new HashMap<>();
		this.wireTypes = new HashMap<>();
		this.portTypes = new HashMap<>();
		this.equipmentTypes = new HashMap<>();
	}

	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	public ArrayList<org.isfpp.modelo.Connection> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<org.isfpp.modelo.Connection> connections) {
		this.connections = connections;
	}

	public HashMap<String, Location> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Location addLocation(String code, String description) {
		if (locations.containsKey(code))
			throw new AlreadyExistException("la localización ya se encuentra");

		Location l = new Location(code, description);
		locations.put(code, l);
		return l;
	}

	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode()))
			throw new NotFoundException("Localización inválida");
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que dependen de esa ubicación: " + codes);
		locations.remove(l.getCode());
	}

	public PortType addPort(String code, String description, int speed) {
		if (portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p = new PortType(code, description, speed);
		portTypes.put(code, p);
		return p;
	}

	public WireType addWire(String code, String description, int speed) {
		if (wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w = new WireType(code, description, speed);
		wireTypes.put(code, w);
		return w;
	}

	public EquipmentType addEquipmentType(String code, String description) {
		if (equipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType e = new EquipmentType(code, description);
		equipmentTypes.put(code, e);
		return e;
	}

	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType, int cantidad,
								  EquipmentType equipmentType, Location location, Boolean status) {
		if (hardware.containsKey(code))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if (!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if (!equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		Equipment e = new Equipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
		hardware.put(code, e);
		return e;
	}

	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode()))
			throw new NotFoundException("equipo inválido");
		hardware.remove(e.getCode());
	}

	public void eraseWire(WireType w) {
		if (!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<>();
		for (org.isfpp.modelo.Connection connection : connections) {
			if (connection.getWire().equals(w))
				codes.add(connection.getEquipment1().getCode() + "<->" + connection.getEquipment2().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Las siguientes conexiones tienen ese tipo de cable: " + codes);
		wireTypes.remove(w.getCode());
	}

	public void erasePort(PortType portType) {
		if (!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getAllPortsTypes().contains(portType))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
		portTypes.remove(portType.getCode());
	}

	@Override
	public String toString() {
		return "Web{" +
				"hardware=" + hardware +
				", connections=" + connections +
				", locations=" + locations +
				", nombre='" + nombre + '\'' +
				", equipmentTypes=" + equipmentTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}
}
