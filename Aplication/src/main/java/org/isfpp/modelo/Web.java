package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;

import org.isfpp.exceptions.AlredyExistException;

public class Web {
	private HashMap<String,Equipment> hardware;
	private ArrayList<Conection> linked;
	private HashMap<String,Location> locations;
	private String nombre;
	public Web(String nombre) {
		super();
		this.nombre = nombre;
		this.hardware=new HashMap<String,Equipment>();
		this.linked=new ArrayList<Conection>();
		this.locations=new HashMap<String,Location>();
	}
	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}
	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}
	public ArrayList<Conection> getLinked() {
		return linked;
	}
	public void setLinked(ArrayList<Conection> linked) {
		this.linked = linked;
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
	public void addLocation(Location location) {
		if(locations.containsKey(location.getCode()))
			throw new AlredyExistException("la localizacion ya se encuentra");
		locations.put(location.getCode(), location);
	}
	public void addEquipment(Equipment equipment) {
		if(locations.containsKey(equipment.getCode())){
			throw new AlredyExistException("el quipo ya se encuentra");
		}
		hardware.put(equipment.getCode(),equipment);
	}
	
	

}
