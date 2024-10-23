package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.LAN;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PortTypeFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;
    private final JTextField speedField;

    public PortTypeFormPanel(LAN LAN) {
        JFrame frame = new JFrame("Agregar tipo de Puerto");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel("Codigo");
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel("Descripcion:");
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);
        add(formPanel, BorderLayout.CENTER);

        JLabel speed = new JLabel("Velocidad:");
        StylusUI.aplicarEstiloEtiqueta(speed);
        formPanel.add(speed);
        speedField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(speedField);
        formPanel.add(speedField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton("Crear tipo de puerto");
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            int portSpeed = Integer.parseInt(speedField.getText());

            if (Objects.equals(code, "")) {
                JOptionPane.showMessageDialog(this, "El codigo no debe estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                LAN.addPort(code, description, portSpeed);
                JOptionPane.showMessageDialog(this, "Tipo de puerto creado con éxito");
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear puerto: " + ex.getMessage());
            }
        });
        frame.add(this);
        frame.setVisible(true);
    }
}
