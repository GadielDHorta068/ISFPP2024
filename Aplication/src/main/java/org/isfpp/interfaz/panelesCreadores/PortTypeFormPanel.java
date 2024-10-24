package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.LAN;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class PortTypeFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField speedField;
    private ResourceBundle rb;
    Coordinator coordinator;
    public PortTypeFormPanel() {}
    public void run() {
        this.rb=coordinator.getResourceBundle();


        JFrame frame = new JFrame(rb.getString("Agregar tipo de Puerto"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
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

        JLabel speed = new JLabel(rb.getString("Velocidad:"));
        StylusUI.aplicarEstiloEtiqueta(speed);
        formPanel.add(speed);
        speedField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(speedField);
        formPanel.add(speedField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Crear tipo de puerto"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            int portSpeed = Integer.parseInt(speedField.getText());

            if (Objects.equals(code, "")) {
                JOptionPane.showMessageDialog(this, rb.getString("El codigo no debe estar vacio"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                coordinator.addPort(code, description, portSpeed);
                JOptionPane.showMessageDialog(this, rb.getString("Tipo de puerto creado con éxito"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,rb.getString( "Error al crear puerto: ") + ex.getMessage());
            }
        });
        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
