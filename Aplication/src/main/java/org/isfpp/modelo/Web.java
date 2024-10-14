
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

import static org.isfpp.logica.Utils.generarNuevaIP;
import static org.isfpp.logica.Utils.verificarPuertosOcupados;

public class Web {
    private static Web web = null;

    private String nombre;
    private HashMap<String, Equipment> hardware;
    private EquipmentService equipmentService;
    private ArrayList<Connection> connections;
    private ConnectionService connectionService;
    private HashMap<String, Location> locations;
    private LocationService locationService;
    private final HashMap<Object, EquipmentType> equipmentTypes;
    private EquipmentTypeService equipmentTypeService;
    private final HashMap<String, WireType> wireTypes;
    private WireTypeService wireTypeService;
    private final HashMap<String, PortType> portTypes;
    private PortTypeService portTypeService;
    private Coordinator coordinator;

    /**
     * Esta clase representa una red de equipos y conexiones.
     * Proporciona servicios para añadir, eliminar y actualizar equipos y conexiones en la red.
     */
    public Web() {
        super();
        setNombre("RedLocal");
        this.wireTypes = new HashMap<>();
        this.wireTypeService = new WireTypeServiceImpl();
        wireTypes.putAll(wireTypeService.searchAll());
        this.portTypes = new HashMap<>();
        this.portTypeService = new PortTypeServiceImpl();
        portTypes.putAll(portTypeService.searchAll());
        this.equipmentTypes = new HashMap<>();
        this.equipmentTypeService = new EquipmentTypeServiceImpl();
        equipmentTypes.putAll(equipmentTypeService.searchAll());
        this.locations = new HashMap<>();
        this.locationService = new LocationServiceImpl();
        locations.putAll(locationService.searchAll());
        this.hardware = new HashMap<>();
        this.equipmentService = new EquipmentServiceImpl();
        hardware.putAll(equipmentService.searchAll());
        this.connections = new ArrayList<>();
        this.connectionService = new ConnectionServiceImpl();
        connections.addAll(connectionService.searchAll());
    }

    public Web(String nombre) {
        super();
        this.nombre = nombre;
        this.hardware = new HashMap<>();
        this.connections = new ArrayList<>();
        this.locations = new HashMap<>();
        this.wireTypes = new HashMap<>();
        this.portTypes = new HashMap<>();
        this.equipmentTypes = new HashMap<>();
        coordinator = new Coordinator();
    }

    /**
     * Devuelve una instancia única de la clase Web.
     *
     * @return instancia de la clase Web
     */
    public static Web getWeb() {
        if (web == null)
            web = new Web();
        return web;
    }

    /**
     * Obtiene el nombre de la red.
     *
     * @return nombre de la red
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la red.
     *
     * @param nombre el nuevo nombre de la red
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Agrega un nuevo equipo a la red.
     *
     * @param code          el código del equipo
     * @param description   la descripción del equipo
     * @param marca         la marca del equipo
     * @param model         el modelo del equipo
     * @param portType      el tipo de puerto del equipo
     * @param cantidad      la cantidad de puertos
     * @param equipmentType el tipo de equipo
     * @param location      la ubicación del equipo
     * @param status        el estado del equipo
     * @return el equipo agregado
     * @throws AlreadyExistException si el equipo ya existe
     * @throws NotFoundException     si el tipo de puerto o equipo no se encuentra
     */
    public Equipment addEquipment(String code, String description, String marca, String model, PortType portType, int cantidad,
                                  EquipmentType equipmentType, Location location, Boolean status) {
        if (hardware.containsKey(code))
            throw new AlreadyExistException("el equipo ya se encuentra");
        if (!portTypes.containsKey(portType.getCode()))
            throw new NotFoundException("El puerto no se encuentra en la lista");
        if (!equipmentTypes.containsKey(equipmentType.getCode()))
            throw new NotFoundException("El tipo de equipo no se encuentra en la lista");
        Equipment e = new Equipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
        hardware.put(code, e);
        equipmentService.insert(e);
        coordinator.updateTablas();
        return e;
    }

