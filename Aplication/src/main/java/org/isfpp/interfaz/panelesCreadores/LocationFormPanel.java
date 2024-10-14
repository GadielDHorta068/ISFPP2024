package org.isfpp.interfaz.panelesCreadores;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;

public class LocationFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;

    public LocationFormPanel(Web web) {

        JFrame frame = new JFrame("Agregar Ubicación");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400 , 150);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
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

        JButton createButton = new JButton("Crear Ubicacion");
        StylusUI.aplicarEstiloBoton(createButton,true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(_ -> {
            String code = codeField.getText();
            String description = descriptionField.getText();

            try {
                web.addLocation(code, description);
                JOptionPane.showMessageDialog(this, "Ubicacion creado con éxito");
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, STR."Error al crear equipo: \{ex.getMessage()}");
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }
}
