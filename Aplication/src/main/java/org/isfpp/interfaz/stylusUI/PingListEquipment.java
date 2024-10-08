package org.isfpp.interfaz.stylusUI;

import org.isfpp.controller.Coordinator;
import org.isfpp.modelo.Equipment;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PingListEquipment {
    private JFrame frame;
    private JTextArea textArea;
    private Coordinator coordinator;
    private HashMap<Equipment, Boolean> direcciones;
    public PingListEquipment() {

    }
    public void ping (){
        direcciones = new HashMap<>();
        frame = new JFrame("TraceRouter");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        direcciones=coordinator.ping();
        JPanel northPanel = new JPanel();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);// Añadir el panel con los dos JTextField
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        frame.setVisible(true);

    }
    private void updateTextArea () {
        for (Map.Entry<Equipment, Boolean> entry : direcciones.entrySet()) {
            textArea.append(entry.getKey().getCode() + " : " + entry.getValue() + "\n");
        }
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