    /**
     * Elimina un equipo de la red.
     *
     * @param e el equipo a eliminar
     * @throws NotFoundException si el equipo no se encuentra
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
        equipmentService.erase(e);
        coordinator.updateTablas();
    }

    /**
     * Actualiza un equipo en la red.
     *
     * @param equipment el equipo a actualizar
     * @throws NotFoundException si el equipo no se encuentra
     */
    public void updateEquipment(Equipment equipment) {
        if (!hardware.containsKey(equipment.getCode()))
            throw new NotFoundException("equipo invalido");
        hardware.replace(equipment.getCode(), equipment);
        equipmentService.update(equipment);
        coordinator.updateTablas();
    }

    /**
     * Busca un equipo en la red.
     *
     * @param equipment el equipo a buscar
     * @return el equipo encontrado o null
     */
    public Equipment searchEquipment(Equipment equipment) {
        return hardware.get(equipment.getCode());
    }

    /**
     * Obtiene el conjunto de hardware en la red.
     *
     * @return el conjunto de hardware
     */
    public HashMap<String, Equipment> getHardware() {
        return hardware;
    }

    /**
     * Establece el conjunto de hardware en la red.
     *
     * @param hardware el nuevo conjunto de hardware
     */
    public void setHardware(HashMap<String, Equipment> hardware) {
        this.hardware = hardware;
    }

    /**
     * Agrega una nueva conexión a la red.
     *
     * @param port1 el primer puerto de la conexión
     * @param port2 el segundo puerto de la conexión
     * @param wire  el tipo de cable de la conexión
     * @return la conexión agregada
     * @throws NotFoundException     si algún equipo no se encuentra
     * @throws AlreadyExistException si la conexión ya existe
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
        connectionService.insert(connection);
        coordinator.updateTablas();
        return connection;
    }

    /**
     * Elimina una conexión de la red.
     *
     * @param connection la conexión a eliminar
     * @throws NotFoundException si la conexión no se encuentra
     */
    public void eraseConnection(Connection connection) {
        // Verificar si la conexión existe
        if (!connections.contains(connection))
            throw new NotFoundException("La conexión no se encuentra.");

        // Eliminar la conexión de la lista
        connection.getPort1().setInUse(false);
        connection.getPort2().setInUse(false);
    }


    /**
     * Actualiza una conexión en la red.
     *
     * @param connection la conexión a actualizar
     * @throws NotFoundException si la conexión no se encuentra
     */
    public void updateConnection(Connection connection) {
        if (!connections.contains(connection))
            throw new NotFoundException("Conexion no entrada");

        int index = connections.indexOf(connection);
        connections.set(index, connection);
        connectionService.update(connection);
        coordinator.updateTablas();
    }

    /**
     * Busca una conexión en la red.
     *
     * @param connection la conexión a buscar
     * @return la conexión encontrada
     */
    public Connection searchConnection(Connection connection) {
        return connections.get(connections.indexOf(connection));
    }

