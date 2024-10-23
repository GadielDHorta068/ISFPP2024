package org.isfpp.controller;

import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.interfaz.panelesPrincipal.DesplegableComponent;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * La clase Coordinator es responsable de gestionar diversas funcionalidades relacionadas con la web y sus componentes.
 */
public class Coordinator {
    private LAN LAN;
    private CalculoGraph calculoGraph;
    private MainMenu mainMenu;
    private Object selectedItem;
    private final List<DesplegableComponent> tablas = new ArrayList<>();

    /**
     * Constructor por defecto de la clase Coordinator.
     */
    public Coordinator() {
        this.LAN = null;
        this.calculoGraph = null;
        this.mainMenu = null;
        this.selectedItem = null;
    }

    /**
     * Obtiene el objeto Utils asociado.
     *
     * @return el objeto Utils.
     */
    public CalculoGraph getUtils() {
        return calculoGraph;
    }

    /**
     * Establece el objeto Utils.
     *
     * @param calculoGraph el objeto Utils a establecer.
     */
    public void setUtils(CalculoGraph calculoGraph) {
        this.calculoGraph = calculoGraph;
    }

    /**
     * Obtiene el objeto Web asociado.
     *
     * @return el objeto Web.
     */
    public LAN getWeb() {
        return LAN;
    }

    /**
     * Establece el objeto Web.
     *
     * @param LAN el objeto Web a establecer.
     */
    public void setWeb(LAN LAN) {
        this.LAN = LAN;
    }

    /**
     * Obtiene el hardware de la red.
     *
     * @return un HashMap con el hardware de la red.
     */
    public HashMap<String, Equipment> getHardware() {
        return LAN.getHardware();
    }

    /**
     * Establece el hardware de la red.
     *
     * @param hardware un HashMap con el hardware a establecer.
     */
    public void setHardware(HashMap<String, Equipment> hardware) {
        LAN.setHardware(hardware);
    }

    /**
     * Obtiene las conexiones de la red.
     *
     * @return un ArrayList con las conexiones de la red.
     */
    public ArrayList<Connection> getConnections() {
        return LAN.getConnections();
    }

    /**
     * Establece las conexiones de la red.
     *
     * @param conections un ArrayList con las conexiones a establecer.
     */
    public void setConnections(ArrayList<Connection> conections) {
        this.LAN.setConnections(conections);
    }

    /**
     * Obtiene las ubicaciones de la red.
     *
     * @return un HashMap con las ubicaciones de la red.
     */
    public HashMap<String, Location> getLocations() {
        return LAN.getLocations();
    }

    /**
     * Establece las ubicaciones de la red.
     *
     * @param locations un HashMap con las ubicaciones a establecer.
     */
    public void setLocations(HashMap<String, Location> locations) {
        this.LAN.setLocations(locations);
    }

    /**
     * Obtiene el nombre de la red.
     *
     * @return el nombre de la red.
     */
    public String getNombre() {
        return LAN.getNombre();
    }

    /**
     * Establece el nombre de la red.
     *
     * @param nombre el nombre a establecer.
     */
    public void setNombre(String nombre) {
        LAN.setNombre(nombre);
    }

    /**
     * Agrega una nueva ubicación a la red.
     *
     * @param code        el código de la ubicación.
     * @param description la descripción de la ubicación.
     * @return la ubicación agregada.
     */
    public Location addLocation(String code, String description) {
        return LAN.addLocation(code, description);
    }

    /**
     * Elimina una ubicación de la red.
     *
     * @param l la ubicación a eliminar.
     */
    public void eraseLocation(Location l) {
        LAN.eraseLocation(l);
    }

    /**
     * Agrega un nuevo puerto a la red.
     *
     * @param code        el código del puerto.
     * @param description la descripción del puerto.
     * @param speed       la velocidad del puerto.
     * @return el puerto agregado.
     */
    public PortType addPort(String code, String description, int speed) {
        return LAN.addPort(code, description, speed);
    }

    /**
     * Agrega un nuevo cable a la red.
     *
     * @param code        el código del cable.
     * @param description la descripción del cable.
     * @param speed       la velocidad del cable.
     * @return el cable agregado.
     */
    public WireType addWire(String code, String description, int speed) {
        return LAN.addWire(code, description, speed);
    }

    /**
     * Agrega un nuevo tipo de equipo a la red.
     *
     * @param code        el código del tipo de equipo.
     * @param description la descripción del tipo de equipo.
     * @return el tipo de equipo agregado.
     */
    public EquipmentType addEquipmentType(String code, String description) {
        return LAN.addEquipmentType(code, description);
    }

