package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.isfpp.exceptions.AlredyExistException;
import org.isfpp.exceptions.NotFoundException;

public class Equipment {
	private String code;
	private String description;
	private String marca;
	private String modelo;
	private List<String> ipAdresses;
	private List<Port> ports;
	private EquipmetType equipmentType;
	private Location location;
	private boolean status;

	public Equipment(String code, String description, String marca, String modelo, PortType portType,int cantidad,
			EquipmetType equipmentType, Location location,boolean status) {
		super();
		this.code = code;
		this.description = description;
		this.marca = marca;
		this.modelo = modelo;
		this.equipmentType = equipmentType;
		this.location = location;
		this.status=status;

		this.ipAdresses = new ArrayList<String>();
		this.ports = new ArrayList<Port>();
		this.ports.add(new Port(portType,cantidad));

	}
	public static String generarMAC() {
		Random random = new Random();
		byte[] macAddr = new byte[6];
		random.nextBytes(macAddr);

		StringBuilder macAddress = new StringBuilder(18);
		for (byte b : macAddr) {
			if (!macAddress.isEmpty()) {
				macAddress.append(":");
			}
			macAddress.append(String.format("%02x", b));
		}
		return macAddress.toString().toUpperCase();
	}


	public void addIp(String ip) {
		// llamo al metodo check de ip
		if (ipAdresses.contains(ip))
			throw new AlredyExistException("la ip ya se encuentra");
		ipAdresses.add(ip);

	}

	public void deleteIp(String ip) {
		if (!ipAdresses.contains(ip))
			throw new NotFoundException("la ip no se encuentra");
		ipAdresses.remove(ip);

	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}

	public Port addPort(PortType portType, int cantidad) {
		Port p = new Port(portType, cantidad);
		if (ports.contains(p))
			throw new AlredyExistException("ese puerto ya esta en la lista de puertos del equipo");
		ports.add(p);
		return p;

	}

	public void deletePort(Port port) {
		if (!ports.contains(port))
			throw new AlredyExistException("ese puerto ya esta en la lista de puertos del equipo");
		ports.remove(port);

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public List<String> getIpAdresses() {
		return ipAdresses;
	}

	public void setIpAdresses(List<String> ipAdresses) {
		this.ipAdresses = ipAdresses;
	}

	public List<Port> getPorts() {
		return ports;
	}

	public void setPorts(List<Port> ports) {
		this.ports = ports;
	}

	public EquipmetType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmetType equipmentType) {
		equipmentType = equipmentType;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipment other = (Equipment) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return "Equipment [code=" + code + ", description=" + description + ", marca=" + marca + ", modelo=" + modelo
				+ ", ipAdresses=" + ipAdresses + ", ports=" + ports + ", location=" + location + "]";
	}

	public static class Port {
		private PortType portType;
		private int cantidad;

		public Port(PortType portType, int cantidad) {
			super();
			this.portType = portType;
			if (cantidad < 0)
				throw new IllegalArgumentException("la cantidad de puertos no puede ser negativa");
			this.cantidad = cantidad;
		}
		public PortType getPortType() {
			return portType;
		}
		public void setPortType(PortType portType) {
			this.portType = portType;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	}

}
