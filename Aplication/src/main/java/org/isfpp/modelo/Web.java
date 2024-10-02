package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.isfpp.exceptions.AlredyExistException;
import org.isfpp.exceptions.NotFoundException;

public class Web {
	private HashMap<String, Equipment> hardware;
	private ArrayList<org.isfpp.modelo.Connection> conections;
	private HashMap<String, Location> locations;
	private String nombre;
	private HashMap<Object, EquipmentType> equipmetTypes;
	private HashMap<String,WireType>wireTypes;
	private HashMap<String,PortType>portTypes;

	public Web(String nombre) {
		super();
		this.nombre = nombre;
		this.hardware = new HashMap<String, Equipment>();
		this.conections = new ArrayList<org.isfpp.modelo.Connection>();
		this.locations = new HashMap<String, Location>();
		this.wireTypes=new HashMap<String,WireType>();
		this.portTypes=new HashMap<String,PortType>();
		this.equipmetTypes= new HashMap<Object, EquipmentType>();
	}

	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	public ArrayList<org.isfpp.modelo.Connection> getConections() {
		return conections;
	}

	public void setConections(ArrayList<org.isfpp.modelo.Connection> conections) {
		this.conections = conections;
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
			throw new AlredyExistException("la localizacion ya se encuentra");

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

	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlredyExistException("el tipo de puerto ya se encuentra");
		PortType p=new PortType(code,description,speed);
		portTypes.put(code,p);
		return p;
	}
	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlredyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		return w;
	}
	public EquipmentType addEquipment(String code,String description){
		if(equipmetTypes.containsKey(code))
			throw new AlredyExistException("el tipo de equipo ya existe");
		EquipmentType e=new EquipmentType(code,description);
		equipmetTypes.put(code,e);
		return e;
	}
	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType,int cantidad,
								  EquipmetType equipmentType, Location location,Boolean status)  {
		if (hardware.containsKey(code))
			throw new AlredyExistException("el equipo ya se encuentra");
		if(!portTypes.containsKey(portType))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if(equipmetTypes.containsKey(equipmentType))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		Equipment e = new Equipment(code, description, marca, model, portType,cantidad, equipmentType, location,status);
		hardware.put(code, e);
		return e;
	}

	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.remove(e.getCode(), e);
	}
    public void eraseWire(WireType w){
		if(!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<String>();
		for (org.isfpp.modelo.Connection connection : conections) {
			if(connection.getWire().equals(w))
				codes.add(connection.getEquipment1().getCode()+"<->"+connection.getEquipment2().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(w.getCode(),w);

	}
	public void erasePort(PortType portType){
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra");
		List<String> codes = new ArrayList<String>();
		for (Equipment e : hardware.values()) {
			if (e.getAllPortsTypes().contains(portType))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
		locations.remove(portType.getCode(),portType);
	}
	@Override
	public String toString() {
		return "Web{" +
				"hardware=" + hardware +
				", conections=" + conections +
				", locations=" + locations +
				", nombre='" + nombre + '\'' +
				", equipmetTypes=" + equipmetTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}
}
