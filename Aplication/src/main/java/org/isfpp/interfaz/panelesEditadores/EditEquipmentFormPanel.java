package org.isfpp.interfaz.panelesEditadores;


import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.interfaz.stylusUI.WindowUtils;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class EditEquipmentFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField marcaField;
    private JTextField modeloField;
    private JTextField capacidadField;
    private JComboBox<EquipmentType> equipmentTypeCombo;
    private JComboBox<Location> locationCombo;
    private JComboBox<PortType> portTypeCombo;
    private JCheckBox statusCheckBox;
    private Coordinator coordinator;
    private ResourceBundle rb;

    public EditEquipmentFormPanel(){}

    public void run(String codeOriginal) {
        rb = coordinator.getResourceBundle();
        JFrame frame = new JFrame(rb.getString("edicion_equipo"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        setLayout(new BorderLayout());

        Equipment eq = coordinator.getHardware().get(codeOriginal);
        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel(rb.getString("codigo"));
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(eq.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel(rb.getString("descripcion"));
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(eq.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);

        JLabel marc = new JLabel(rb.getString("marca"));
        StylusUI.aplicarEstiloEtiqueta(marc);
        formPanel.add(marc);
        marcaField = new JTextField(eq.getMake());
        StylusUI.aplicarEstiloCampoTexto(marcaField);
        formPanel.add(marcaField);

        JLabel mod = new JLabel(rb.getString("modelo"));
        StylusUI.aplicarEstiloEtiqueta(mod);
        formPanel.add(mod);
        modeloField = new JTextField(eq.getModel());
        StylusUI.aplicarEstiloCampoTexto(modeloField);
        formPanel.add(modeloField);

        JLabel tipoDeEquipo = new JLabel(rb.getString("tipo_equipo"));
        StylusUI.aplicarEstiloEtiqueta(tipoDeEquipo);
        formPanel.add(tipoDeEquipo);
        equipmentTypeCombo = new JComboBox<>(coordinator.getEquipmentTypes().values().toArray(new EquipmentType[0]));
        StylusUI.aplicarEstiloComboBox(equipmentTypeCombo);
        formPanel.add(equipmentTypeCombo);

        JLabel ubi = new JLabel(rb.getString("ubicacion"));
        StylusUI.aplicarEstiloEtiqueta(ubi);
        formPanel.add(ubi);
        locationCombo = new JComboBox<>(coordinator.getLocations().values().toArray(new Location[0]));
        StylusUI.aplicarEstiloComboBox(locationCombo);
        formPanel.add(locationCombo);

        JLabel puerto = new JLabel(rb.getString("tipo_puerto"));
        StylusUI.aplicarEstiloEtiqueta(puerto);
   //     formPanel.add(puerto);
        portTypeCombo = new JComboBox<>(coordinator.getPortTypes().values().toArray(new PortType[0]));
        StylusUI.aplicarEstiloComboBox(portTypeCombo);
   //     formPanel.add(portTypeCombo);

        JLabel capacidad = new JLabel(rb.getString("cantidad_puertos"));
        StylusUI.aplicarEstiloEtiqueta(capacidad);
 //       formPanel.add(capacidad);
        capacidadField = new JTextField(0);
        StylusUI.aplicarEstiloCampoTexto(capacidadField);
   //     formPanel.add(capacidadField);

        JLabel estado = new JLabel(rb.getString("estado"));
        StylusUI.aplicarEstiloEtiqueta(estado);
      //  formPanel.add(estado);
        statusCheckBox = new JCheckBox();
        StylusUI.aplicarEstiloCheckBox(statusCheckBox);
     //   formPanel.add(statusCheckBox);

        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("editar_equipo"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            String marca = marcaField.getText();
            String modelo = modeloField.getText();
            EquipmentType equipmentType = (EquipmentType) equipmentTypeCombo.getSelectedItem();
            Location location = (Location) locationCombo.getSelectedItem();
            //PortType portType = eq.getAllPortsTypes();
       //     boolean status = statusCheckBox.isSelected();

            try {
                Equipment modEq = new Equipment(code, description, marca, modelo, null, 0, equipmentType, location, eq.isStatus());
                modEq.setPorts(eq.getPorts());
                modEq.setIpAdresses(eq.getIpAdresses());
                coordinator.updateEquipment(codeOriginal, modEq);

                JOptionPane.showMessageDialog(this, rb.getString("equipo_modificado"));
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("Error_equipo") + ": " + ex.getMessage());
            }
        });

        frame.add(this);
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
