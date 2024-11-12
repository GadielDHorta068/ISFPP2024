package org.isfpp.interfaz.panelesEditadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Location;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Panel editor de locaciones
 */
public class EditLocationFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private Coordinator coordinator;
    private ResourceBundle rb;

    public EditLocationFormPanel() {
    }

    public void run(String codeOriginal) {
        rb = coordinator.getResourceBundle(); // Asignar el ResourceBundle desde el Coordinator

        JFrame frame = new JFrame(rb.getString("agregar_ubicacion")); // Usar rb.getString() para el texto
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 150);

        setLayout(new BorderLayout());

        Location loc = coordinator.getLocations().get(codeOriginal); // Obtener Location usando el Coordinator
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("codigo")); // Texto traducido
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(loc.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel(rb.getString("descripcion")); // Texto traducido
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(loc.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("modificar_ubicacion")); // Texto traducido
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            try {
                coordinator.updateLocation(codeOriginal, new Location(codeField.getText(), descriptionField.getText()));
                JOptionPane.showMessageDialog(this, rb.getString("ubicacion_modificada_exito")); // Texto traducido
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("Error_modificar") + ": " + ex.getMessage()); // Texto traducido
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}