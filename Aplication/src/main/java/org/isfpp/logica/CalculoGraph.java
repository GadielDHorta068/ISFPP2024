package org.isfpp.logica;

import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.NotFoundException;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.LAN;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.*;
import java.util.stream.IntStream;

public class CalculoGraph {
    private static Graph<Equipment, Connection> graph;
    private Coordinator coordinator;

    public CalculoGraph() {
    }

    /**
     * Crea un grafo no dirigido a partir de los equipos y las conexiones proporcionadas por el objeto {@code Web}.
     * <p>
     * Este método toma un objeto {@code Web}, que contiene un mapa de equipos de hardware y una lista de conexiones,
     * y construye un grafo no dirigido. Cada equipo se agrega como un vértice en el grafo y cada conexión se agrega
     * como una arista entre los equipos correspondientes.
     *
     * @param LAN El objeto {@code Web} que contiene la información de hardware y conexiones.
     * @throws IllegalArgumentException Si se intenta agregar una conexión entre el mismo equipo o si hay una
     *                                  conexión duplicada en el grafo.
     */
    public void LoadData(LAN LAN) {
        HashMap<String, Equipment> hardware = LAN.getHardware();
        ArrayList<Connection> connections = LAN.getConnections();
        // Crear un grafo no dirigido
        graph = new SimpleGraph<>(Connection.class);
        for (Equipment valor : hardware.values()) {
            graph.addVertex(valor);
        }


        for (Connection c : connections) {
            Equipment sourceNode = c.getPort1().getEquipment();
            Equipment targetNode = c.getPort2().getEquipment();

            if (sourceNode.equals(targetNode)) throw new IllegalArgumentException("son el mismo equipo");
            if (!graph.containsEdge(sourceNode, targetNode))
                graph.addEdge(sourceNode, targetNode, c);
        }
        coordinator.setGraph(graph);
        coordinator.setWeb(LAN);
        System.out.println("Actualizado");


    }

    /**
     * Encuentra el camino más corto entre dos equipos utilizando el algoritmo de Dijkstra.
     * <p>
     * Este método toma dos objetos {@code Equipment} y verifica si son válidos y están activos.
     * Luego, construye un grafo temporal con los equipos activos y sus conexiones.
     * Usa el algoritmo de Dijkstra para calcular y devolver la lista de aristas que forman
     * el camino más corto entre los dos equipos especificados.
     *
     * @param e1 El primer equipo de origen para el que se desea encontrar el camino.
     * @param e2 El segundo equipo de destino para el que se desea encontrar el camino.
     * @return Una lista de aristas ({@code DefaultWeightedEdge}) que representan el camino más corto
     * entre {@code e1} y {@code e2}. Puede devolver {@code null} si no se encuentra
     * un camino o si alguno de los equipos no está activo.
     * @throws IllegalArgumentException Si {@code e1} o {@code e2} son {@code null}, si son iguales,
     *                                  o si alguno de los equipos no está activo.
     */
    public GraphPath<Equipment, DefaultWeightedEdge> traceroute(Equipment e1, Equipment e2) throws IllegalArgumentException {

        if (e1 == null || e2 == null) {
            throw new IllegalArgumentException("Equipo inválido");
        }
        if (e1.equals(e2)) {
            throw new IllegalArgumentException("Equipo duplicado");
        }

        if (!e1.isStatus() || !e2.isStatus()) {
            throw new IllegalArgumentException(String.format(
                    "Uno de los equipos no está activo %s %s %s %s",
                    e1.getCode(),
                    e1.isStatus(),
                    e2.getCode(),
                    e2.isStatus()
            ));

        }

        // Crear un grafo temporal que contendrá solo los equipos activos
        SimpleWeightedGraph<Equipment, DefaultWeightedEdge> graphTemp = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Insertar vértices (equipos) activos en el grafo temporal
        for (Equipment e : graph.vertexSet()) {
            if (e.isStatus())
                graphTemp.addVertex(e);
        }

        // Insertar aristas con conexiones activas
        for (Connection edge : graph.edgeSet()) {
            Equipment source = graph.getEdgeSource(edge);
            Equipment target = graph.getEdgeTarget(edge);

            if (graphTemp.containsVertex(source) && graphTemp.containsVertex(target) && !graphTemp.containsEdge(source, target)) {
                // Calcular la velocidad mínima entre los puertos de los equipos y la velocidad del cable
                int edgeValue = getMinSpeed(convertSetToList(source.getAllPortsTypes().keySet()), convertSetToList(target.getAllPortsTypes().keySet()), edge.getWire().getSpeed());
                DefaultWeightedEdge newEdge = graphTemp.addEdge(source, target);
                // Asignar el peso correspondiente a la arista
                graphTemp.setEdgeWeight(newEdge, edgeValue);
            }
        }

        // Encontrar el camino más corto desde e1 a e2
        GraphPath<Equipment, DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(graphTemp, e1, e2);
        if (path == null) {
            throw new IllegalArgumentException("No existe un camino entre los equipos.");
        }

        // Devolver el camino completo
        return path;
    }


