package org.isfpp.modelo;
import org.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.isfpp.exceptions.AlredyExistException;
import org.isfpp.exceptions.NotFoundException;


public class Web {
	private HashMap<String, Equipment> hardware;
	private ArrayList<Connection> conections;
	private HashMap<String, Location> locations;
	private String nombre;

	public Web(String nombre) {
		super();
		this.nombre = nombre;
		this.hardware = new HashMap<String, Equipment>();
		this.conections = new ArrayList<Connection>();
		this.locations = new HashMap<String, Location>();
	}

	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	public ArrayList<Connection> getLinked() {
		return conections;
	}

	public void setLinked(ArrayList<Connection> conectiones) {
		this.conections = conectiones;
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

//	public Equipment addEquipment(String code, String description, String marca, String modelo, String ipAdress,
//			  Port port, EquipmetType equipmentType, Location location) {
//		if (hardware.containsKey(code))
//			throw new AlredyExistException("el quipo ya se encuentra");
//
//		//Equipment e = new Equipment(code, description, marca, modelo, port, equipmentType, location);
//		//hardware.put(code, e);
//		//return e;
//	}

	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.remove(e.getCode(), e);
	}

}
