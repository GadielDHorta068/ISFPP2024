package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.List;

public class Equipment{
	private String code;
	private String description;
	private String marca;
	private String modelo;
	private List<String> IpAdress;
	private List<Port> puertos;
	private EquipmetType EquipmentType;
	private Location location;
	
	
	
	
	
	public Equipment(String code, String description, String marca, String modelo, List<String> ipAdress,
			List<Port> puertos, EquipmetType equipmentType, Location location) {
		super();
		this.code = code;
		this.description = description;
		this.marca = marca;
		this.modelo = modelo;
		IpAdress = ipAdress;
		this.puertos =new ArrayList<Port>();
		EquipmentType = equipmentType;
		this.location = location;
	}

	public String getCode() {
		return null;
	}


	private class Port{
		private PortType portType;
		private int cantidad;
		
		
	}

}
