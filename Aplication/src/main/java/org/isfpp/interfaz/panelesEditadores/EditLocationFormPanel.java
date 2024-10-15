package org.isfpp.interfaz.panelesEditadores;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;

public class EditLocationFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;

    public EditLocationFormPanel(Web web, String codeOriginial) {

        JFrame frame = new JFrame("Agregar Ubicación");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400 , 150);

        setLayout(new BorderLayout());

        Location loc = web.getLocations().get(codeOriginial);
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel("Codigo");
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(loc.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel("Descripcion:");
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(loc.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);
        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton("modificar Ubicacion");
        StylusUI.aplicarEstiloBoton(createButton,true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(_ -> {
            String code = codeField.getText();
            String description = descriptionField.getText();

            try {
                loc.setCode(code);
                loc.setDescription(description);
                JOptionPane.showMessageDialog(this, "Ubicacion modificado con éxito");
                web.getCoordinator().updateTablas();
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error modificar: "+ex.getMessage());
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }
}
