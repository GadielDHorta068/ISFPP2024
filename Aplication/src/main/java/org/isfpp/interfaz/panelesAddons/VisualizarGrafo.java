package org.isfpp.interfaz.panelesAddons;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class VisualizarGrafo extends JFrame {
    private Properties properties;
    private Coordinator coordinator;
    private ResourceBundle rb;

    public VisualizarGrafo() throws HeadlessException {
        setLayout(new BorderLayout());  // Establecer el Layout a BorderLayout

    }
    public void Visualizar() {
        this.rb=coordinator.getResourceBundle();
        Graph<Equipment, Connection> graph = coordinator.getGraph(); // Usar SimpleGraph para grafo no dirigido

        cargarProperties();
        mxGraph mxGraph = new mxGraph();
        mxGraph.setConnectableEdges(false);
        mxGraph.setCellsEditable(false);
        mxGraph.setCellsResizable(false);
        mxGraph.setCellsBendable(false);
        mxGraph.setCellsSelectable(false);

        Object parent = mxGraph.getDefaultParent();
        mxGraph.getModel().beginUpdate();

        mxStylesheet stylesheet = mxGraph.getStylesheet();
        Map<String, Object> edgeStyle = new Hashtable<>();
        edgeStyle.put("strokeColor", "#6482B9");
        edgeStyle.put("endArrow", "none");
        edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, 2);
        stylesheet.putCellStyle("EDGE_STYLE", edgeStyle);

        try {
            HashMap<Equipment, Object> vertexMap = new HashMap<>();

            // Agregar los vértices de JGraphT al grafo de JGraphX
            for (Equipment vertex : graph.vertexSet()) {
                String vertexStyle = getVertexStyle(mxGraph, vertex);
                Object v = mxGraph.insertVertex(parent, null, vertex.getCode(), 100, 100, 80, 80, vertexStyle);
                vertexMap.put(vertex, v);
            }

            // Agregar las aristas (conexiones) de JGraphT al grafo de JGraphX
            for (Connection edge : graph.edgeSet()) {
                Equipment source = graph.getEdgeSource(edge);
                Equipment target = graph.getEdgeTarget(edge);
                mxGraph.insertEdge(parent, null, edge.getWire().getSpeed(), vertexMap.get(source), vertexMap.get(target), "EDGE_STYLE");
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }


        mxOrganicLayout layout = new mxOrganicLayout(mxGraph);
        layout.execute(mxGraph.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        graphComponent.getViewport().setBackground(StylusUI.COLOR_PRIMARIO);
        add(graphComponent, BorderLayout.SOUTH);  // Coloca el componente en la parte superior
        setTitle(rb.getString("ver_grafo"));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        pack();

        setVisible(true);
    }

    private void cargarProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException(rb.getString("propiedades_falta"));
            }
            properties.load(input);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, rb.getString("error_propiedades: ") + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String getVertexStyle(mxGraph mxGraph, Equipment equipment) {
        // Configurar estilos de vértices con imágenes basadas en el archivo de propiedades
        String equipmentType = equipment.getEquipmentType().getCode();
        String imagePath = properties.getProperty("icon." + equipmentType);

        Map<String, Object> style = new Hashtable<>();

        if (imagePath != null) {
            style.put("shape", "image");
            style.put("image", Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath)).toString()); // Asegurarse de que la ruta es correcta y accesible
        } else {
            style.put("shape", "ellipse");
            style.put("fillColor", "#C3D9FF");
            style.put("strokeColor", "#6482B9");
            style.put("fontColor", "#774400");
        }


        style.put("fontSize", 16);
        style.put("fontColor", "#000000");
        String styleName = equipmentType + "_STYLE";
        mxGraph.getStylesheet().putCellStyle(styleName, style);
        return styleName;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
