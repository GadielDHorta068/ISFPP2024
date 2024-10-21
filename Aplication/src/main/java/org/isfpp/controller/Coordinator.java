package org.isfpp.controller;

import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.interfaz.panelesPrincipal.DesplegableComponent;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * La clase Coordinator es responsable de gestionar diversas funcionalidades relacionadas con la web y sus componentes.
 */
public class Coordinator {
    private Web web;
    private Utils utils;
    private MainMenu mainMenu;
    private Object selectedItem;
    private final List<DesplegableComponent> tablas = new ArrayList<>();

    /**
     * Constructor por defecto de la clase Coordinator.
     */
    public Coordinator() {
        this.web = null;
        this.utils = null;
        this.mainMenu = null;
        this.selectedItem = null;
    }

    /**
     * Obtiene el objeto Utils asociado.
     *
     * @return el objeto Utils.
     */
    public Utils getUtils() {
        return utils;
    }

    /**
     * Establece el objeto Utils.
     *
     * @param utils el objeto Utils a establecer.
     */
    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    /**
     * Obtiene el objeto Web asociado.
     *
     * @return el objeto Web.
     */
    public Web getWeb() {
        return web;
    }

    /**
     * Establece el objeto Web.
     *
     * @param web el objeto Web a establecer.
     */
    public void setWeb(Web web) {
        this.web = web;
    }

    /**
     * Obtiene el hardware de la red.
     *
     * @return un HashMap con el hardware de la red.
     */
    public HashMap<String, Equipment> getHardware() {
        return web.getHardware();
    }

    /**
     * Establece el hardware de la red.
     *
     * @param hardware un HashMap con el hardware a establecer.
     */
    public void setHardware(HashMap<String, Equipment> hardware) {
        web.setHardware(hardware);
    }

    /**
     * Obtiene las conexiones de la red.
     *
     * @return un ArrayList con las conexiones de la red.
     */
    public ArrayList<Connection> getConnections() {
        return web.getConnections();
    }

    /**
     * Establece las conexiones de la red.
     *
     * @param conections un ArrayList con las conexiones a establecer.
     */
    public void setConnections(ArrayList<Connection> conections) {
        this.web.setConnections(conections);
    }

    /**
     * Obtiene las ubicaciones de la red.
     *
     * @return un HashMap con las ubicaciones de la red.
     */
    public HashMap<String, Location> getLocations() {
        return web.getLocations();
    }

    /**
     * Establece las ubicaciones de la red.
     *
     * @param locations un HashMap con las ubicaciones a establecer.
     */
    public void setLocations(HashMap<String, Location> locations) {
        this.web.setLocations(locations);
    }

    /**
     * Obtiene el nombre de la red.
     *
     * @return el nombre de la red.
     */
    public String getNombre() {
        return web.getNombre();
    }

    /**
     * Establece el nombre de la red.
     *
     * @param nombre el nombre a establecer.
     */
    public void setNombre(String nombre) {
        web.setNombre(nombre);
    }

    /**
     * Agrega una nueva ubicación a la red.
     *
     * @param code        el código de la ubicación.
     * @param description la descripción de la ubicación.
     * @return la ubicación agregada.
     */
    public Location addLocation(String code, String description) {
        return web.addLocation(code, description);
    }

    /**
     * Elimina una ubicación de la red.
     *
     * @param l la ubicación a eliminar.
     */
    public void eraseLocation(Location l) {
        web.eraseLocation(l);
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
        return web.addPort(code, description, speed);
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
        return web.addWire(code, description, speed);
    }

    /**
     * Agrega un nuevo tipo de equipo a la red.
     *
     * @param code        el código del tipo de equipo.
     * @param description la descripción del tipo de equipo.
     * @return el tipo de equipo agregado.
     */
    public EquipmentType addEquipmentType(String code, String description) {
        return web.addEquipmentType(code, description);
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
        return web.addEquipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
    }

    /**
     * Elimina un equipo de la red.
     *
     * @param e el equipo a eliminar.
     */
    public void eraseEquipment(Equipment e) {
        web.eraseEquipment(e);
    }

    /**
     * Elimina un cable de la red.
     *
     * @param w el cable a eliminar.
     */
    public void eraseWire(WireType w) {
        web.eraseWire(w);
    }

    /**
     * Elimina un puerto de la red.
     *
     * @param portType el puerto a eliminar.
     */
    public void erasePort(PortType portType) {
        web.erasePort(portType);
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
        return web.addConnection(port1, port2, wire);
    }

    /**
     * Elimina una conexión de la red.
     *
     * @param connection la conexión a eliminar.
     */
    public void eraseConnection(Connection connection) {
        web.eraseConnection(connection);
    }

