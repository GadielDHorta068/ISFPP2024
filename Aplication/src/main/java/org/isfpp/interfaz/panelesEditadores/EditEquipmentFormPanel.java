package org.isfpp.interfaz.panelesEditadores;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.*;

public class EditEquipmentFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;
    private final JTextField marcaField;
    private final JTextField modeloField;
    private final JTextField capacidadField;
    private final JComboBox<EquipmentType> equipmentTypeCombo;
    private final JComboBox<Location> locationCombo;
    private final JComboBox<PortType> portTypeCombo;
    private final JCheckBox statusCheckBox;

    public EditEquipmentFormPanel(Lan lan, String codeOriginal) {
        JFrame frame = new JFrame("Edicion de Equipo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        setLayout(new BorderLayout());

        Equipment eq = lan.getHardware().get(codeOriginal);
        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel codigo = new JLabel("Codigo");
        StylusUI.aplicarEstiloEtiqueta(codigo);
        formPanel.add(codigo);
        codeField = new JTextField(eq.getCode());
        StylusUI.aplicarEstiloCampoTexto(codeField);
        formPanel.add(codeField);

        JLabel des = new JLabel("Descripcion:");
        StylusUI.aplicarEstiloEtiqueta(des);
        formPanel.add(des);
        descriptionField = new JTextField(eq.getDescription());
        StylusUI.aplicarEstiloCampoTexto(descriptionField);
        formPanel.add(descriptionField);

        JLabel marc = new JLabel("Marca:");
        StylusUI.aplicarEstiloEtiqueta(marc);
        formPanel.add(marc);
        marcaField = new JTextField(eq.getMake());
        StylusUI.aplicarEstiloCampoTexto(marcaField);
        formPanel.add(marcaField);


        JLabel mod = new JLabel("Modelo:");
        StylusUI.aplicarEstiloEtiqueta(mod);
        formPanel.add(mod);
        modeloField = new JTextField(eq.getModel());
        StylusUI.aplicarEstiloCampoTexto(modeloField);
        formPanel.add(modeloField);


        JLabel tipoDeEquipo = new JLabel("Tipo de equipo");
        StylusUI.aplicarEstiloEtiqueta(tipoDeEquipo);
        formPanel.add(tipoDeEquipo);
        equipmentTypeCombo = new JComboBox<>(lan.getEquipmentTypes().values().toArray(new EquipmentType[0]));
        StylusUI.aplicarEstiloComboBox(equipmentTypeCombo);
        formPanel.add(equipmentTypeCombo);

        JLabel ubi = new JLabel("Ubicacion:");
        StylusUI.aplicarEstiloEtiqueta(ubi);
        formPanel.add(ubi);
        locationCombo = new JComboBox<>(lan.getLocations().values().toArray(new Location[0]));
        StylusUI.aplicarEstiloComboBox(locationCombo);
        formPanel.add(locationCombo);

        JLabel puerto = new JLabel("Tipo de Puerto");
        StylusUI.aplicarEstiloEtiqueta(puerto);
        formPanel.add(puerto);
        portTypeCombo = new JComboBox<>(lan.getPortTypes().values().toArray(new PortType[0]));
        StylusUI.aplicarEstiloComboBox(portTypeCombo);
        formPanel.add(portTypeCombo);
        JLabel capacidad = new JLabel("cantidad de puertos");
        StylusUI.aplicarEstiloEtiqueta(capacidad);
        formPanel.add(capacidad);
        capacidadField = new JTextField(0);
        StylusUI.aplicarEstiloCampoTexto(capacidadField);
        formPanel.add(capacidadField);
        
        JLabel estado = new JLabel("Estado (Activo)");
        StylusUI.aplicarEstiloEtiqueta(estado);
        formPanel.add(estado);
        statusCheckBox = new JCheckBox();
        StylusUI.aplicarEstiloCheckBox(statusCheckBox);
        formPanel.add(statusCheckBox);


        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton("Editar Equipo");
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            String code = codeField.getText();
            String description = descriptionField.getText();
            String marca = marcaField.getText();
            String modelo = modeloField.getText();
            EquipmentType equipmentType = (EquipmentType) equipmentTypeCombo.getSelectedItem();
            Location location = (Location) locationCombo.getSelectedItem();
            PortType portType = (PortType) portTypeCombo.getSelectedItem();
            boolean status = statusCheckBox.isSelected();

            try {
                lan.updateEquipment(codeOriginal,new Equipment(code,description,marca,modelo,portType,Integer.parseInt(capacidadField.getText()),equipmentType,location,status));

                JOptionPane.showMessageDialog(this, "Equipo modificado con éxito");
                lan.getCoordinator().updateTablas(lan);
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al mod equipo: " + ex.getMessage());
            }

        });

        frame.add(this);
        frame.setVisible(true);

    }
}
