package org.isfpp.logica;

import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.Web;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Utils {

    private static Graph<Equipment, Connection> graph;

    /**
     * Crea un grafo no dirigido a partir de los equipos y las conexiones proporcionadas por el objeto {@code Web}.
     *
     * Este método toma un objeto {@code Web}, que contiene un mapa de equipos de hardware y una lista de conexiones,
     * y construye un grafo no dirigido. Cada equipo se agrega como un vértice en el grafo y cada conexión se agrega
     * como una arista entre los equipos correspondientes.
     *
     * @param web El objeto {@code Web} que contiene la información de hardware y conexiones.
     * @throws IllegalArgumentException Si se intenta agregar una conexión entre el mismo equipo o si hay una
     *                                  conexión duplicada en el grafo.
     */
    public void createGraph(Web web) {
        HashMap<String, Equipment> hardware = web.getHardware();
        ArrayList<Connection> connections = web.getConections();
        // Crear un grafo no dirigido
        this.graph = new SimpleGraph<>(Connection.class);
        for (Equipment valor : hardware.values()) {
            this.graph.addVertex(valor);
        }
        for (Connection c : connections) {
            Equipment sourceNode = c.getEquipment1();
            Equipment targetNode = c.getEquipment2();

            if (sourceNode.equals(targetNode)) throw new IllegalArgumentException("son el mismo equipo");
            if (this.graph.containsEdge(sourceNode, targetNode))
                this.graph.addEdge(sourceNode, targetNode, c);

        }
    }

    /**
     * Encuentra el camino más corto entre dos equipos utilizando el algoritmo de Dijkstra.
     *
     * Este método toma dos objetos {@code Equipment} y verifica si son válidos y están activos.
     * Luego, construye un grafo temporal con los equipos activos y sus conexiones.
     * Utiliza el algoritmo de Dijkstra para calcular y devolver la lista de aristas que forman
     * el camino más corto entre los dos equipos especificados.
     *
     * @param e1 El primer equipo de origen para el que se desea encontrar el camino.
     * @param e2 El segundo equipo de destino para el que se desea encontrar el camino.
     * @return Una lista de aristas ({@code DefaultWeightedEdge}) que representan el camino más corto
     *         entre {@code e1} y {@code e2}. Puede devolver {@code null} si no se encuentra
     *         un camino o si alguno de los equipos no está activo.
     * @throws IllegalArgumentException Si {@code e1} o {@code e2} son {@code null}, si son iguales,
     *                                  o si alguno de los equipos no está activo.
     */
    //mirar parte de minSpeed
    public List<DefaultWeightedEdge> traceroute(Equipment e1, Equipment e2) throws IllegalArgumentException {

        if (e1 == null || e2 == null) {
            throw new IllegalArgumentException("Equipo invalido");
        }
        if (e1.equals(e2)) {
            throw new IllegalArgumentException("Equipo duplicado");
        }

        if (!e1.isStatus() || !e2.isStatus()) {
            throw new IllegalArgumentException("Uno de los equipos no está activo \n" + e1.getCode() + e1.isStatus() + "\n" + e2.getCode() + e2.isStatus());// Devolver null si alguno de los equipos no está activo
        }

        // Crear un grafo temporal que contendrá solo los equipos activos
        SimpleWeightedGraph<Equipment, DefaultWeightedEdge> graphTemp = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        HashMap<String, Equipment> equipmentMap = new HashMap<>();

        // Insertar vértices (equipos) activos en el grafo temporal
        for (Equipment e : graphTemp.vertexSet()) {
            if (e.isStatus()) {
                graphTemp.addVertex(e);
                equipmentMap.put(e.getCode(), e);
            }
        }

        // Insertar aristas con conexiones activas
        for (Connection edge : graph.edgeSet()) {
            Equipment source = graph.getEdgeSource(edge);
            Equipment target = graph.getEdgeTarget(edge);

            if (equipmentMap.containsKey(source.getCode()) && equipmentMap.containsKey(source.getCode())) {
                // Calcular la velocidad mínima entre los puertos de los equipos y la velocidad del cable
                int edgeValue = getMinSpeed(source.getAllPortsTypes().keySet(), target.getAllPortsTypes().keySet(), edge.getWire().getSpeed());

                //int edgeValue = Math.min(minSpeedPort1, Math.min(minSpeedPort1, minSpeedPort2))
                DefaultWeightedEdge newEdge = graphTemp.addEdge(source, target);
                // Asignar el peso correspondiente a la arista
                graphTemp.setEdgeWeight(newEdge, edgeValue);
            }
        }
        // Aplicar el algoritmo de Dijkstra para encontrar el camino más corto entre dos equipos
        DijkstraShortestPath<Equipment, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(graphTemp);

        // Encontrar el camino más corto desde Router1 a PC1
        return dijkstraAlg.getPath(e1, e2).getEdgeList();
    }
    /**
     * Calcula la velocidad mínima de conexión entre dos equipos considerando
     * la velocidad de los puertos y la velocidad del cable.
     *
     * Este método recibe dos listas de tipos de puertos (representando las velocidades de
     * los puertos de dos equipos) y la velocidad de un cable. Compara las velocidades
     * de los puertos para determinar cuál es la más baja y luego la compara con la
     * velocidad del cable, devolviendo el valor más bajo de los tres.
     *
     * @param ports1 Una lista de objetos {@code PortType} que representan los puertos
     *                del primer equipo.
     * @param ports2 Una lista de objetos {@code PortType} que representan los puertos
     *                del segundo equipo.
     * @param wireSpeed La velocidad del cable de conexión entre los equipos.
     * @return La velocidad mínima de conexión entre los dos equipos, que es la velocidad
     *         más baja de los puertos y la velocidad del cable.
     */
    //mirar
    public static int getMinSpeed(Set<PortType> ports1, Set<PortType> ports2, int wireSpeed) {
        // Encuentra el puerto más lento de ambos equipos
        ArrayList<Integer> port1Speed = new ArrayList<>();
        ArrayList<Integer> port2Speed = new ArrayList<>();

        for (PortType p : ports1) {
            port1Speed.add(p.getSpeed());
        }
        for (PortType p : ports2) {
            port2Speed.add(p.getSpeed());
        }

        int minSpeedPort1 = port1Speed.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
        int minSpeedPort2 = port2Speed.stream().min(Integer::compare).orElse(Integer.MAX_VALUE);

        int speedBetweenEquipments = Math.min(minSpeedPort1, minSpeedPort2);

        // Finalmente, compara con la velocidad del cable
        return Math.min(speedBetweenEquipments, wireSpeed);
    }

}
