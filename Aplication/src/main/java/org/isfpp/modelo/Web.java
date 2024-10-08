package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.isfpp.Service.*;
import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

public class Web {
	private static  Web web = null;
	private Coordinator coordinator;

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

	//constructor protoDao-Service
	//web
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
	}

	public static Web getWeb() {
		if (web ==  null)
			web = new Web();
		return web;
	}

	//name
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
		return e;
	}

	public void addEquipment(Equipment equipment) throws AlreadyExistException {
		if (hardware.containsKey(equipment.getCode()))
			throw new AlreadyExistException("el equipo ya existe");
		hardware.put(equipment.getCode(),equipment);
		equipmentService.insert(equipment);
	}

	public void eraseEquipment(Equipment equipment) {
		if (!hardware.containsKey(equipment.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.remove(equipment.getCode(), equipment);
		equipmentService.erase(equipment);
		for (Connection c : connections)
			if(c.getPort1().getEquipment().equals(equipment) || c.getPort2().getEquipment().equals(equipment))
				eraseConnection(c);
	}

	public void updateEquipment(Equipment equipment) {
		if (!hardware.containsKey(equipment.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.replace(equipment.getCode(), equipment);
		equipmentService.update(equipment);
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

		Connection connection = new Connection(port1, port2,wire);

		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + port1.getEquipment().getCode() + " y " + port2.getEquipment().getCode() + " ya existe.");

		// Agregar la conexión a la lista de conexiones
		connections.add(connection);
		connectionService.insert(connection);
		return connection;
	}

	public Connection addConnection(Connection connection) {
		// Verificar si los equipos existen en el hardware
		if (!hardware.containsKey(connection.getPort1().getEquipment().getCode()))
			throw new NotFoundException("El equipo " + connection.getPort1().getEquipment().getCode() + " no se encuentra.");
		if (!hardware.containsKey(connection.getPort2().getEquipment().getCode()))
			throw new NotFoundException("El equipo " + connection.getPort2().getEquipment().getCode() + " no se encuentra.");
		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + connection.getPort1().getEquipment().getCode() + " y " + connection.getPort2().getEquipment().getCode() + " ya existe.");

		// Agregar la conexión a la lista de conexiones
		connections.add(connection);
		connectionService.insert(connection);
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
	}

	public void updateConnection(Connection connection){
		if (!connections.contains(connection))
			throw new NotFoundException("Conexion no entrada");

		int index = connections.indexOf(connection);
		connections.set(index,connection);
		connectionService.update(connection);
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

	//portType
	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p=new PortType(code,description,speed);
		portTypes.put(code,p);
		portTypeService.insert(p);
		return p;
	}

	public PortType addPort(PortType portType){
		if(portTypes.containsKey(portType.getCode()))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		portTypes.put(portType.getCode(),portType);
		portTypeService.insert(portType);
		return portType;
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
	}

	public void updatePort(PortType portType){
		if (portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("Tipo de puerto no encontrado");

		portTypes.replace(portType.getCode(),portType);
		portTypeService.update(portType);
	}

	public PortType searchPortType(PortType portType){
		return portTypes.get(portType.getCode());
	}

	public HashMap<String, PortType> getPortTypes() {
		return portTypes;
	}

	//WireType
	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		wireTypeService.insert(w);
		return w;
	}

	public void eraseWire(WireType w){
		if(!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Connection connection : connections) {
			if(connection.getWire().equals(w))
				codes.add(connection.getPort1().getEquipment().getCode()+"<->"+connection.getPort2().getEquipment().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(w.getCode(),w);
		wireTypeService.erase(w);

	}

	public void updateWire(WireType wireType){
		if (wireTypes.containsKey(wireType.getCode()))
			throw new NotFoundException("Tipo de cable no encontrado");

		wireTypes.replace(wireType.getCode(),wireType);
		wireTypeService.update(wireType);
	}

	public WireType searchWire (WireType wireType){
		return wireTypes.get(wireType.getCode());
	}

	public HashMap<String, WireType> getWireTypes() {
		return wireTypes;
	}

	//EquipmentType
	public EquipmentType addEquipmentType(String code,String description){
		if(equipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType e=new EquipmentType(code,description);
		equipmentTypes.put(code,e);
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
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);

		equipmentTypes.remove(equipmentType.getCode(),equipmentType);
		equipmentTypeService.erase(equipmentType);
	}

	public void updateEquipmentType(EquipmentType equipmentType){
		if (equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("No se pudo encontrar el tipo de equipo");

		equipmentTypes.replace(equipmentType.getCode(),equipmentType);
		equipmentTypeService.update(equipmentType);
	}

	public EquipmentType searchEquipmentType(EquipmentType equipmentType){
		return equipmentTypes.get(equipmentType);
	}

	public HashMap<Object, EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	//Location
	public Location addLocation(String code, String description) {
		if (locations.containsKey(code))
			throw new AlreadyExistException("la localizacion ya se encuentra");

		Location l = new Location(code, description);
		locations.put(code, l);
		locationService.insert(l);
		return l;
	}

	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode()))
			throw new NotFoundException("Localizacion invalida");
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que dependen de esa ubicación: " + codes);
		locations.remove(l.getCode(), l);
		locationService.erase(l);
	}

	public void updateLocation(Location l){
		if (!locations.containsKey(l.getCode()))
			throw new NotFoundException("la ubicacion no se pudo encontrar");

		locations.replace(l.getCode(),l);
		locationService.update(l);
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

	//Coordinador
	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}
	public Coordinator getCoordinator() {
        return this.coordinator;
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
