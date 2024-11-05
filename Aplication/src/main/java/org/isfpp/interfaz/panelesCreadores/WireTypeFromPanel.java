package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.WireType;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class WireTypeFromPanel extends JPanel{
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField speedField;
    private Coordinator coordinator;
    private ResourceBundle rb;

    public WireTypeFromPanel() {
    }

    public void run(String codeOriginal) {
        rb = coordinator.getResourceBundle();

        JFrame frame = new JFrame(rb.getString("Tipo_Cable"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);

        setLayout(new BorderLayout());

        WireType Wire = coordinator.getWireTypes().get(codeOriginal);
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("codigo"));
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(Wire.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel(rb.getString("descripcion"));
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(Wire.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);

        JLabel speed = new JLabel(rb.getString("Velocidad"));
        StylusUI.aplicarEstiloEtiqueta(speed);
        formPanel.add(speed);
        speedField = new JTextField(String.valueOf(Wire.getSpeed()));
        StylusUI.aplicarEstiloCampoTexto(speedField);
        formPanel.add(speedField);

        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Modificar_tipo_Puerto"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            int wireSpeed= Integer.parseInt(speedField.getText());
            try {
                coordinator.addWire(code,description,wireSpeed);
                JOptionPane.showMessageDialog(this, rb.getString("Tipo_de_puerto_modificado_exito"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("Error_modificar") + ": " + ex.getMessage());
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
