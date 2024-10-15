
package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.isfpp.Service.*;
import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import javax.swing.*;

public class Web {
	private static  Web web = null;

	private String nombre;
	private HashMap<String, Equipment> hardware;
	private EquipmentService equipmentService;
	private ArrayList<Connection> connections;
	private ConnectionService connectionService;
	private HashMap<String, Location> locations;
	private LocationService locationService;
	private final HashMap<Object, EquipmentType> equipmentTypes;
	private EquipmentTypeService equipmentTypeService;
	private final HashMap<String,WireType>wireTypes;
	private WireTypeService wireTypeService;
	private final HashMap<String,PortType>portTypes;
	private PortTypeService portTypeService;
	private Coordinator coordinator;

	public Web() {
		super();
		setNombre("RedLocal");
		this.wireTypes				= new HashMap<>();
		this.wireTypeService 		= new WireTypeServiceImpl();
		wireTypes.putAll(wireTypeService.searchAll());
		this.portTypes				= new HashMap<>();
		this.portTypeService 		= new PortTypeServiceImpl();
		portTypes.putAll(portTypeService.searchAll());
		this.equipmentTypes			= new HashMap<>();
		this.equipmentTypeService 	= new EquipmentTypeServiceImpl();
		equipmentTypes.putAll(equipmentTypeService.searchAll());
		this.locations 				= new HashMap<>();
		this.locationService 		= new LocationServiceImpl();
		locations.putAll(locationService.searchAll());
		this.hardware     			= new HashMap<>();
		this.equipmentService 		= new EquipmentServiceImpl();
		hardware.putAll(equipmentService.searchAll());
		this.connections 			= new ArrayList<>();
		this.connectionService 		= new ConnectionServiceImpl();
		connections.addAll(connectionService.searchAll());
	}

	public Web(String nombre) {
		super();
		this.nombre = nombre;
		this.hardware = new HashMap<>();
		this.connections = new ArrayList<>();
		this.locations = new HashMap<>();
		this.wireTypes= new HashMap<>();
		this.portTypes= new HashMap<>();
		this.equipmentTypes= new HashMap<>();
		coordinator = new Coordinator();
	}

