package org.isfpp.interfaz.panelesEditadores;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.Lan;

import javax.swing.*;
import java.awt.*;

public class EditPortTypeFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;
    private final JTextField speedField;

    public EditPortTypeFormPanel(Lan Lan, String codeOriginal) {
        JFrame frame = new JFrame("Modificar tipo de Puerto");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        setLayout(new BorderLayout());
        PortType port = Lan.getPortTypes().get(codeOriginal);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel("Codigo");
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(port.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel("Descripcion:");
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(port.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);
        add(formPanel, BorderLayout.CENTER);

        JLabel speed = new JLabel("Velocidad:");
        StylusUI.aplicarEstiloEtiqueta(speed);
        formPanel.add(speed);
        speedField = new JTextField(port.getSpeed());
        StylusUI.aplicarEstiloCampoTexto(speedField);
        formPanel.add(speedField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton("Modificar tipo de puerto");
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            int portSpeed = Integer.parseInt(speedField.getText());
            try {
                port.setCode(code);
                port.setDescription(description);
                port.setSpeed(portSpeed);
                Lan.updatePortType(codeOriginal,port);
                JOptionPane.showMessageDialog(this, "Tipo de puerto modificar con éxito");
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
            }
        });
        frame.add(this);
        frame.setVisible(true);
    }
}
