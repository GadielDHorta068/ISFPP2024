package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.WireType;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class EquipmentTypeFromPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField speedField;
    private Coordinator coordinator;
    private ResourceBundle rb;

    public EquipmentTypeFromPanel() {
    }

    public void run() {
        rb = coordinator.getResourceBundle();

        JFrame frame = new JFrame(rb.getString("Tipo_Equipo"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("codigo"));
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField();
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

        JButton createButton = new JButton(rb.getString("Modificar_tipo_Puerto"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            try {
                coordinator.addEquipmentType(code,description);
                JOptionPane.showMessageDialog(this, rb.getString("Tipo_de_equipo_modificado_exito"));
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
