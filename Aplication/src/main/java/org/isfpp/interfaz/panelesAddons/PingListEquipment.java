package org.isfpp.interfaz.panelesAddons;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PingListEquipment {
    private JTextArea textArea;
    private Coordinator coordinator;
    private HashMap<Equipment, Boolean> direcciones;

    public PingListEquipment() {

    }

    public void ping() {
        direcciones = new HashMap<>();
        JFrame frame = new JFrame("Equipos Activos");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(textArea);
        direcciones = coordinator.ping();
        updateTextArea();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        frame.setVisible(true);

    }

    private void updateTextArea() {
        for (Map.Entry<Equipment, Boolean> entry : direcciones.entrySet()) {
            textArea.append(entry.getKey().getCode() + " : " + entry.getValue() + "\n");
        }
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
