package org.isfpp.logica;

import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Web;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Utils {

    private Graph<Equipment, Connection> graph;
    /**
     * Genera una direccion MAC aleatoria, usada en la creacion de todo Nodo
     *
     * @return String
     */
    public Utils(HashMap<String, Equipment> hardware, ArrayList<Connection> connections) {
        // Crear un grafo no dirigido
        graph = new SimpleGraph<>(Connection.class);
        for (Equipment valor : hardware.values()) {
            graph.addVertex(valor);
        }
        for (Connection c : connections) {
            Equipment sourceNode = c.getEquipment1();
            Equipment targetNode = c.getEquipment2();

            if (sourceNode.equals(targetNode))
                throw new IllegalArgumentException("son el mismo equipo");
            if (graph.containsEdge(sourceNode, targetNode))
                graph.addEdge(sourceNode, targetNode, c);

        }
    }

}
