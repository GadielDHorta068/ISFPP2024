package org.isfpp.logica;

import org.apache.log4j.Logger;
import org.isfpp.controller.Coordinator;
import org.isfpp.exceptions.NotFoundException;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

public class CalculoGraph implements Observer{
    private final static Logger logger = Logger.getLogger(CalculoGraph.class);
    private static Graph<Equipment, Connection> graph;
    private Coordinator coordinator;
    private Subject subject;
    private boolean actualizar;

    public CalculoGraph() {
    }
    public void init(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.actualizar = true;
    }

    /**
     * Crea un grafo no dirigido a partir de los equipos y las conexiones proporcionadas por el objeto {@code Web}.
     * <p>
     * Este método toma un objeto {@code Web}, que contiene un mapa de equipos de hardware y una lista de conexiones,
     * y construye un grafo no dirigido. Cada equipo se agrega como un vértice en el grafo y cada conexión se agrega
     * como una arista entre los equipos correspondientes.
     *
     * @param lan El objeto {@code Web} que contiene la información de hardware y conexiones.
     * @throws IllegalArgumentException Si se intenta agregar una conexión entre el mismo equipo o si hay una
     *                                  conexión duplicada en el grafo.
     */
    public void LoadData(Lan lan) {
        if (actualizar) {
        HashMap<String, Equipment> hardware = coordinator.getHardware();
        ArrayList<Connection> connections = coordinator.getConnections();
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
            actualizar = false;
            coordinator.setGraph(graph);
            coordinator.updateTablas(lan);

            logger.info("Se actualizaron los datos para realizar calculos");
        } else
            logger.info("No se actualizaron los datos");
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
    public List<String> scanIP(String ip1, String ip2) {
        // Dividir las IPs en segmentos
        String[] parts1 = ip1.split("\\.");
        String[] parts2 = ip2.split("\\.");  // Corregido, ahora se usa ip2

        // Comprobar si ip1 es mayor que ip2 y si es así, intercambiarlas
        if (compareIPs(ip1, ip2) > 0) {
            // Si ip1 > ip2, intercambiar las IPs
            String temp = ip1;
            ip1 = ip2;
            ip2 = temp;
            // Volver a dividir las IPs después del intercambio
            parts1 = ip1.split("\\.");
            parts2 = ip2.split("\\.");
        }

        List<String> ipList = new ArrayList<>();

        // Convertir los segmentos a enteros
        int[] ip1Segments = new int[4];
        int[] ip2Segments = new int[4];
        for (int i = 0; i < 4; i++) {
            ip1Segments[i] = Integer.parseInt(parts1[i]);
            ip2Segments[i] = Integer.parseInt(parts2[i]);
        }

        // Recorrer los segmentos
        for (int i = ip1Segments[0]; i <= ip2Segments[0]; i++) {
            for (int j = (i == ip1Segments[0] ? ip1Segments[1] : 0); j <= (i == ip2Segments[0] ? ip2Segments[1] : 255); j++) {
                for (int k = (i == ip1Segments[0] && j == ip1Segments[1] ? ip1Segments[2] : 0); k <= (i == ip2Segments[0] && j == ip2Segments[1] ? ip2Segments[2] : 255); k++) {
                    for (int l = (i == ip1Segments[0] && j == ip1Segments[1] && k == ip1Segments[2] ? ip1Segments[3] : 0); l <= (i == ip2Segments[0] && j == ip2Segments[1] && k == ip2Segments[2] ? ip2Segments[3] : 255); l++) {
                        // Crear la nueva IP con formato adecuado
                        String nuevaIP = i + "." + j + "." + k + "." + l;
                        System.out.println(nuevaIP);  // Imprimir la nueva IP

                        // Realizar el ping para verificar la IP
                        if (CalculoGraph.ping(nuevaIP)) {
                            System.out.println("Encontró: " + nuevaIP);
                            ipList.add(nuevaIP);  // Agregar la IP válida a la lista
                            try {
                                Thread.sleep(1000);  // Uso correcto de sleep dentro de un hilo
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }

        return ipList;  // Devolver la lista de IPs válidas
    }

    private int compareIPs(String ip1, String ip2) {
        String[] parts1 = ip1.split("\\.");
        String[] parts2 = ip2.split("\\.");

        for (int i = 0; i < 4; i++) {
            int diff = Integer.parseInt(parts1[i]) - Integer.parseInt(parts2[i]);
            if (diff != 0) {
                return diff;  // Si los segmentos son diferentes, devolver la diferencia
            }
        }
        return 0;  // Si son iguales
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


    @Override
    public void update() {
        actualizar=true;
    }
}