package org.isfpp.interfaz.panelesAddons;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Clase que mostrara los equipos que estan activos o inactivos
 */
public class ActiveEquipments {
    private JTextArea textArea;
    private Coordinator coordinator;
    private HashMap<Equipment, Boolean> direcciones;
    private ResourceBundle rb;

    /**
     * Constructor default
     */
    public ActiveEquipments() {}

    /**
     * Obtener el panel con el estado de todos los equipos
     */
    public void ping() {
        this.rb=coordinator.getResourceBundle();
        direcciones = new HashMap<>();
        JFrame frame = new JFrame(rb.getString("equipos_activos"));
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
        frame.pack();
        StylusUI.styleTextArea(textArea);
        frame.setVisible(true);
    }

    /**
     * Actualizar el area de texto
     */
    private void updateTextArea() {
        for (Map.Entry<Equipment, Boolean> entry : direcciones.entrySet()) {
            textArea.append(entry.getKey().getCode() + " : " + entry.getValue() + "\n");
        }
    }

    /**
     * Setear coordinador
     * @param coordinator coordinador de la red
     */
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
