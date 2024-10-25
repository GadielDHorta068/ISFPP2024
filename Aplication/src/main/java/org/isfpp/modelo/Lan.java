
package org.isfpp.modelo;

import java.util.*;


import org.isfpp.Service.*;
import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import javax.swing.*;

public class Lan {
	private static Lan lan = null;

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

	public Lan() {
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

	public static Lan getLan() {
		if (lan ==  null)
			lan = new Lan();
		return lan;
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
		coordinator.updateTablas(this);
		System.out.println("aca");
		return e;
	}

	/**
	 * Agrega un nuevo equipo a la red.
	 *
	 * @param equipment Equipo que se agrega a la red
	 *
	 *
	 * @throws AlreadyExistException si el equipo ya existe en la red
	 * @throws NotFoundException     si el tipo de puerto o equipo no se encuentra
	 */
	public void addEquipment(Equipment equipment){
		if (hardware.containsKey(equipment.getCode()))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if (!equipmentTypes.containsKey(equipment.getEquipmentType().getCode()))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		hardware.put(equipment.getCode(), equipment);
		equipmentService.insert(equipment);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	public void updateEquipment(String codeOriginal, Equipment updateEquipment) {
		if (!hardware.containsKey(updateEquipment.getCode()) && !codeOriginal.equals(updateEquipment.getCode()))
			throw new NotFoundException("Ese codigo ya existe");

		if(codeOriginal.equals(updateEquipment.getCode())){
			equipmentService.update(updateEquipment);
		}
		else {
			equipmentService.erase(hardware.get(codeOriginal));
			equipmentService.insert(updateEquipment);
			hardware.remove(codeOriginal);
		}
		hardware.replace(updateEquipment.getCode(),updateEquipment);
		coordinator.updateTablas(this);
	}

	public Equipment searchEquipment(Equipment equipment) {
		return hardware.get(equipment.getCode());
	}

	/**
	 * Agrega todos los equipos desde un archivo
	 *
	 * @param directory Directorio en donde se busca el archivo con los equipos
	 */
	public void addAllEquipmentOf(String directory){
		for (Equipment equipment: equipmentService.searchAllIn(directory).values()){
			if (hardware.containsKey(equipment.getCode()))
				updateEquipment(equipment.getCode(),equipment);
			else
				addEquipment(equipment);
		}
	}

	/**
	 * Guarda todos los equipos dentro de la red en el directorio dado
	 * @param directory
	 */
	public void insertAllEquipmentIn(String directory){
		equipmentService.insertAllIn(directory);
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
		coordinator.updateTablas(this);
		return connection;
	}

	/**
	 * Agrega una nueva conexión a la red.
	 *
	 * @param connection Conexion que se quiere agregar a la red
	 * @return la conexión agregada
	 * @throws NotFoundException     si algún equipo no se encuentra
	 * @throws AlreadyExistException si la conexión ya existe
	 */
	public void addConnection(Connection connection) {
		Equipment eq1 = connection.getPort1().getEquipment();
		Equipment eq2 = connection.getPort2().getEquipment();

		// Verificar si los equipos existen en el hardware
		if (!hardware.containsKey(eq1.getCode()))
			throw new NotFoundException("El equipo " + eq1.getCode() + " no se encuentra.");
		if (!hardware.containsKey(eq2.getCode()))
			throw new NotFoundException("El equipo " + eq2.getCode() + " no se encuentra.");

		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + eq1.getCode() + " y " + eq2.getCode() + " ya existe.");

		// Agregar la conexión a la lista de conexiones
		connections.add(connection);
		connectionService.insert(connection);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	/**
	 *
	 * @param originalConnection
	 * @param updateConnection
	 * @throws NotFoundException si la conexión no se encuentra
	 */
	public void updateConnection(Connection originalConnection,Connection updateConnection){
		if (connections.contains(updateConnection) && !originalConnection.equals(updateConnection))
			throw new NotFoundException("ya existe esa conexion");
		if(originalConnection.equals(updateConnection)){
			connectionService.update(updateConnection);
		}
		else {
			connectionService.erase(originalConnection);
			connectionService.insert(updateConnection);
		}
		connections.remove(originalConnection);
		connections.add(updateConnection);
		coordinator.updateTablas(this);
	}

	public void updateConnection(Equipment originalEquipment1, Equipment originalEquipment2,Connection updateConnection){
		Connection originalConnection = null, connectionIndex;

		for (int i = 0; originalConnection != null && i < connections.size(); i++){
			connectionIndex = connections.get(i);
			if ((originalEquipment1.equals(connectionIndex.getPort1().getEquipment()) && originalEquipment2.equals(connectionIndex.getPort2().getEquipment()))
					|| (originalEquipment2.equals(connectionIndex.getPort1().getEquipment()) && originalEquipment1.equals(connectionIndex.getPort2().getEquipment())))
				originalConnection = connectionIndex;
		}
		if (connections.contains(updateConnection) && !originalConnection.equals(updateConnection))
			throw new NotFoundException("ya existe esa conexion");
		if(originalConnection.equals(updateConnection)){
			connectionService.update(updateConnection);
		}
		else {
			connectionService.erase(originalConnection);
			connectionService.insert(updateConnection);
		}
		connections.remove(originalConnection);
		connections.add(updateConnection);
		coordinator.updateTablas(this);
	}

	public Connection searchConnection(Connection connection){
		return connections.get(connections.indexOf(connection));
	}

	/**
	 * Agrega todas las conexiones que se encientran en el directorio dado
	 *  y actualiza las conexiones ya existentes dentro de la red
	 *
	 * @param directory directorio en donde se buscan las conexiones
	 */
	public void addAllConnectionOf(String directory){
		for (Connection connection: connectionService.searchAllIn(directory))
			if (connections.contains(connection))
				updateConnection(connection.getPort1().getEquipment(), connection.getPort2().getEquipment(), connection);
			else
				addConnection(connection);
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
		coordinator.updateTablas(this);
		return l;
	}

	/**
	 * Agrega una nueva localización a la red.
	 *
	 * @param location Ubicacion que se quiere agregar a la red
	 * @return la localización agregada
	 * @throws AlreadyExistException si la localización ya existe
	 */
	public void addLocation(Location location) {
		if (locations.containsKey(location.getCode())) {
			JOptionPane.showMessageDialog(null, "La localizacion ya se encuentra", "Error de duplicacion", JOptionPane.INFORMATION_MESSAGE);
			throw new AlreadyExistException("la localizacion ya se encuentra");
		}

		locations.put(location.getCode(), location);
		locationService.insert(location);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	public void updateLocation(String codeOriginal, Location updateLocation){
		if (locations.containsKey(updateLocation.getCode()) && !codeOriginal.equals(updateLocation.getCode()))
			throw new NotFoundException("localizacion ya extiste");
		if(codeOriginal.equals(updateLocation.getCode())){
			locationService.update(updateLocation);
		}
		else {
			locationService.erase(locations.get(codeOriginal));
			locationService.insert(updateLocation);
			locations.remove(codeOriginal);
		}
		locations.put(updateLocation.getCode(),updateLocation);
		coordinator.updateTablas(this);
	}

	/**
	 * Agrega todas las ubicaciones que se encuentran en el directorio dado
	 *  y actualiza las ubicaciones ya existentes dentro de la red
	 *  actualiza las tablas y cálculo llamando a add*
	 *
	 * @param directory directorio en donde se buscan las ubicaciones
	 */
	public  void  addAllLocationOf(String directory){
		for (Location location: locationService.searchAllIn(directory).values())
			if (locations.containsKey(location.getCode()))
				updateLocation(location.getCode(), location);
			else
				addLocation(location);
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
		coordinator.updateTablas(this);
		return w;
	}

	public void addWire(WireType wireType) {
		if (wireTypes.containsKey(wireType.getCode()))
			throw new AlreadyExistException("el tipo de cable ya existe");

		wireTypes.put(wireType.getCode(), wireType);
		wireTypeService.insert(wireType);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	public void updateWire(String originalCode, WireType wireType){
		if (!wireTypes.containsKey(originalCode))
			throw new NotFoundException("Tipo de cable no encontrado");

		wireTypes.replace(wireType.getCode(),wireType);
		wireTypeService.update(wireType);
		coordinator.updateTablas(this);
	}

	/**
	 * Agrega todos los tipos de cable que se encuentran en el directorio dado
	 *  y actualiza los tipos de cable ya existentes dentro de la red
	 *
	 * @param directory directorio en donde se buscan los tipos de cable
	 */
	public  void  addAllWiretypeOf(String directory){
		for (WireType wireType: wireTypeService.searchAllIn(directory).values())
			if (locations.containsKey(wireType.getCode()))
				updateWire(wireType.getCode(),wireType);
			else
				addWire(wireType);
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
		coordinator.updateTablas(this);
		return p;
	}

	/**
	 * Agrega un nuevo tipo de puerto a la red.
	 *
	 * @param portType Tipo de puerto que se quiere agragar a la red
	 *
	 * @throws AlreadyExistException si el tipo de puerto ya existe
	 */
	public void addPort(PortType portType) {
		if (portTypes.containsKey(portType.getCode()))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");

		portTypes.put(portType.getCode(), portType);
		portTypeService.insert(portType);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	public void updatePortType(String originalCode,PortType updatedPort) {
		// Verificar si el código ya existe en el mapa
		if (!portTypes.containsKey(updatedPort.getCode()) && !Objects.equals(updatedPort.getCode(), originalCode)) {
			throw new NotFoundException("El tipo de puerto con código " + originalCode + " ya existe.");
		}
		if(Objects.equals(updatedPort.getCode(), originalCode)){
			portTypeService.update(updatedPort);
		}
		else {
			portTypeService.erase(portTypes.get(originalCode));
			portTypeService.insert(updatedPort);
			portTypes.remove(originalCode);
		}

		portTypes.put(updatedPort.getCode(), updatedPort);
		coordinator.updateTablas(this);
	}

	public PortType searchPortType(PortType portType){
		return portTypes.get(portType.getCode());
	}

	/**
	 * Agrega todos los tipos de puerto que se encuentran en el directorio dado
	 *  y actualiza los tipos de puerto ya existentes dentro de la red
	 *
	 * @param directory directorio en donde se buscan los tipos de puerto
	 */
	public  void  addAllPortTypeOf(String directory){
		for (PortType portType: portTypeService.searchAllIn(directory).values())
			if (locations.containsKey(portType.getCode()))
				updatePortType(portType.getCode(), portType);
			else
				addPort(portType);
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
		coordinator.updateTablas(this);
		return e;
	}

	/**
	 * Agrega un nuevo equipo a la red.
	 *
	 * @param equipmentType Tipo de equipo que se quiere agregar a la red
	 * @return el tipo de equipo agregado
	 * @throws AlreadyExistException si el tipo de equipo ya existe
	 */
	public void addEquipmentType(EquipmentType equipmentType) {
		if (equipmentTypes.containsKey(equipmentType.getCode()))
			throw new AlreadyExistException("el tipo de equipo ya existe");

		equipmentTypes.put(equipmentType.getCode(), equipmentType);
		equipmentTypeService.insert(equipmentType);
		coordinator.updateTablas(this);
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
		coordinator.updateTablas(this);
	}

	public void updateEquipmentType(String originalCode,EquipmentType updateEquipmentType){
		if (equipmentTypes.containsKey(updateEquipmentType.getCode()) && !Objects.equals(originalCode,updateEquipmentType.getCode()))
			throw new NotFoundException("No se pudo encontrar el tipo de equipo");
		if(Objects.equals(updateEquipmentType.getCode(), originalCode)){
			equipmentTypeService.update(updateEquipmentType);
		}
		else {
			equipmentTypeService.erase(equipmentTypes.get(originalCode));
			equipmentTypeService.insert(updateEquipmentType);
			equipmentTypes.remove(originalCode);
		}
		equipmentTypes.remove(originalCode);
		equipmentTypes.put(updateEquipmentType.getCode(), updateEquipmentType);
		coordinator.updateTablas(this);
	}

	/**
	 * Agrega todos los tipos de puerto que se encuentran en el directorio dado
	 *  y actualiza los tipos de puerto ya existentes dentro de la red
	 *
	 * @param directory directorio en donde se buscan los tipos de puerto
	 */
	public  void  addAllEquipmentTypeOf(String directory){
		for (EquipmentType equipmentType: equipmentTypeService.searchAllIn(directory).values())
			if (locations.containsKey(equipmentType.getCode()))
				updateEquipmentType(equipmentType.getCode(), equipmentType);
			else
				addEquipmentType(equipmentType);
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
		return "lan{" +
				"hardware=" + hardware +
				", connections=" + connections +
				", locations=" + locations +
				", nombre='" + nombre + '\'' +
				", EquipmentTypes=" + equipmentTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}

	public void insertAllConnectionInto(String directory){
		connectionService.insertAllIn(directory);
	}
	public void insertAllLocationInto(String directory){
		locationService.insertAllIn(directory);
	}
	public void insertAllPortTypeInto(String directory){
		portTypeService.insertAllIn(directory);
	}
	public void insertAllWireTypeInto(String directory){
		wireTypeService.insertAllIn(directory);
	}
	public void insertAllEquipmentTypeInto(String directory){
		equipmentTypeService.insertAllIn(directory);
	}

}
