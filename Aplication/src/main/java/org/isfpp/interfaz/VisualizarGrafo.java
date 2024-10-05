package org.isfpp.interfaz;import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class VisualizarGrafo extends JFrame {

    private Graph<Equipment, Connection> graph;

    public VisualizarGrafo(HashMap<String, Equipment> hardware, ArrayList<Connection> connections) {
        graph = new SimpleGraph<>(Connection.class);
        for (Equipment valor : hardware.values()) {
            graph.addVertex(valor);
        }
        for (Connection c : connections) {
            Equipment sourceNode = c.getPort1().getEquipment();
            Equipment targetNode = c.getPort2().getEquipment();

            if (sourceNode.equals(targetNode)) throw new IllegalArgumentException("son el mismo equipo");
            if (!graph.containsEdge(sourceNode, targetNode)) graph.addEdge(sourceNode, targetNode, c);
        }

        // Crear el grafo en JGraphX manualmente
        mxGraph mxGraph = new mxGraph();
        Object parent = mxGraph.getDefaultParent();
        mxGraph.getModel().beginUpdate();

        try {
            HashMap<Equipment, Object> vertexMap = new HashMap<>();

            // Agregar los vértices de JGraphT al grafo de JGraphX
            for (Equipment vertex : graph.vertexSet()) {
                Object v = mxGraph.insertVertex(parent, null, vertex.getCode(), 100, 100, 80, 30);
                vertexMap.put(vertex, v);
            }

            // Agregar las aristas (conexiones) de JGraphT al grafo de JGraphX
            for (Connection edge : graph.edgeSet()) {
                Equipment source = graph.getEdgeSource(edge);
                Equipment target = graph.getEdgeTarget(edge);
                mxGraph.insertEdge(parent, null, edge, vertexMap.get(source), vertexMap.get(target));
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        getContentPane().add(graphComponent);

        // Aplicar un layout circular al grafo
        mxCircleLayout layout = new mxCircleLayout(mxGraph);
        layout.execute(mxGraph.getDefaultParent());

        setTitle("Visualización del Grafo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

}

