package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class PortTypeFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField speedField;
    private ResourceBundle rb;
    private Coordinator coordinator;

    public PortTypeFormPanel() {}

    public void run() {
        this.rb = coordinator.getResourceBundle();

        JFrame frame = new JFrame(rb.getString("agregar_puerto"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("codigo"));
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel(rb.getString("descripcion"));
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);

        JLabel speed = new JLabel(rb.getString("velocidad"));
        StylusUI.aplicarEstiloEtiqueta(speed);
        formPanel.add(speed);
        speedField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(speedField);
        formPanel.add(speedField);

        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("crear_tipo_puerto"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            try {
                String code = codeField.getText().trim();
                String description = descriptionField.getText().trim();
                String speedText = speedField.getText().trim();

                if (Objects.equals(code, "")) {
                    JOptionPane.showMessageDialog(this, rb.getString("codigo_no_vacio"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int portSpeed;
                try {
                    portSpeed = Integer.parseInt(speedText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, rb.getString("velocidad_invalida"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                coordinator.addPort(code, description, portSpeed);
                JOptionPane.showMessageDialog(this, rb.getString("puerto_creado"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("error_crear_puerto") + ": " + ex.getMessage());
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}