	public static Web getWeb() {
		if (web ==  null)
			web = new Web();
		return web;
	}
	//Name
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	//Equipment
	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType,int cantidad,
								  EquipmentType equipmentType, Location location,Boolean status)  {
		if (hardware.containsKey(code))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if(!equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		Equipment e = new Equipment(code, description, marca, model, portType,cantidad, equipmentType, location,status);
		hardware.put(code, e);
		equipmentService.insert(e);
		coordinator.updateTablas();
		return e;
	}

	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode())) {
			throw new NotFoundException("equipo invalido");
		}

		Iterator<Connection> iterator = connections.iterator();
		while (iterator.hasNext()) {
			Connection c = iterator.next();
			// Eliminar la conexión del equipo si coincide con alguno de los puertos
			if (c.getPort1().getEquipment().equals(e) || c.getPort2().getEquipment().equals(e)) {
				iterator.remove(); // Elimina la conexión del Iterator para evitar ConcurrentModificationException
				if (connections.contains(c)) {
					eraseConnection(c); // Solo llama a eraseConnection si la conexión aún está en la lista
				}
			}
		}

		hardware.remove(e.getCode(), e);  // Eliminar el equipo del hardware
		equipmentService.erase(e);
		coordinator.updateTablas();
	}

	public void updateEquipment(Equipment equipment) {
		if (!hardware.containsKey(equipment.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.replace(equipment.getCode(), equipment);
		equipmentService.update(equipment);
		coordinator.updateTablas();
	}

	public Equipment searchEquipment(Equipment equipment) {
		return hardware.get(equipment.getCode());
	}

	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	//Connection
	public Connection addConnection(Port port1, Port port2, WireType wire) {
		// Verificar si los equipos existen en el hardware
		if (!hardware.containsKey(port1.getEquipment().getCode()))
			throw new NotFoundException("El equipo " + port1.getEquipment().getCode() + " no se encuentra.");
		if (!hardware.containsKey(port2.getEquipment().getCode()))
			throw new NotFoundException("El equipo " + port2.getEquipment().getCode() + " no se encuentra.");

		Connection connection = new Connection(port2,port1, wire);

		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + port1.getEquipment().getCode() + " y " + port2.getEquipment().getCode() + " ya existe.");

		// Agregar la conexión a la lista de conexiones
		connections.add(connection);
		connectionService.insert(connection);
		coordinator.updateTablas();
		return connection;
	}

	public void eraseConnection(Connection connection) {
		// Verificar si la conexión existe
		if (!connections.contains(connection))
			throw new NotFoundException("La conexión no se encuentra.");

		// Eliminar la conexión de la lista
		connection.getPort1().setInUse(false);
		connection.getPort2().setInUse(false);
		connections.remove(connection);
		connectionService.erase(connection);
		coordinator.updateTablas();
	}

	public void updateConnection(Connection connection){
		if (!connections.contains(connection))
			throw new NotFoundException("Conexion no entrada");

		int index = connections.indexOf(connection);
		connections.set(index,connection);
		connectionService.update(connection);
		coordinator.updateTablas();
	}

	public Connection searchConnection(Connection connection){
		return connections.get(connections.indexOf(connection));
	}

	public ArrayList<Connection> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}

	//Location
	public Location addLocation(String code, String description) {
		if (locations.containsKey(code)){
			JOptionPane.showMessageDialog(null,"La localizacion ya se encuentra", "Error de duplicacion", JOptionPane.INFORMATION_MESSAGE);
			throw new AlreadyExistException("la localizacion ya se encuentra");

		}
		Location l = new Location(code, description);
		locations.put(code, l);
		locationService.insert(l);
		coordinator.updateTablas();
		return l;
	}

	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode()))
			JOptionPane.showMessageDialog(null,"Error de locacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			JOptionPane.showMessageDialog(null,"Hay equipos que dependen de la ubicacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
		locations.remove(l.getCode(), l);
		locationService.erase(l);
		coordinator.updateTablas();
	}

	public void updateLocation(Location l){
		if (!locations.containsKey(l.getCode()))
			throw new NotFoundException("la ubicacion no se pudo encontrar");

		locations.replace(l.getCode(),l);
		locationService.update(l);
		coordinator.updateTablas();
	}

	public Location searchLocation(Location location){
		return locations.get(location.getCode());
	}

	public HashMap<String, Location> getLocations() {
		return locations;
	}

	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}

	//WireType
	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		wireTypeService.insert(w);
		coordinator.updateTablas();
		return w;
	}

	public void eraseWire(WireType w){
		if(!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Connection connection : connections) {
			if(connection.getWire().equals(w))
				codes.add(connection.getPort1().getEquipment().getCode() + "<-> " +connection.getPort2().getEquipment().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(w.getCode(),w);
		wireTypeService.erase(w);
		coordinator.updateTablas();
	}

	public void updateWire(WireType wireType){
		if (wireTypes.containsKey(wireType.getCode()))
			throw new NotFoundException("Tipo de cable no encontrado");

		wireTypes.replace(wireType.getCode(),wireType);
		wireTypeService.update(wireType);
		coordinator.updateTablas();
	}

	public WireType searchWire (WireType wireType){
		return wireTypes.get(wireType.getCode());
	}

	public HashMap<String, WireType> getWireTypes() {
		return wireTypes;
	}

	//PortType
	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p=new PortType(code,description,speed);
		portTypes.put(code,p);
		portTypeService.insert(p);
		coordinator.updateTablas();
		return p;
	}

	public void erasePort(PortType portType){
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getAllPortsTypes().containsKey(portType))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
		portTypes.remove(portType.getCode(),portType);
		portTypeService.erase(portType);
		coordinator.updateTablas();
	}

	public void updatePort(PortType portType){
		if (portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("Tipo de puerto no encontrado");

		portTypes.replace(portType.getCode(),portType);
		portTypeService.update(portType);
		coordinator.updateTablas();
	}

	public PortType searchPortType(PortType portType){
		return portTypes.get(portType.getCode());
	}

	public HashMap<String, PortType> getPortTypes() {
		return portTypes;
	}

	//EquipmentType
	public EquipmentType addEquipmentType(String code,String description){
		if(equipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType e=new EquipmentType(code,description);
		equipmentTypes.put(code,e);
		equipmentTypeService.insert(e);
		coordinator.updateTablas();
		return e;
	}

	public void eraseEquipmentType(EquipmentType equipmentType){
		if (!equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("tipo de equipo no encontrado");
		List<String> codes = new ArrayList<>();
		for (Equipment equipment: hardware.values()) {
			if(equipment.getEquipmentType().equals(equipmentType))
				codes.add(equipment.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("los siguientes equipos poseen este tipo de equipo, en la red" + codes);

		equipmentTypes.remove(equipmentType.getCode(),equipmentType);
		equipmentTypeService.erase(equipmentType);
		coordinator.updateTablas();
	}

	public void updateEquipmentType(EquipmentType equipmentType){
		if (equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("No se pudo encontrar el tipo de equipo");

		equipmentTypes.replace(equipmentType.getCode(),equipmentType);
		equipmentTypeService.update(equipmentType);
		coordinator.updateTablas();
	}

	public EquipmentType searchEquipmentType(EquipmentType equipmentType){
		return equipmentTypes.get(equipmentType);
	}

	public HashMap<Object, EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	//Coordinator
	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	public Coordinator getCoordinator(){
		return coordinator;
	}

	@Override
	public String toString() {
		return "Web{" +
				"hardware=" + hardware +
				", connections=" + connections +
				", locations=" + locations +
				", nombre='" + nombre + '\'' +
				", EquipmentTypes=" + equipmentTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}


}
