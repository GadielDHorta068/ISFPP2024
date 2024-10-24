package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.LAN;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class LocationFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private ResourceBundle rb;
    private Coordinator coordinator;
    public LocationFormPanel(){}

    public void run() {
        this.rb=coordinator.getResourceBundle();

        JFrame frame = new JFrame(rb.getString("Agregar Ubicación"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 150);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("Codigo"));
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel(rb.getString("Descripcion:"));
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Crear Ubicacion"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();

            try {
                coordinator.addLocation(code, description);
                JOptionPane.showMessageDialog(this, rb.getString("Ubicacion creado con éxito"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("Error al crear ubicacion: ") + ex.getMessage());
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {this.coordinator=coordinator;
    }
}