    /**
     * Calcula la velocidad mínima de conexión entre dos equipos considerando
     * la velocidad de los puertos y la velocidad del cable.
     * <p>
     * Este método recibe dos listas de tipos de puertos (representando las velocidades de
     * los puertos de dos equipos) y la velocidad de un cable. Compara las velocidades
     * de los puertos para determinar cuál es la más baja y luego la compara con la
     * velocidad del cable, devolviendo el valor más bajo de los tres.
     *
     * @param ports1    Una lista de objetos {@code PortType} que representan los puertos
     *                  del primer equipo.
     * @param ports2    Una lista de objetos {@code PortType} que representan los puertos
     *                  del segundo equipo.
     * @param wireSpeed La velocidad del cable de conexión entre los equipos.
     * @return La velocidad mínima de conexión entre los dos equipos, que es la velocidad
     * más baja de los puertos y la velocidad del cable.
     */
    private static int getMinSpeed(List<PortType> ports1, List<PortType> ports2, int wireSpeed) {
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

    public Graph<Equipment, Connection> getGraph() {
        return graph;
    }


    public void setGraph(Graph<Equipment, Connection> graph) {
        CalculoGraph.graph = graph;
    }


    public List<Equipment> detectConnectivityIssues(Equipment startNode) {
        if (!graph.containsVertex(startNode))
            throw new NotFoundException("equipo no se encuentra");
        List<Equipment> visitedNodes = new ArrayList<>();
        BreadthFirstIterator<Equipment, Connection> bfsIterator = new BreadthFirstIterator<>(graph, startNode);

        while (bfsIterator.hasNext()) {
            visitedNodes.add(bfsIterator.next());
        }

        return visitedNodes;
    }

    /**
     * Verifica si un equipo está en el grafo y si está activo.
     * 
     * @param e1 El equipo que se desea verificar.
     * @return {@code true} si el equipo está activo, {@code false} en caso contrario.
     * @throws NotFoundException Si el equipo no se encuentra en el grafo.
     */
    public boolean ping(Equipment e1) {
        if (!graph.containsVertex(e1))
            throw new NotFoundException("equipo no se encuentra");
        return e1.isStatus();
    }

    /**
     * Verifica si una dirección IP está en el grafo.
     * 
     * @param ip La dirección IP que se desea verificar.
     * @return {@code true} si la IP está presente en alguno de los equipos, {@code false} en caso contrario.
     */
    public static boolean ping(String ip) {
        if (graph != null) {
            for (Equipment equipo : graph.vertexSet()) {
                if (equipo.getIpAdresses().contains(ip)) {
                    return true;
                }
            }
        } else {
            System.out.println("El graph es null");
        }
        return false;
    }

    /**
     * Verifica el estado de todos los equipos en el grafo.
     * 
     * @return Un mapa que relaciona cada equipo con su estado de actividad.
     */
    public HashMap<Equipment, Boolean> ping() {
        HashMap<Equipment, Boolean> mapStatus = new HashMap<>();
        for (Equipment p : graph.iterables().vertices()) {
            mapStatus.put(p, p.isStatus());
        }
        return mapStatus;

    }

    /**
     * Verifica si una IP es válida.
     * 
     * @param ip La IP que se desea verificar.
     * @return {@code true} si la IP es válida, {@code false} en caso contrario.
     * @throws NumberFormatException Si un segmento de la IP no es un número válido.
     */
    private boolean checkIp(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) return false;

        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("es una ip valida");
            }
        }
        return true;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public static List<PortType> convertSetToList(Set<PortType> set) {
        return new ArrayList<>(set);
    }

    /**
     * Escanea un rango de IPs comenzando desde la IP dada.
     * 
     * @param ip La IP inicial para comenzar el escaneo.
     * @return Una lista de IPs válidas encontradas.
     */
    public List<String> scanIP(String ip) {
        String[] parts = ip.split("\\.");
        List<String> ipList = new ArrayList<>();
        int start = Integer.parseInt(parts[3]);
        int startThirdSegment = Integer.parseInt(parts[2]);

        IntStream.range(startThirdSegment, 256).forEach(j -> IntStream.range(start, 256).forEach(i -> {
            String nuevaIP = parts[0] +parts[1] + j +i;
            System.out.println(nuevaIP);
            if (CalculoGraph.ping(nuevaIP)) {
                System.out.println("encontro");
                ipList.add(nuevaIP);
            }
        }));

        return ipList; // Devolver la lista de IPs válidas
    }

    /**
     * Genera una dirección MAC aleatoria.
     * 
     * @return Una dirección MAC generada aleatoriamente.
     */
    public static String generarMAC() {
        Random random = new Random();
        byte[] macAddr = new byte[6];
        random.nextBytes(macAddr);

        StringBuilder macAddress = new StringBuilder(18);
        for (byte b : macAddr) {
            if (!macAddr.toString().isEmpty()) {
                macAddress.append(":");
            }
            macAddress.append(String.format("%02x", b));
        }
        return macAddress.toString().toUpperCase();
    }

    /**
     * Verifica si todos los puertos de un equipo están ocupados.
     * 
     * @param equipo El equipo que se desea verificar.
     * @throws NotFoundException Si el equipo tiene todos sus puertos ocupados.
     */
    public static void verificarPuertosOcupados(Equipment equipo) {
        if (equipo.getIpAdresses().size() == equipo.getPorts().size()) {
            throw new NotFoundException("El equipo " + equipo.getCode() + " tiene todos los puertos ocupados.");
        }
    }

    /**
     * Verifica si el equipo es un dispositivo de red.
     * 
     * @param equipo El equipo que se desea verificar.
     * @return {@code true} si el equipo es un dispositivo de red, {@code false} en caso contrario.
     */
    public static boolean esEquipoRed(Equipment equipo) {
        String code = equipo.getEquipmentType().getCode();
        return code.equals("SW") || code.equals("RT") || code.equals("AP");
    }

    /**
     * Genera una nueva dirección IP para un equipo dentro de una red.
     * 
     * @param equipo El equipo para el cual se desea generar la IP.
     * @param LAN    La red en la que se encuentra el equipo.
     * @return Una nueva IP generada para el equipo.
     */
    public static String generarNuevaIP(Equipment equipo, LAN LAN) {
        String[] parts = equipo.getIpAdresses().getFirst().split("\\.");
        String nuevaIP = "";
        int pool = Integer.parseInt(parts[3]);

        boolean t = true;
        while (t) {
            t = false;
            pool += 1;
            nuevaIP = String.format("%s.%s.%s.%d", parts[0], parts[1], parts[2], pool);
            for (Equipment eq :  LAN.getHardware().values()) {
                if (eq.getIpAdresses().contains(nuevaIP)) {
                    t = true;
                    break;
                }
            }
        }
        return nuevaIP;
    }
}