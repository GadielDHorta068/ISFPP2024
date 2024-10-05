package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

public class Web {
	private HashMap<String, Equipment> hardware;
	private ArrayList<org.isfpp.modelo.Connection> conections;
	private HashMap<String, Location> locations;
	private String name;
	private HashMap<String, EquipmentType> equipmentTypes;
	private HashMap<String,WireType> wireTypes;
	private HashMap<String,PortType> portTypes;

	public Web(String name) {
		super();
		this.name = name;
		this.locations 		= new HashMap<String, Location>();
		this.wireTypes  	= new HashMap<String,WireType>();
		this.portTypes      = new HashMap<String,PortType>();
		this.equipmentTypes = new HashMap<String, EquipmentType>();
		this.hardware 		= new HashMap<String, Equipment>();
		this.conections 	= new ArrayList<org.isfpp.modelo.Connection>();
	}

	//name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	//equimpents
	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType,int cantidad,
								  EquipmentType equipmentType, Location location,Boolean status)  {
		if (hardware.containsKey(code))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if(!portTypes.containsKey(portType))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if(equipmentTypes.containsKey(equipmentType))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		Equipment newEquipment = new Equipment(code, description, marca, model,
				portType,cantidad, equipmentType, location,status);
		hardware.put(code, newEquipment);
		return newEquipment;
	}

	public void eraseEquipment(Equipment equipment) {
		if (!hardware.containsKey(equipment.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.remove(equipment.getCode(), equipment);
	}

	//conections
	public ArrayList<org.isfpp.modelo.Connection> getConections() {
		return conections;
	}

	public void setConections(ArrayList<org.isfpp.modelo.Connection> conections) {
		this.conections = conections;
	}

	public org.isfpp.modelo.Connection addConection(WireType wire,Port port1, Port port2){
		if (hardware.containsKey(port1.getEquipment().getCode()) || hardware.containsKey(port2.getEquipment().getCode()))
			throw new NotFoundException("los equipos pedidos no se encuentran dentro de la red");

		Connection newConnection = new Connection(wire, port1, port2);
		if (getConections().contains(newConnection))
			throw new AlreadyExistException("la conexion ya se encuentra");

		getConections().add(newConnection);
		return newConnection;
	}

	public org.isfpp.modelo.Connection eraseConection(org.isfpp.modelo.Connection connection){
		if (!getConections().contains(connection))
			throw new NotFoundException("conexion invalida");
		getConections().remove(connection);
		return connection;
	}

	//locations
	public HashMap<String, Location> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}

	public Location addLocation(String code, String description) {
		if (locations.containsKey(code))
			throw new AlreadyExistException("la localizacion ya se encuentra");

		Location l = new Location(code, description);
		locations.put(code, l);
		return l;
	}

	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode()))
			throw new NotFoundException("Localizacion invalida");
		List<String> codes = new ArrayList<String>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que dependen de esa ubicación: " + codes);
		locations.remove(l.getCode(), l);

	}

	//equipmentType
	public HashMap<String, EquipmentType> getEquipmentTypes(){
		return this.equipmentTypes;
	}

	public EquipmentType addEquipmentType(String code,String description){
		if(equipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType newEquipmentType = new EquipmentType(code,description);
		equipmentTypes.put(code,newEquipmentType);
		return newEquipmentType;
	}

	public void eraseEquipmentType(EquipmentType equipmentType){
		if (getEquipmentTypes().containsKey(equipmentType.getCode()))
			throw new NotFoundException("elemento no encontrado");
		List<String> codes = new ArrayList<>();
		for (Equipment equipment : hardware.values()) {
			if (equipment.getEquipmentType().equals(equipmentType))
				codes.add(equipment.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
		locations.remove(equipmentType.getCode(),equipmentType);
	}


	//portType
	public HashMap<String,PortType> getPortTypes(){
		return this.portTypes;
	}

	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p = new PortType(code,description,speed);
		portTypes.put(code,p);
		return p;
	}

	public void erasePort(PortType portType){
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Equipment equipment : hardware.values()) {
			if (equipment.getAllPortsTypes().containsKey(portType))
				codes.add(equipment.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
		locations.remove(portType.getCode(),portType);
	}

	//wireType
	public HashMap<String, WireType> getWireTypes(){
		return this.wireTypes;
	}

	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		return w;
	}

	public void eraseWire(WireType wireType){
		if(!wireTypes.containsKey(wireType.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<String>();
		for (org.isfpp.modelo.Connection connection : getConections()) {
			if(connection.getWire().equals(wireType))
				codes.add(connection.getPort1().getEquipment().getCode()+"<->"+connection.getPort2().getEquipment().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(wireType.getCode(),wireType);
	}

	@Override
	public String toString() {
		return "Web{" +
				"hardware=" + hardware +
				", conections=" + conections +
				", locations=" + locations +
				", nombre='" + name + '\'' +
				", equipmentTypes=" + equipmentTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}
}