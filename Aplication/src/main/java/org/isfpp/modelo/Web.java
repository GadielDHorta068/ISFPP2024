package org.isfpp.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import javax.swing.*;

import static org.isfpp.logica.Utils.*;

public class Web {
	private HashMap<String, Equipment> hardware;
	private ArrayList<Connection> connections;
	private HashMap<String, Location> locations;
	private String nombre;
	private final HashMap<Object, EquipmentType> EquipmentTypes;
	private final HashMap<String,WireType>wireTypes;
	private final HashMap<String,PortType>portTypes;
	private Coordinator coordinator;

	/**
	 * Constructor de la clase Web.
	 * @param nombre Nombre de la web.
	 */
	public Web(String nombre) {
		super();
		this.nombre = nombre;
		this.hardware = new HashMap<>();
		this.connections = new ArrayList<>();
		this.locations = new HashMap<>();
		this.wireTypes= new HashMap<>();
		this.portTypes= new HashMap<>();
		this.EquipmentTypes= new HashMap<>();
		coordinator = new Coordinator();
	}

	/**
	 * Obtiene el hardware de la web.
	 * @return Un HashMap con el hardware.
	 */
	public HashMap<String, Equipment> getHardware() {
		return hardware;
	}

	/**
	 * Establece el hardware de la web.
	 * @param hardware Un HashMap con el hardware.
	 */
	public void setHardware(HashMap<String, Equipment> hardware) {
		this.hardware = hardware;
	}

	/**
	 * Obtiene las conexiones de la web.
	 * @return Una lista de conexiones.
	 */
	public ArrayList<org.isfpp.modelo.Connection> getConnections() {
		return connections;
	}

	/**
	 * Establece las conexiones de la web.
	 * @param connections Una lista de conexiones.
	 */
	public void setConnections(ArrayList<org.isfpp.modelo.Connection> connections) {
		this.connections = connections;
	}

	/**
	 * Obtiene las ubicaciones de la web.
	 * @return Un HashMap con las ubicaciones.
	 */
	public HashMap<String, Location> getLocations() {
		return locations;
	}

	/**
	 * Establece las ubicaciones de la web.
	 * @param locations Un HashMap con las ubicaciones.
	 */
	public void setLocations(HashMap<String, Location> locations) {
		this.locations = locations;
	}

	/**
	 * Obtiene el nombre de la web.
	 * @return El nombre de la web.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre de la web.
	 * @param nombre El nombre de la web.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Agrega una ubicación a la web.
	 * @param code Código de la ubicación.
	 * @param description Descripción de la ubicación.
	 * @return La ubicación agregada.
	 * @throws AlreadyExistException Si la ubicación ya existe.
	 */
	public Location addLocation(String code, String description) {
		if (locations.containsKey(code)){
			JOptionPane.showMessageDialog(null,"La localizacion ya se encuentra", "Error de duplicacion", JOptionPane.INFORMATION_MESSAGE);
			throw new AlreadyExistException("la localizacion ya se encuentra");
		}
		Location l = new Location(code, description);
		locations.put(code, l);
		coordinator.updateTablas();
		return l;
	}