    /**
     * Agrega un nuevo equipo a la red.
     *
     * @param code           el código del equipo.
     * @param description    la descripción del equipo.
     * @param marca          la marca del equipo.
     * @param model          el modelo del equipo.
     * @param portType       el tipo de puerto del equipo.
     * @param cantidad       la cantidad de equipos.
     * @param equipmentType  el tipo de equipo.
     * @param location       la ubicación del equipo.
     * @param status         el estado del equipo.
     * @return el equipo agregado.
     */
    public Equipment addEquipment(String code, String description, String marca, String model, PortType portType, int cantidad,
                                  EquipmentType equipmentType, Location location, Boolean status) {
        return LAN.addEquipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
    }

    /**
     * Elimina un equipo de la red.
     *
     * @param e el equipo a eliminar.
     */
    public void eraseEquipment(Equipment e) {
        LAN.eraseEquipment(e);
    }

    /**
     * Elimina un cable de la red.
     *
     * @param w el cable a eliminar.
     */
    public void eraseWire(WireType w) {
        LAN.eraseWire(w);
    }

    /**
     * Elimina un puerto de la red.
     *
     * @param portType el puerto a eliminar.
     */
    public void erasePort(PortType portType) {
        LAN.erasePort(portType);
    }

    /**
     * Agrega una conexión entre dos puertos en la red.
     *
     * @param port1 el primer puerto.
     * @param port2 el segundo puerto.
     * @param wire  el tipo de cable.
     * @return la conexión agregada.
     */
    public Connection addConnection(Port port1, Port port2, WireType wire) {
        return LAN.addConnection(port1, port2, wire);
    }

    /**
     * Elimina una conexión de la red.
     *
     * @param connection la conexión a eliminar.
     */
    public void eraseConnection(Connection connection) {
        LAN.eraseConnection(connection);
    }

    /**
     * Realiza un ping a todos los equipos de la red.
     *
     * @return un HashMap con los resultados del ping.
     */
    public HashMap<Equipment, Boolean> ping() {
        return calculoGraph.ping();
    }

    /**
     * Realiza un ping a un equipo específico.
     *
     * @param e1 el equipo a hacer ping.
     * @return el resultado del ping.
     */
    public boolean ping(Equipment e1) {
        return calculoGraph.ping(e1);
    }

    /**
     * Detecta problemas de conectividad a partir de un equipo inicial.
     *
     * @param startNode el equipo inicial.
     * @return una lista de equipos con problemas de conectividad.
     */
    public List<Equipment> detectConnectivityIssues(Equipment startNode) {
        return calculoGraph.detectConnectivityIssues(startNode);
    }

    /**
     * Realiza un traceroute entre dos equipos en la red.
     *
     * @param e1 el primer equipo.
     * @param e2 el segundo equipo.
     * @return el camino del traceroute.
     */
    public GraphPath<Equipment, DefaultWeightedEdge> traceroute(Equipment e1, Equipment e2) {
        return calculoGraph.traceroute(e1, e2);
    }

    /**
     * Obtiene el grafo de la red.
     *
     * @return el grafo de la red.
     */
    public Graph<Equipment, Connection> getGraph() {
        return calculoGraph.getGraph();
    }

    /**
     * Establece el grafo de la red.
     *
     * @param graph el grafo a establecer.
     */
    public void setGraph(Graph<Equipment, Connection> graph) {
        this.calculoGraph.setGraph(graph);
    }

    /**
     * Obtiene el menú principal.
     *
     * @return el menú principal.
     */
    public MainMenu getMainMenu() {
        return mainMenu;
    }

    /**
     * Establece el menú principal.
     *
     * @param mainMenu el menú principal a establecer.
     */
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * Escanea una dirección IP.
     *
     * @param ip la dirección IP a escanear.
     * @return una lista de resultados del escaneo.
     */
    public List<String> scanIP(String ip) {
        return calculoGraph.scanIP(ip);
    }

    /**
     * Carga los datos de la web.
     *
     * @param LAN la web cuyos datos se van a cargar.
     */
    public void LoadData(LAN LAN) {
        this.calculoGraph.LoadData(LAN);
    }

    /**
     * Obtiene el elemento seleccionado.
     *
     * @return el elemento seleccionado.
     */
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * Establece el elemento seleccionado.
     *
     * @param selectedItem el elemento a establecer.
     */
    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
    }

    /**
     * Actualiza las tablas de la interfaz.
     */

    public void updateTablas(LAN w1) {
        this.LoadData(w1);
        for (DesplegableComponent tabla : tablas) {
            tabla.updateTable();
        }
    }


    /**
     * Agrega un componente desplegable a las tablas.
     *
     * @param d el componente desplegable a agregar.
     */
    public void addTabla(DesplegableComponent d) {
        tablas.add(d);
    }
}