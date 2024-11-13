
package org.isfpp.logica;

import java.util.*;


import org.apache.log4j.Logger;
import org.isfpp.Service.*;
import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import org.isfpp.modelo.*;

import javax.swing.*;

/**
 * Clase principal del modelo, la misma contiene todos los objetos
 */
public class Lan {
	private static Lan lan = null;

    private final static Logger logger = Logger.getLogger(Lan.class);
    private Subject subject;
	private String nombre;
	private HashMap<String, Equipment> hardware;
	private final EquipmentService equipmentService;
	private ArrayList<Connection> connections;
	private final ConnectionService connectionService;
	private HashMap<String, Location> locations;
	private final LocationService locationService;
	private final HashMap<Object, EquipmentType> equipmentTypes;
	private final EquipmentTypeService equipmentTypeService;
	private final HashMap<String, WireType>wireTypes;
	private final WireTypeService wireTypeService;
	private final HashMap<String,PortType>portTypes;
	private final PortTypeService portTypeService;
	private Coordinator coordinator;


	/**
	 * Constructor de la clase
	 */
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

	/**
	 * Devolverse a si mismo
	 * @return Lan
	 */
	public static Lan getLan() {
		if (lan ==  null)
			lan = new Lan();
		return lan;
	}

	/**
	 * Obtener el nombre de nuestra red
	 * @return String
	 */
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Añadir equipo nuevo pasando todos sus atributos
	 * @param code codigo del equipo
	 * @param description descripcion del equipo
	 * @param marca marca del equipo
	 * @param model modelo
	 * @param portType tipo de puerto que tendra
	 * @param cantidad cantidad de ese tipo
	 * @param equipmentType tipo de equipo
	 * @param location ubicacion
	 * @param status estado
	 * @return Equipment
	 */
	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType,int cantidad,
								  EquipmentType equipmentType, Location location,Boolean status)  {
		if (hardware.containsKey(code))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if(!equipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");

		Equipment e = new Equipment(code, description, marca, model, portType,cantidad, equipmentType, location,status);
		if (hardware.isEmpty()){
			e.addIp("192.168.0.0");
		}
		hardware.put(code, e);
		coordinator.updateTablas(this);
		equipmentService.insert(e);
		subject.refresh();
		logger.info("Se agrega un equipo");
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
		System.out.println(equipment);
		coordinator.updateTablas(this);
		equipmentService.insert(equipment);
        subject.refresh();
        logger.info("Se agrega un equipo");

    }

	/**
	 * Eliminar un equipo de la Lan
	 * @param e equipo a ser borrado
	 */
	public void eraseEquipment(Equipment e) {
		if (!hardware.containsKey(e.getCode())) {
			throw new NotFoundException("equipo invalido");
		}
        for (Connection c : List.copyOf(connections)) {
            if (c.getPort1().getEquipment().equals(e) || c.getPort2().getEquipment().equals(e)) {
                if (connections.contains(c)) {
                    eraseConnection(c);
                }
            }
        }

		hardware.remove(e.getCode(), e);
		equipmentService.erase(e);
		subject.refresh();
		logger.info("Se elimino un equipo");
		coordinator.updateTablas(this);
		coordinator.setSelectedItem(null);
	}

	/**
	 * Buscar un equipo
	 * @param equipment equipo a buscar
	 * @return Equipo
	 */
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
			coordinator.updateTablas(this);
		}
	}

	/**
	 * Guarda todos los equipos dentro de la red en el directorio dado
	 * @param directory Directorio desde donde se levanta los archivos
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
		Equipment e1 = hardware.get(port1.getEquipment().getCode());
		Equipment e2 = hardware.get(port2.getEquipment().getCode());

		if (!hardware.containsKey(port1.getEquipment().getCode()))
			throw new NotFoundException("El equipo " + port1.getEquipment().getCode() + " no se encuentra.");
		if (!hardware.containsKey(port2.getEquipment().getCode()))
			throw new NotFoundException("El equipo " + port2.getEquipment().getCode() + " no se encuentra.");

		if (e1.getIpAdresses().isEmpty() && e2.getIpAdresses().isEmpty()){
			System.out.println(e1.getIpAdresses());
			e1.addIp("192.168.0.0");
			addIpToEquipment(e2, e1.getIpAdresses().getLast());
		}else if (!e1.getIpAdresses().isEmpty() && e2.getIpAdresses().isEmpty()){
			addIpToEquipment(e2, e1.getIpAdresses().getLast());
		} else {
			addIpToEquipment(e1, e2.getIpAdresses().getLast());
		}
		Connection connection = new Connection(port1,port2, wire);


		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + port1.getEquipment().getCode() + " y " + port2.getEquipment().getCode() + " ya existe.");

		connections.add(connection);
		connectionService.insert(connection);
		coordinator.updateTablas(this);
		subject.refresh();
		logger.info("Se agrega una conexion");
		return connection;
	}

	/**
	 * Agrega una nueva conexión a la red.
	 *
	 * @param connection Conexion que se quiere agregar a la red
	 * @throws NotFoundException     si algún equipo no se encuentra
	 * @throws AlreadyExistException si la conexión ya existe
	 */
	public void addConnection(Connection connection) {
		Equipment eq1 = connection.getPort1().getEquipment();
		Equipment eq2 = connection.getPort2().getEquipment();

		if (!hardware.containsKey(eq1.getCode()))
			throw new NotFoundException("El equipo " + eq1.getCode() + " no se encuentra.");
		if (!hardware.containsKey(eq2.getCode()))
			throw new NotFoundException("El equipo " + eq2.getCode() + " no se encuentra.");

		if (connections.contains(connection))
			throw new AlreadyExistException("La conexión entre " + eq1.getCode() + " y " + eq2.getCode() + " ya existe.");

		connections.add(connection);
		subject.refresh();
		logger.info("Se agrega una conexion");
		connectionService.insert(connection);
		coordinator.updateTablas(this);
    }

	public void eraseConnection(Connection connection) {
		if (!connections.contains(connection))
			throw new NotFoundException("La conexión no se encuentra.");

		connection.getPort1().setInUse(false);
		connection.getPort2().setInUse(false);
		connections.remove(connection);
		connectionService.erase(connection);
		subject.refresh();
		logger.info("Se borro una conexion");
		coordinator.updateTablas(this);
		coordinator.setSelectedItem(null);
	}

	public void updateConnection(Equipment originalEquipment1, Equipment originalEquipment2,Connection updateConnection){
		Connection originalConnection = null, connectionIndex;

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
        subject.refresh();
        logger.info("Se actualizo una conexion");
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
			if (connections.contains(connection)) {
				updateConnection(connection.getPort1().getEquipment(), connection.getPort2().getEquipment(), connection);

			}
			else
				addConnection(connection);
	}

	public ArrayList<Connection> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}

	public Location addLocation(String code, String description) {
		if (locations.containsKey(code)){
			JOptionPane.showMessageDialog(null,"La localizacion ya se encuentra", "Error de duplicacion", JOptionPane.INFORMATION_MESSAGE);
			throw new AlreadyExistException("la localizacion ya se encuentra");

		}
		Location l = new Location(code, description);
		locations.put(code, l);
		coordinator.updateTablas(this);
		locationService.insert(l);
		subject.refresh();
		logger.info("Se agrego una localizacion");
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
        subject.refresh();
        logger.info("Se agrego una localizacion");
    }

	/**
	 * Eliminar una ubicacion
	 * @param l ubicacion
	 */
	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode()))
			JOptionPane.showMessageDialog(null,"Error de locacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty()){
			JOptionPane.showMessageDialog(null,"Hay equipos que dependen de la ubicacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
		}else{
			locations.remove(l.getCode(), l);
			locationService.erase(l);
			coordinator.updateTablas(this);
			coordinator.setSelectedItem(null);
			subject.refresh();
			logger.info("Se borro una localizacion");
		}
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
		subject.refresh();
		logger.info("Se agrego un cable");
		return w;
	}

	public void addWire(WireType wireType) {
		if (wireTypes.containsKey(wireType.getCode()))
			throw new AlreadyExistException("el tipo de cable ya existe");

		wireTypes.put(wireType.getCode(), wireType);
		wireTypeService.insert(wireType);
        subject.refresh();
        logger.info("Se agrego un cable");
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
		subject.refresh();
		logger.info("Se elimino un cable");
	}

	public void updateWire(String originalCode,WireType wireType){
		if (wireTypes.containsKey(wireType.getCode()))
			throw new NotFoundException("Tipo de cable no encontrado");

		wireTypes.replace(originalCode, wireType);
		wireTypeService.update(wireType);
		subject.refresh();
		logger.info("cable actualizado");
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
		subject.refresh();
		logger.info("Se agrego un puerto");
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
        subject.refresh();
        logger.info("Se agrego un puerto");
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
		subject.refresh();
		logger.info("Se elimino un puerto");
	}

	public void updatePort(PortType portType){
		if (portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("Tipo de puerto no encontrado");

		portTypes.replace(portType.getCode(),portType);
		portTypeService.update(portType);
		subject.refresh();
		logger.info("Se actualizo un tipo de puerto");
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
		subject.refresh();
		logger.info("Se agrega un tipo equipo");
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
        subject.refresh();
        logger.info("Se actualizo un tipo equipo");
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
		subject.refresh();
		coordinator.updateTablas(this);
		logger.info("Se elimino un tipo equipo");
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
		subject.refresh();
		logger.info("Se actualizo un puerto");
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
		equipmentTypes.put(updateEquipmentType.getCode(),updateEquipmentType);
        subject.refresh();
        logger.info("Se actualizo un tipoEquipo");
    }
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
        subject.refresh();
        logger.info("Se conexion actualizada");
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
        subject.refresh();
        logger.info("Se actualizo un equipo");
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
        subject.refresh();
        logger.info("Se actualizo una ubicacion");
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


	public void init(org.isfpp.logica.Subject subject){
		this.subject = subject;
	}


		public String getNextAvailableIp(Equipment equipment, String initialIp) {
			Set<String> usedIps = new HashSet<>();
			for (Equipment e : hardware.values()){
				usedIps.addAll(e.getIpAdresses());
			}
			String newIp;
			String[] ipParts = initialIp.split("\\.");

			if (isSwitchOrRouter(equipment)) {
				int fourOctet = Integer.parseInt(ipParts[2]);
				do {
					fourOctet++;
					ipParts[2] = String.valueOf(fourOctet);
					newIp = String.join(".", ipParts);
				} while (usedIps.contains(newIp));
			} else {
				int lastOctet = Integer.parseInt(ipParts[3]);
				do {
					lastOctet++;
					ipParts[3] = String.valueOf(lastOctet);
					newIp = String.join(".", ipParts);
				} while (usedIps.contains(newIp));
			}
			return newIp;
		}

		private boolean isSwitchOrRouter(Equipment equipment) {
			String type = equipment.getEquipmentType().getCode();
			return type.equalsIgnoreCase("SW") || type.equalsIgnoreCase("RT") || type.equalsIgnoreCase("AP");
		}

		public void addIpToEquipment(Equipment equipment, String initialIp) {
			String newIp = getNextAvailableIp(equipment, initialIp);
			equipment.addIp(newIp);
		}
}
