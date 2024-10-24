package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.LAN;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class EquipmentFormPanel extends JPanel {
    private JTextField codeField;
    private JTextField descriptionField;
    private JTextField marcaField;
    private JTextField modeloField;
    private JComboBox<EquipmentType> equipmentTypeCombo;
    private JComboBox<Location> locationCombo;
    private JComboBox<PortType> portTypeCombo;
    private JCheckBox statusCheckBox;
    private ResourceBundle rb;
    private Coordinator coordinator;
    public EquipmentFormPanel() {}
    public void run() {
        this.rb=coordinator.getResourceBundle();
        JFrame frame = new JFrame(rb.getString("Formulario de Equipo"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
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

        JLabel marc = new JLabel("Marca:");
        StylusUI.aplicarEstiloEtiqueta(marc);
        formPanel.add(marc);
        marcaField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(marcaField);
        formPanel.add(marcaField);


        JLabel mod = new JLabel(rb.getString("Modelo:"));
        StylusUI.aplicarEstiloEtiqueta(mod);
        formPanel.add(mod);
        modeloField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(modeloField);
        formPanel.add(modeloField);


        JLabel tipoDeEquipo = new JLabel(rb.getString("Tipo de equipo"));
        StylusUI.aplicarEstiloEtiqueta(tipoDeEquipo);
        formPanel.add(tipoDeEquipo);
        equipmentTypeCombo = new JComboBox<>(coordinator.getEquipmentTypes().values().toArray(new EquipmentType[0]));
        StylusUI.aplicarEstiloComboBox(equipmentTypeCombo);
        formPanel.add(equipmentTypeCombo);

        JLabel ubi = new JLabel(rb.getString("Ubicacion:"));
        StylusUI.aplicarEstiloEtiqueta(ubi);
        formPanel.add(ubi);
        locationCombo = new JComboBox<>(coordinator.getLocations().values().toArray(new Location[0]));
        StylusUI.aplicarEstiloComboBox(locationCombo);
        formPanel.add(locationCombo);

        JLabel puerto = new JLabel(rb.getString("Tipo de Puerto"));
        StylusUI.aplicarEstiloEtiqueta(puerto);
        formPanel.add(puerto);
        portTypeCombo = new JComboBox<>(coordinator.getPortTypes().values().toArray(new PortType[0]));
        StylusUI.aplicarEstiloComboBox(portTypeCombo);
        formPanel.add(portTypeCombo);

        JLabel estado = new JLabel(rb.getString("Estado (Activo)"));
        StylusUI.aplicarEstiloEtiqueta(estado);
        formPanel.add(estado);
        statusCheckBox = new JCheckBox();
        //StylusUI.aplicarEstiloCheckBox(statusCheckBox);
        formPanel.add(statusCheckBox);


        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Crear Equipo"));
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

            if (Objects.equals(code, "")) {
                JOptionPane.showMessageDialog(this, rb.getString("EL codigo no debe estar vacio"),rb.getString( "Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (portType == null) {
                JOptionPane.showMessageDialog(this, rb.getString("Debe seleccionar al menos un tipo de puerto"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                coordinator.addEquipment(code, description, marca, modelo, portType, 1, equipmentType, location, status);
                JOptionPane.showMessageDialog(this, rb.getString("Equipo creado con éxito"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, rb.getString("Error al crear equipo: " )+ ex.getMessage());
            }

        });

        frame.add(this);
        frame.setVisible(true);

    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
