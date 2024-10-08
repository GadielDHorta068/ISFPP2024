package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.isfpp.Service.*;
import org.isfpp.controller.Coordinator;
import org.isfpp.datos.CargarParametros;
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

	public void addEquipment1(Equipment equipment) throws AlreadyExistException {
		if (hardware.containsKey(equipment.getCode()))
			throw new AlreadyExistException("el equipo ya existe");
		hardware.put(equipment.getCode(),equipment);
		equipmentService.insert(equipment);
	}

	public void updateEquipment(Equipment equipment) {
		hardware.replace(equipment.getCode(), equipment);
		equipmentService.update(equipment);
	}

	public void deleteEquipment(Equipment equipment) {
		hardware.remove(equipment.getCode());
		equipmentService.erase(equipment);
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
			throw new AlreadyExistException("la localizacion ya se encuentra");

		Location l = new Location(code, description);
		locations.put(code, l);
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

	}

	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p=new PortType(code,description,speed);
		portTypes.put(code,p);
		return p;
	}
	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		return w;
	}
	public EquipmentType addEquipmentType(String code,String description){
		if(equipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType e=new EquipmentType(code,description);
		equipmentTypes.put(code,e);
		return e;
	}
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

	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode()))
			throw new NotFoundException("equipo invalido");
		hardware.remove(e.getCode(), e);
		for (Connection c : connections){
			if(c.getPort1().getEquipment().equals(e) || c.getPort2().getEquipment().equals(e)){
				eraseConnection(c);
			}
		}
	}
    public void eraseWire(WireType w){
		if(!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<>();
		for (org.isfpp.modelo.Connection connection : connections) {
			if(connection.getWire().equals(w))
				codes.add(connection.getPort1().getEquipment().getCode()+"<->"+connection.getPort2().getEquipment().getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(w.getCode(),w);

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
		locations.remove(portType.getCode(),portType);
	}
	// Agregar una conexión entre dos equipos
	public org.isfpp.modelo.Connection addConnection(Port port1, Port port2, WireType wire) {
		// Verificar si los equipos existen en el hardware
		if (!hardware.containsKey(port1.getEquipment().getCode())) {
			throw new NotFoundException("El equipo " + port1.getEquipment().getCode() + " no se encuentra.");
		}
		if (!hardware.containsKey(port2.getEquipment().getCode())) {
			throw new NotFoundException("El equipo " + port2.getEquipment().getCode() + " no se encuentra.");
		}

		org.isfpp.modelo.Connection connection = new org.isfpp.modelo.Connection(port1, port2,wire);

		if (connections.contains(connection)) {
			throw new AlreadyExistException("La conexión entre " + port1.getEquipment().getCode() + " y " + port2.getEquipment().getCode() + " ya existe.");
		}

		// Agregar la conexión a la lista de conexiones
		connections.add(connection);
		return connection;
	}

	public HashMap<Object, EquipmentType> getEquipmentTypes() {
		return equipmentTypes;
	}

	public HashMap<String, WireType> getWireTypes() {
		return wireTypes;
	}

	public HashMap<String, PortType> getPortTypes() {
		return portTypes;
	}

	// Eliminar una conexión
	public void eraseConnection(org.isfpp.modelo.Connection connection) {
		// Verificar si la conexión existe
		if (!connections.contains(connection)) {
			throw new NotFoundException("La conexión no se encuentra.");
		}

		// Eliminar la conexión de la lista
		connections.remove(connection);
	}

	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
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
