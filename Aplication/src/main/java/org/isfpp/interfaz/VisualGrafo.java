package org.isfpp.interfaz;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.jgrapht.graph.SimpleGraph;


import javax.swing.*;
import java.util.HashMap;
import java.util.ArrayList;

public class VisualizarGrafo extends JFrame {
    private SimpleGraph<Equipment, Connection> graph;

    public VisualizarGrafo(HashMap<String, Equipment> hardware, ArrayList<Connection> connections) {
        // Crear un grafo no dirigido
        graph = new SimpleGraph<>(Connection.class);
        for (Equipment valor : hardware.values()) {
            graph.addVertex(valor);
        }
        for (Connection c : connections) {
            Equipment sourceNode = c.getEquipment1();
            Equipment targetNode = c.getEquipment2();

            if (sourceNode.equals(targetNode)) throw new IllegalArgumentException("son el mismo equipo");
            if (!graph.containsEdge(sourceNode, targetNode)) graph.addEdge(sourceNode, targetNode, c);
        }

        // Adaptar el grafo para JGraphX
        JGraphXAdapter<Equipment, Connection> graphAdapter = new JGraphXAdapter<>(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        getContentPane().add(graphComponent);

        // Aplicar un layout al grafo
        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        setTitle("Visualización del Grafo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        HashMap<String, Equipment> hardware = new HashMap<>();
        ArrayList<Connection> connections = new ArrayList<>();

        // Agregar datos a hardware y connections aquí

        new VisualizarGrafo(hardware, connections);
    }
}
