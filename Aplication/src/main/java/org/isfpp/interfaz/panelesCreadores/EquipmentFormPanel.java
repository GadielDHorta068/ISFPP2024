package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class EquipmentFormPanel extends JPanel {
    private final JTextField codeField;
    private final JTextField descriptionField;
    private final JTextField marcaField;
    private final JTextField modeloField;
    private final JComboBox<EquipmentType> equipmentTypeCombo;
    private final JComboBox<Location> locationCombo;
    private final JComboBox<PortType> portTypeCombo;
    private final JCheckBox statusCheckBox;

    public EquipmentFormPanel(Web web) {
        JFrame frame = new JFrame("Formulario de Equipo");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
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

        JLabel marc = new JLabel("Marca:");
        StylusUI.aplicarEstiloEtiqueta(marc);
        formPanel.add(marc);
        marcaField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(marcaField);
        formPanel.add(marcaField);


        JLabel mod = new JLabel("Modelo:");
        StylusUI.aplicarEstiloEtiqueta(mod);
        formPanel.add(mod);
        modeloField = new JTextField();
        StylusUI.aplicarEstiloCampoTexto(modeloField);
        formPanel.add(modeloField);


        JLabel tipoDeEquipo = new JLabel("Tipo de equipo");
        StylusUI.aplicarEstiloEtiqueta(tipoDeEquipo);
        formPanel.add(tipoDeEquipo);
        equipmentTypeCombo = new JComboBox<>(web.getEquipmentTypes().values().toArray(new EquipmentType[0]));
        StylusUI.aplicarEstiloComboBox(equipmentTypeCombo);
        formPanel.add(equipmentTypeCombo);

        JLabel ubi = new JLabel("Ubicacion:");
        StylusUI.aplicarEstiloEtiqueta(ubi);
        formPanel.add(ubi);
        locationCombo = new JComboBox<>(web.getLocations().values().toArray(new Location[0]));
        StylusUI.aplicarEstiloComboBox(locationCombo);
        formPanel.add(locationCombo);

        JLabel puerto = new JLabel("Tipo de Puerto");
        StylusUI.aplicarEstiloEtiqueta(puerto);
        formPanel.add(puerto);
        portTypeCombo = new JComboBox<>(web.getPortTypes().values().toArray(new PortType[0]));
        StylusUI.aplicarEstiloComboBox(portTypeCombo);
        formPanel.add(portTypeCombo);

        JLabel estado = new JLabel("Estado (Activo)");
        StylusUI.aplicarEstiloEtiqueta(estado);
        formPanel.add(estado);
        statusCheckBox = new JCheckBox();
        //StylusUI.aplicarEstiloCheckBox(statusCheckBox);
        formPanel.add(statusCheckBox);


        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton("Crear Equipo");
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
                JOptionPane.showMessageDialog(this, "EL codigo no debe estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (portType == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un tipo de puerto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                web.addEquipment(code, description, marca, modelo, portType, 1, equipmentType, location, status);
                JOptionPane.showMessageDialog(this, "Equipo creado con éxito");
                web.getCoordinator().updateTablas();
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al crear equipo: " + ex.getMessage());
            }

        });

        frame.add(this);
        frame.setVisible(true);

    }
}