    /**
     * Obtiene la lista de conexiones en la red.
     *
     * @return la lista de conexiones
     */
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    /**
     * Establece la lista de conexiones en la red.
     *
     * @param connections la nueva lista de conexiones
     */
    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
    }

    /**
     * Agrega una nueva localización a la red.
     *
     * @param code        el código de la localización
     * @param description la descripción de la localización
     * @return la localización agregada
     * @throws AlreadyExistException si la localización ya existe
     */
    public Location addLocation(String code, String description) {
        if (locations.containsKey(code)) {
            JOptionPane.showMessageDialog(null, "La localizacion ya se encuentra", "Error de duplicacion", JOptionPane.INFORMATION_MESSAGE);
            throw new AlreadyExistException("la localizacion ya se encuentra");

        }
        Location l = new Location(code, description);
        locations.put(code, l);
        locationService.insert(l);
        coordinator.updateTablas();
        return l;
    }

    /**
     * Elimina una localización de la red.
     *
     * @param l la localización a eliminar
     */
    public void eraseLocation(Location l) {
        if (!locations.containsKey(l.getCode()))
            JOptionPane.showMessageDialog(null, "Error de locacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
        List<String> codes = new ArrayList<>();
        for (Equipment e : hardware.values()) {
            if (e.getLocation().equals(l))
                codes.add(e.getCode());
        }
        if (!codes.isEmpty())
            JOptionPane.showMessageDialog(null, "Hay equipos que dependen de la ubicacion", "Error de dependencia", JOptionPane.INFORMATION_MESSAGE);
        locations.remove(l.getCode(), l);
        locationService.erase(l);
        coordinator.updateTablas();
    }

    /**
     * Actualiza una localización en la red.
     *
     * @param l la localización a actualizar
     * @throws NotFoundException si la localización no se encuentra
     */
    public void updateLocation(Location l) {
        if (!locations.containsKey(l.getCode()))
            throw new NotFoundException("la ubicacion no se pudo encontrar");

        locations.replace(l.getCode(), l);
        locationService.update(l);
        coordinator.updateTablas();
    }

    /**
     * Busca una localización en la red.
     *
     * @param location la localización a buscar
     * @return la localización encontrada
     */
    public Location searchLocation(Location location) {
        return locations.get(location.getCode());
    }

    /**
     * Obtiene el conjunto de localizaciones en la red.
     *
     * @return el conjunto de localizaciones
     */
    public HashMap<String, Location> getLocations() {
        return locations;
    }

    /**
     * Establece el conjunto de localizaciones en la red.
     *
     * @param locations el nuevo conjunto de localizaciones
     */
    public void setLocations(HashMap<String, Location> locations) {
        this.locations = locations;
    }

    /**
     * Agrega un nuevo tipo de cable a la red.
     *
     * @param code        el código del tipo de cable
     * @param description la descripción del tipo de cable
     * @param speed       la velocidad del tipo de cable
     * @return el tipo de cable agregado
     * @throws AlreadyExistException si el tipo de cable ya existe
     */
    public WireType addWire(String code, String description, int speed) {
        if (wireTypes.containsKey(code))
            throw new AlreadyExistException("el tipo de cable ya existe");
        WireType w = new WireType(code, description, speed);
        wireTypes.put(code, w);
        wireTypeService.insert(w);
        coordinator.updateTablas();
        return w;
    }

    /**
     * Elimina un tipo de cable de la red.
     *
     * @param w el tipo de cable a eliminar
     * @throws NotFoundException si el tipo de cable no se encuentra
     */
    public void eraseWire(WireType w) {
        if (!wireTypes.containsKey(w.getCode()))
            throw new NotFoundException("El cable no se encuentra");
        List<String> codes = new ArrayList<>();
        for (Connection connection : connections) {
            if (connection.getWire().equals(w))
                codes.add(connection.getPort1().getEquipment().getCode() + " <-> " + connection.getPort2().getEquipment().getCode());
        }
        if (!codes.isEmpty())
            throw new IllegalStateException("las siguientes conexiones tienen ese tipo de cable" + codes);
        wireTypes.remove(w.getCode(), w);
        wireTypeService.erase(w);
        coordinator.updateTablas();
    }


    /**
     * Actualiza un tipo de cable en la red.
     *
     * @param wireType el tipo de cable a actualizar
     * @throws NotFoundException si el tipo de cable no se encuentra
     */
    public void updateWire(WireType wireType) {
        if (wireTypes.containsKey(wireType.getCode()))
            throw new NotFoundException("Tipo de cable no encontrado");

        wireTypes.replace(wireType.getCode(), wireType);
        wireTypeService.update(wireType);
        coordinator.updateTablas();
    }

    /**
     * Busca un tipo de cable en la red.
     *
     * @param wireType el tipo de cable a buscar
     * @return el tipo de cable encontrado
     */
    public WireType searchWire(WireType wireType) {
        return wireTypes.get(wireType.getCode());
    }

    /**
     * Obtiene el conjunto de tipos de cable en la red.
     *
     * @return el conjunto de tipos de cable
     */
    public HashMap<String, WireType> getWireTypes() {
        return wireTypes;
    }


    /**
     * Agrega un nuevo tipo de puerto a la red.
     *
     * @param code        el código del tipo de puerto
     * @param description la descripción del tipo de puerto
     * @param speed       la velocidad del tipo de puerto
     * @return el tipo de puerto agregado
     * @throws AlreadyExistException si el tipo de puerto ya existe
     */
    public PortType addPort(String code, String description, int speed) {
        if (portTypes.containsKey(code))
            throw new AlreadyExistException("el tipo de puerto ya se encuentra");
        PortType p = new PortType(code, description, speed);
        portTypes.put(code, p);
        portTypeService.insert(p);
        coordinator.updateTablas();
        return p;
    }


    /**
     * Elimina un tipo de puerto de la red.
     *
     * @param portType el tipo de puerto a eliminar
     * @throws NotFoundException     si el tipo de puerto no se encuentra
     * @throws IllegalStateException si hay equipos que usan ese tipo de puerto
     */
    public void erasePort(PortType portType) {
        if (!portTypes.containsKey(portType.getCode())) {
            throw new NotFoundException("El puerto no se encuentra");
        }
        List<String> codes = new ArrayList<>();
        for (Equipment e : hardware.values()) {
            if (e.getAllPortsTypes().containsKey(portType)) {
                codes.add(e.getCode());
            }
        }
        if (!codes.isEmpty()) {
            throw new IllegalStateException("Hay equipos que usan ese tipo de puertos: " + codes);
        }
        portTypes.remove(portType.getCode(), portType);
        portTypeService.erase(portType);
        coordinator.updateTablas();
    }

    /**
     * Actualiza un tipo de puerto en la red.
     *
     * @param portType el tipo de puerto a actualizar
     * @throws NotFoundException si el tipo de puerto no se encuentra
     */
    public void updatePort(PortType portType) {
        if (portTypes.containsKey(portType.getCode()))
            throw new NotFoundException("Tipo de puerto no encontrado");

        portTypes.replace(portType.getCode(), portType);
        portTypeService.update(portType);
        coordinator.updateTablas();
    }

    public PortType searchPortType(PortType portType) {
        return portTypes.get(portType.getCode());
    }

    public HashMap<String, PortType> getPortTypes() {
        return portTypes;
    }


    /**
     * Agrega un nuevo equipo a la red.
     *
     * @param code        el código del tipo de equipo
     * @param description la descripción del tipo de equipo
     * @return el tipo de equipo agregado
     * @throws AlreadyExistException si el tipo de equipo ya existe
     */
    public EquipmentType addEquipmentType(String code, String description) {
        if (equipmentTypes.containsKey(code))
            throw new AlreadyExistException("el tipo de equipo ya existe");
        EquipmentType e = new EquipmentType(code, description);
        equipmentTypes.put(code, e);
        equipmentTypeService.insert(e);
        coordinator.updateTablas();
        return e;
    }

    /**
     * Elimina un tipo de equipo de la red.
     *
     * @param equipmentType el tipo de equipo a eliminar
     * @throws NotFoundException     si el tipo de equipo no se encuentra
     * @throws IllegalStateException si hay equipos que usan ese tipo de equipo
     */
    public void eraseEquipmentType(EquipmentType equipmentType) {
        if (!equipmentTypes.containsKey(equipmentType.getCode()))
            throw new NotFoundException("tipo de equipo no encontrado");
        List<String> codes = new ArrayList<>();
        for (Equipment equipment : hardware.values()) {
            if (equipment.getEquipmentType().equals(equipmentType))
                codes.add(equipment.getCode());
        }
        if (!codes.isEmpty())
            throw new IllegalStateException("los siguientes equipos poseen este tipo de equipo, en la red" + codes);

        equipmentTypes.remove(equipmentType.getCode(), equipmentType);
        equipmentTypeService.erase(equipmentType);
        coordinator.updateTablas();
    }

    /**
     * Actualiza un tipo de equipo en la red.
     *
     * @param equipmentType el tipo de equipo a actualizar
     * @throws NotFoundException si el tipo de equipo no se encuentra
     */
    public void updateEquipmentType(EquipmentType equipmentType) {
        if (equipmentTypes.containsKey(equipmentType.getCode()))
            throw new NotFoundException("No se pudo encontrar el tipo de equipo");

        equipmentTypes.replace(equipmentType.getCode(), equipmentType);
        equipmentTypeService.update(equipmentType);
        coordinator.updateTablas();
    }

    /**
     * Busca un tipo de equipo en la red.
     *
     * @param equipmentType el tipo de equipo a buscar
     * @return el tipo de equipo encontrado
     */
    public EquipmentType searchEquipmentType(EquipmentType equipmentType) {
        return equipmentTypes.get(equipmentType.getCode());
    }

    /**
     * Obtiene el conjunto de tipos de equipos en la red.
     *
     * @return el conjunto de tipos de equipos
     */
    public HashMap<Object, EquipmentType> getEquipmentTypes() {
        return equipmentTypes;
    }


    /**
     * Se encarga de establecer un Coordinador.
     *
     * @param coordinator el coordinador a establecer
     */
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Obtiene el Coordinador actual.
     *
     * @return el coordinador actual
     */
    public Coordinator getCoordinator() {
        return coordinator;
    }

    /**
     * Retorna una representación en formato string de la aplicación Web.
     *
     * @return una representación en formato string de la web
     */
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
