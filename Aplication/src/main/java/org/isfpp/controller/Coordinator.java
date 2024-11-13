package org.isfpp.controller;

import org.isfpp.interfaz.panelesPrincipal.MainMenu;
import org.isfpp.interfaz.panelesPrincipal.DesplegableComponent;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.logica.Lan;
import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.util.*;

/**
 * La clase Coordinator es responsable de gestionar diversas funcionalidades relacionadas con la web y sus componentes.
 */
public class Coordinator {
    private Lan lan;
    private CalculoGraph calculoGraph;
    private MainMenu mainMenu;
    private Object selectedItem;
    private final List<DesplegableComponent> tablas = new ArrayList<>();
    private Settings settings;

    /**
     * Constructor por defecto de la clase Coordinator.
     */
    public Coordinator() {
        this.lan = null;
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
    public Lan getWeb() {
        return lan;
    }

    /**
     * Establece el objeto Web.
     *
     * @param lan el objeto Web a establecer.
     */
    public void setWeb(Lan lan) {
        this.lan = lan;
    }

    /**
     * Obtiene el hardware de la red.
     *
     * @return un HashMap con el hardware de la red.
     */
    public HashMap<String, Equipment> getHardware() {
        return lan.getHardware();
    }

    /**
     * Establece el hardware de la red.
     *
     * @param hardware un HashMap con el hardware a establecer.
     */
    public void setHardware(HashMap<String, Equipment> hardware) {
        lan.setHardware(hardware);
    }

    /**
     * Obtiene las conexiones de la red.
     *
     * @return un ArrayList con las conexiones de la red.
     */
    public ArrayList<Connection> getConnections() {
        return lan.getConnections();
    }

    /**
     * Establece las conexiones de la red.
     *
     * @param conections un ArrayList con las conexiones a establecer.
     */
    public void setConnections(ArrayList<Connection> conections) {
        this.lan.setConnections(conections);
    }

    /**
     * Obtiene las ubicaciones de la red.
     *
     * @return un HashMap con las ubicaciones de la red.
     */
    public HashMap<String, Location> getLocations() {
        return lan.getLocations();
    }

    /**
     * Establece las ubicaciones de la red.
     *
     * @param locations un HashMap con las ubicaciones a establecer.
     */
    public void setLocations(HashMap<String, Location> locations) {
        this.lan.setLocations(locations);
    }

    /**
     * Obtiene el nombre de la red.
     *
     * @return el nombre de la red.
     */
    public String getNombre() {
        return lan.getNombre();
    }

    /**
     * Establece el nombre de la red.
     *
     * @param nombre el nombre a establecer.
     */
    public void setNombre(String nombre) {
        lan.setNombre(nombre);
    }

    /**
     * Agrega una nueva ubicación a la red.
     *
     * @param code        el código de la ubicación.
     * @param description la descripción de la ubicación.
     * @return la ubicación agregada.
     */
    public Location addLocation(String code, String description) {
        return lan.addLocation(code, description);
    }

    /**
     * Elimina una ubicación de la red.
     *
     * @param l la ubicación a eliminar.
     */
    public void eraseLocation(Location l) {
        lan.eraseLocation(l);
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
        return lan.addPort(code, description, speed);
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
        return lan.addWire(code, description, speed);
    }

    /**
     * Agrega un nuevo tipo de equipo a la red.
     *
     * @param code        el código del tipo de equipo.
     * @param description la descripción del tipo de equipo.
     * @return el tipo de equipo agregado.
     */
    public EquipmentType addEquipmentType(String code, String description) {
        return lan.addEquipmentType(code, description);
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
        return lan.addEquipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
    }

    /**
     * Elimina un equipo de la red.
     *
     * @param e el equipo a eliminar.
     */
    public void eraseEquipment(Equipment e) {
        lan.eraseEquipment(e);
    }

    /**
     * Elimina un cable de la red.
     *
     * @param w el cable a eliminar.
     */
    public void eraseWire(WireType w) {
        lan.eraseWire(w);
    }

    /**
     * Elimina un puerto de la red.
     *
     * @param portType el puerto a eliminar.
     */
    public void erasePort(PortType portType) {
        lan.erasePort(portType);
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
        return lan.addConnection(port1, port2, wire);
    }

    /**
     * Elimina una conexión de la red.
     *
     * @param connection la conexión a eliminar.
     */
    public void eraseConnection(Connection connection) {
        lan.eraseConnection(connection);
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
     * @param ip1         la dirección IP a escanear.
     * @param ip2         direccion ip hasta donde escanear
     * @param textarea    area de texto a actualizar
     * @param progressBar barra de progreso
     * @return una lista de resultados del escaneo.
     */
    public List<String> scanIP(String ip1, String ip2, JTextArea textarea, JProgressBar progressBar) {
        return calculoGraph.scanIP(ip1,ip2, textarea, progressBar);
    }

    /**
     * Carga los datos de la web.
     *
     * @param lan la web cuyos datos se van a cargar.
     */
    public void LoadData(Lan lan) {
        this.calculoGraph.LoadData(lan);
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
     * Agrega un componente desplegable a las tablas.
     *
     * @param d el componente desplegable a agregar.
     */
    public void addTabla(DesplegableComponent d) {
        tablas.add(d);
    }

    /**
     * Indicar los settings del programa
     * @param settings referencia a la clase settings
     */
    public void setSettings(Settings settings) {this.settings=settings;
    }

    /**
     * Obtener ResourceBoundle de settings
     * @return ResourceBundle
     */
    public ResourceBundle getResourceBundle() {return this.settings.getResourceBundle();
    }

    public void updateConnection(Connection originalConnection, Connection updateConnection) {
        lan.updateConnection(originalConnection, updateConnection);
    }

    public HashMap<Object, EquipmentType> getEquipmentTypes() {return lan.getEquipmentTypes();
    }

    public HashMap<String, PortType> getPortTypes() { return lan.getPortTypes();}

    public HashMap<String, WireType> getWireTypes() { return lan.getWireTypes();
    }

    public void updateEquipment(String codeOriginal, Equipment equipment) {lan.updateEquipment(codeOriginal, equipment);
    }

    public void updateLocation(String codeOriginial, Location location) { lan.updateLocation(codeOriginial, location);
    }

    public void updatePortType(String codeOriginal, PortType port) {lan.updatePortType(codeOriginal, port);
    }
    /**
     * Actualiza las tablas de la interfaz.
     */

    public void updateTablas(Lan l1) {
        this.LoadData(l1);
        for (DesplegableComponent tabla : tablas) {
            tabla.updateTable();
        }

    }

    /**
     * Lectura de las locaciones de la LAN
     * @param directory directorio del archivo
     */
    public void searchAllLocarionOf(String directory){
        lan.addAllLocationOf(directory);
    }

    /**
     * Lectura de todos los tipos de cable de la LAN
     * @param directory directorio del archivo
     */
    public void searchAllWireTypeOf(String directory){
        lan.addAllWiretypeOf(directory);
    }

    /**
     * Lectura de todos los tipos de equipo de la LAN
     * @param directory directorio del archivo
     */
    public void searchAllEquipmentType(String directory){
        lan.addAllEquipmentTypeOf(directory);
    }

    /**
     * Lectura de todos los tipos de puerto de la LAN
     * @param directory directorio del archivo
     */
    public void searchAllPortType(String directory){
        lan.addAllPortTypeOf(directory);
    }

    /**
     * Lectura de todos los equipos en la LAN
     * @param diretory directorio del archivo
     */
    public void searchAllEquipmentOf(String diretory){
        lan.addAllEquipmentOf(diretory);
    }

    /**
     * Lectura de todas las conecciones de la LAN
     * @param directory directorio del archivo
     */
    public void searchAllConnectionOf(String directory){
        lan.addAllConnectionOf(directory);
    }

    /**
     * Leer objetos desde
     * @param directory directorio
     */
    public void searchAllOf(String directory){
        searchAllLocarionOf(directory);
        searchAllWireTypeOf(directory);
        searchAllEquipmentType(directory);
        searchAllPortType(directory);
        searchAllEquipmentOf(directory);
        searchAllConnectionOf(directory);
    }


    /**
     * Escribir todos los equipos en el archivo
     * @param directory directorio del archivo
     */
    public void insertAllEquipmentInto(String directory){
        lan.insertAllWireTypeInto(directory);
    }

    /**
     * Escribir todas las conxiones de la LAN
     * @param directory directorio del archivo
     */
    public void insertAllConnectionInto(String directory){
        lan.insertAllConnectionInto(directory);
    }

    /**
     * Escribir todas las locaciones de la LAN
     * @param directory directorio del archivo
     */
    public void insertAllLocationInto(String directory){
        lan.insertAllLocationInto(directory);
    }

    /**
     * Escribir todos los tipos de puerto
     * @param directory directorio del archivo
     */
    public void insertAllPortTypeInto(String directory){
        lan.insertAllPortTypeInto(directory);
    }

    /**
     * Escribir todos los tipo de cable
     * @param directory directorio del archivo
     */
    public void insertAllWireTypeInto(String directory){
        lan.insertAllPortTypeInto(directory);
    }

    /**
     * Escribir todos los tipos de equipo de la LAN
     * @param directory directorio del archivo
     */
    public void insertAllEquipmentTypeInto(String directory){
        lan.insertAllEquipmentTypeInto(directory);
    }

    /**
     * Escribir todos los elementos de la LAN
     * @param directory directorio del archivo
     */
    public void insertAllInto(String directory){
        insertAllConnectionInto(directory);
        insertAllEquipmentInto(directory);
        insertAllEquipmentTypeInto(directory);
        insertAllLocationInto(directory);
        insertAllPortTypeInto(directory);
        insertAllWireTypeInto(directory);
    }

    /**
     * Obtener nombre de usuario
     * @return String
     */
    public String getUser() {
       return settings.getName();
    }

    /**
     * Obtener los settings
     * @return Settings
     */
    public Settings getSettings(){
        return settings;
    }
}