    /**
     * Realiza un ping a todos los equipos de la red.
     *
     * @return un HashMap con los resultados del ping.
     */
    public HashMap<Equipment, Boolean> ping() {
        return utils.ping();
    }

    /**
     * Realiza un ping a un equipo específico.
     *
     * @param e1 el equipo a hacer ping.
     * @return el resultado del ping.
     */
    public boolean ping(Equipment e1) {
        return utils.ping(e1);
    }

    /**
     * Detecta problemas de conectividad a partir de un equipo inicial.
     *
     * @param startNode el equipo inicial.
     * @return una lista de equipos con problemas de conectividad.
     */
    public List<Equipment> detectConnectivityIssues(Equipment startNode) {
        return utils.detectConnectivityIssues(startNode);
    }

    /**
     * Realiza un traceroute entre dos equipos en la red.
     *
     * @param e1 el primer equipo.
     * @param e2 el segundo equipo.
     * @return el camino del traceroute.
     */
    public GraphPath<Equipment, DefaultWeightedEdge> traceroute(Equipment e1, Equipment e2) {
        return utils.traceroute(e1, e2);
    }

    /**
     * Obtiene el grafo de la red.
     *
     * @return el grafo de la red.
     */
    public Graph<Equipment, Connection> getGraph() {
        return utils.getGraph();
    }

    /**
     * Establece el grafo de la red.
     *
     * @param graph el grafo a establecer.
     */
    public void setGraph(Graph<Equipment, Connection> graph) {
        this.utils.setGraph(graph);
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
        return utils.scanIP(ip);
    }

    /**
     * Carga los datos de la web.
     *
     * @param web la web cuyos datos se van a cargar.
     */
    public void LoadData(Web web) {
        this.utils.LoadData(web);
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
    public void updateTablas() {
        for (DesplegableComponent tabla : tablas) {
            tabla.updateTable();
            LoadData(web);
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

    public void saveALlData(String fileDirectory) throws FileNotFoundException {
        File dataDir = new File(fileDirectory, "data");
        if (!dataDir.exists()) {
            throw new FileNotFoundException("La carpeta 'data' no fue encontrada en: " + dataDir.getAbsolutePath());
        }

        insertAllInto(fileDirectory);
        //return cargarRed(name, equipmentFile, connectionFile, locationFile, portTypeFile, wireTypeFile, equipmentTypeFile);
    }

    public void LoadALlData(String fileDirectory) throws FileNotFoundException {
        File dataDir = new File(fileDirectory, "data");
        if (!dataDir.exists()) {
            throw new FileNotFoundException("La carpeta 'data' no fue encontrada en: " + dataDir.getAbsolutePath());
        }

        searchAllOf(fileDirectory);
        //return cargarRed(name, equipmentFile, connectionFile, locationFile, portTypeFile, wireTypeFile, equipmentTypeFile);
    }



    //metodos searchAllOf de Web, (lectura)
    public void searchAllLocarionOf(String directory){
        web.addAllLocationOf(directory);
    }

    public void searchAllWireTypeOf(String directory){
        web.addAllWiretypeOf(directory);
    }

    public void searchAllEquipmentType(String directory){
        web.addAllEquipmentTypeOf(directory);
    }

    public void searchAllPortType(String directory){
        web.addAllPortTypeOf(directory);
    }

    public void searchAllEquipmentOf(String diretory){
        web.addAllEquipmentOf(diretory);
    }

    public void searchAllConnectionOf(String directory){
        web.addAllConnectionOf(directory);
    }

    public void searchAllOf(String directory){
        web.addAllLocationOf(directory);
        web.addAllWiretypeOf(directory);
        web.addAllEquipmentTypeOf(directory);
        web.addAllPortTypeOf(directory);
        web.addAllEquipmentOf(directory);
        web.addAllConnectionOf(directory);
    }

    //metodos insertALl de Web, (escritor)
    public void insertAllEquipmentInto(String directory){
        web.insertAllWireTypeInto(directory);
    }

    public void insertAllConnectionInto(String directory){
        web.insertAllConnectionInto(directory);
    }

    public void insertAllLocationInto(String directory){
        web.insertAllLocationInto(directory);
    }

    public void insertAllPortTypeInto(String directory){
        web.insertAllPortTypeInto(directory);
    }

    public void insertAllWireTypeInto(String directory){
        web.insertAllPortTypeInto(directory);
    }

    public void insertAllEquipmentTypeInto(String directory){
        web.insertAllEquipmentTypeInto(directory);
    }

    public void insertAllInto(String directory){
        insertAllConnectionInto(directory);
        insertAllEquipmentInto(directory);
        insertAllEquipmentTypeInto(directory);
        insertAllLocationInto(directory);
        insertAllPortTypeInto(directory);
        insertAllWireTypeInto(directory);
    }
}