	/**
	 * Elimina una ubicación de la web.
	 * @param l La ubicación a eliminar.
	 */
	public void eraseLocation(Location l) {
		if (!locations.containsKey(l.getCode())){
			JOptionPane.showMessageDialog(null,"Error de locacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getLocation().equals(l))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty()){
			JOptionPane.showMessageDialog(null,"Hay equipos que dependen de la ubicacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		locations.remove(l.getCode(), l);
		coordinator.updateTablas();
	}

	/**
	 * Agrega un tipo de puerto a la web.
	 * @param code Código del puerto.
	 * @param description Descripción del puerto.
	 * @param speed Velocidad del puerto.
	 * @return El tipo de puerto agregado.
	 * @throws AlreadyExistException Si el puerto ya existe.
	 */
	public PortType addPort(String code,String description,int speed){
		if(portTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de puerto ya se encuentra");
		PortType p=new PortType(code, description, speed);
		portTypes.put(code,p);
		coordinator.updateTablas();
		return p;
	}

	/**
	 * Agrega un tipo de cable a la web.
	 * @param code Código del cable.
	 * @param description Descripción del cable.
	 * @param speed Velocidad del cable.
	 * @return El tipo de cable agregado.
	 * @throws AlreadyExistException Si el cable ya existe.
	 */
	public WireType addWire(String code,String description,int speed){
		if(wireTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de cable ya existe");
		WireType w=new WireType(code,description,speed);
		wireTypes.put(code,w);
		coordinator.updateTablas();
		return w;
	}

	/**
	 * Agrega un tipo de equipo a la web.
	 * @param code Código del equipo.
	 * @param description Descripción del equipo.
	 * @return El tipo de equipo agregado.
	 * @throws AlreadyExistException Si el equipo ya existe.
	 */
	public EquipmentType addEquipmentType(String code,String description){
		if(EquipmentTypes.containsKey(code))
			throw new AlreadyExistException("el tipo de equipo ya existe");
		EquipmentType e=new EquipmentType(code,description);
		EquipmentTypes.put(code,e);
		coordinator.updateTablas();
		return e;
	}

	/**
	 * Agrega un equipo a la web.
	 * @param code Código del equipo.
	 * @param description Descripción del equipo.
	 * @param marca Marca del equipo.
	 * @param model Modelo del equipo.
	 * @param portType Tipo de puerto del equipo.
	 * @param cantidad Cantidad de puertos.
	 * @param equipmentType Tipo de equipo.
	 * @param location Ubicación del equipo.
	 * @param status Estado del equipo.
	 * @return El equipo agregado.
	 * @throws AlreadyExistException Si el equipo ya existe.
	 * @throws NotFoundException Si el puerto o el tipo de equipo no se encuentran.
	 */
	public Equipment addEquipment(String code, String description, String marca, String model, PortType portType, int cantidad,
								  EquipmentType equipmentType, Location location, Boolean status)  {
		if (hardware.containsKey(code))
			throw new AlreadyExistException("el equipo ya se encuentra");
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra en la lista");
		if(!EquipmentTypes.containsKey(equipmentType.getCode()))
			throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
		Equipment e = new Equipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
		hardware.put(code, e);
		coordinator.updateTablas();
		return e;
	}

	/**
	 * Elimina un equipo de la web.
	 * @param e El equipo a eliminar.
	 * @throws NotFoundException Si el equipo no se encuentra.
	 */
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
		coordinator.updateTablas();
	}

	/**
	 * Elimina un tipo de cable de la web.
	 * @param w El cable a eliminar.
	 * @throws NotFoundException Si el cable no se encuentra.
	 * @throws IllegalStateException Si hay conexiones que dependen del cable.
	 */
    public void eraseWire(WireType w){
		if(!wireTypes.containsKey(w.getCode()))
			throw new NotFoundException("El cable no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Connection connection : connections) {
			if (connection.getWire().equals(w)) {
				codes.add(connection.getPort1().getEquipment().getCode() + " <-> " + connection.getPort2().getEquipment().getCode());
			}
		}

		if (!codes.isEmpty())
			throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
		wireTypes.remove(w.getCode(),w);
		coordinator.updateTablas();
	}

	/**
	 * Elimina un tipo de puerto de la web.
	 * @param portType El puerto a eliminar.
	 * @throws NotFoundException Si el puerto no se encuentra.
	 * @throws IllegalStateException Si hay equipos que usan ese tipo de puerto.
	 */
	public void erasePort(PortType portType){
		if(!portTypes.containsKey(portType.getCode()))
			throw new NotFoundException("El puerto no se encuentra");
		List<String> codes = new ArrayList<>();
		for (Equipment e : hardware.values()) {
			if (e.getAllPortsTypes().containsKey(portType))
				codes.add(e.getCode());
		}
		if (!codes.isEmpty())
			throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: "+codes);
		locations.remove(portType.getCode(),portType);
		coordinator.updateTablas();
	}

	/**
	 * Agrega una conexión entre dos equipos.
	 * @param port1 Primer puerto de la conexión.
	 * @param port2 Segundo puerto de la conexión.
	 * @param wire Tipo de cable de la conexión.
	 * @return La conexión agregada.
	 * @throws NotFoundException Si uno de los equipos no se encuentra.
	 * @throws AlreadyExistException Si la conexión ya existe.
	 */
	public Connection addConnection(Port port1, Port port2, WireType wire) {
		Equipment eq1 = port1.getEquipment();
		Equipment eq2 = port2.getEquipment();

		// Verificar si los equipos existen en el hardware
		if (!hardware.containsKey(eq1.getCode())) {
			throw new NotFoundException("El equipo " + port1.getEquipment().getCode() + " no se encuentra.");
		}
		if (!hardware.containsKey(eq2.getCode())) {
			throw new NotFoundException("El equipo " + port2.getEquipment().getCode() + " no se encuentra.");
		}

		verificarPuertosOcupados(eq1);
		verificarPuertosOcupados(eq2);

		Connection connection = new Connection(port2, port1, wire);

		if (connections.contains(connection)) {
			throw new AlreadyExistException("La conexión entre " + eq1.getCode() + " y " + eq2.getCode() + " ya existe.");
		}

		// Determinar si es necesario generar una nueva IP
		String nuevaIP = generarNuevaIP(eq1, this);

        // Agregar la conexión a la lista de conexiones
		connections.add(connection);
		eq2.addIp(nuevaIP);
		coordinator.updateTablas();
		return connection;
	}

	/**
	 * Obtiene los tipos de equipos de la web.
	 * @return Un HashMap con los tipos de equipos.
	 */
	public HashMap<Object, EquipmentType> getEquipmentTypes() {
		return EquipmentTypes;
	}

	/**
	 * Obtiene los tipos de cables de la web.
	 * @return Un HashMap con los tipos de cables.
	 */
	public HashMap<String, WireType> getWireTypes() {
		return wireTypes;
	}

	/**
	 * Obtiene los tipos de puertos de la web.
	 * @return Un HashMap con los tipos de puertos.
	 */
	public HashMap<String, PortType> getPortTypes() {
		return portTypes;
	}

	/**
	 * Elimina una conexión de la web.
	 * @param connection La conexión a eliminar.
	 * @throws NotFoundException Si la conexión no se encuentra.
	 */
	public void eraseConnection(Connection connection) {
		// Verificar si la conexión existe
		if (!connections.contains(connection)) {
			throw new NotFoundException("La conexión no se encuentra.");
		}

		// Eliminar la conexión de la lista
		connections.remove(connection);
		coordinator.updateTablas();
	}

	@Override
	public String toString() {
		return "Web{" +
				"hardware=" + hardware +
				", connections=" + connections +
				", locations=" + locations +
				", nombre='" + nombre + '\'' +
				", EquipmentTypes=" + EquipmentTypes +
				", wireTypes=" + wireTypes +
				", portTypes=" + portTypes +
				'}';
	}

	/**
	 * Establece un coordinador para la web.
	 * @param coordinator El coordinador a establecer.
	 */
	public void setCoordinator(Coordinator coordinator) {
		this.coordinator = coordinator;
	}

	/**
	 * Obtiene el coordinador de la web.
	 * @return El coordinador.
	 */
	public Coordinator getCoordinator(){
		return coordinator;
	}
}