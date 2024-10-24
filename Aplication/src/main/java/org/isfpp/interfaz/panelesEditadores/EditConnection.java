package org.isfpp.interfaz.panelesEditadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class EditConnection extends JPanel {
    private JComboBox<Equipment> eq1ComboBox;
    private JComboBox<Equipment> eq2ComboBox;
    private JComboBox<Port> port1ComboBox;
    private JComboBox<Port> port2ComboBox;
    private JComboBox<WireType> wireComboBox;
    private ResourceBundle rb;
    private Coordinator coordinator;

    public EditConnection() {
    }

    public void run(Connection c) {
        rb= coordinator.getResourceBundle();
        JFrame frame = new JFrame(rb.getString("Edicion de Conexion"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10));
        StylusUI.aplicarEstiloPanel(formPanel);

        JLabel equipo1 = new JLabel(rb.getString("Equipo origen"));
        StylusUI.aplicarEstiloEtiqueta(equipo1);
        formPanel.add(equipo1);
        eq1ComboBox = new JComboBox<>(coordinator.getHardware().values().toArray(new Equipment[0]));
        StylusUI.aplicarEstiloComboBox(eq1ComboBox);
        formPanel.add(eq1ComboBox);

        JLabel tipoPuerto1 = new JLabel(rb.getString("Puerto origen"));
        StylusUI.aplicarEstiloEtiqueta(tipoPuerto1);
        formPanel.add(tipoPuerto1);
        Equipment e1 = (Equipment) eq1ComboBox.getSelectedItem();
        assert e1 != null;
        port1ComboBox = new JComboBox<>(e1.getPortsNotInUse().toArray(new Port[0]));
        StylusUI.aplicarEstiloComboBox(port1ComboBox);
        formPanel.add(port1ComboBox);

        eq1ComboBox.addActionListener((e -> {
            Equipment selected1 = (Equipment) eq1ComboBox.getSelectedItem();
            if (selected1 != null) {
                port1ComboBox.setModel(new DefaultComboBoxModel<>(selected1.getPortsNotInUse().toArray(new Port[0])));
            }
        }));

        JLabel equipo2 = new JLabel(rb.getString("Equipo destino"));
        StylusUI.aplicarEstiloEtiqueta(equipo2);
        formPanel.add(equipo2);
        eq2ComboBox = new JComboBox<>(coordinator.getHardware().values().toArray(new Equipment[1]));
        StylusUI.aplicarEstiloComboBox(eq2ComboBox);
        formPanel.add(eq2ComboBox);

        JLabel tipoPuerto2 = new JLabel(rb.getString("Puerto destino"));
        StylusUI.aplicarEstiloEtiqueta(tipoPuerto2);
        formPanel.add(tipoPuerto2);
        Equipment e2 = (Equipment) eq1ComboBox.getSelectedItem();
        assert e2 != null;
        port2ComboBox = new JComboBox<>(e2.getPortsNotInUse().toArray(new Port[0]));
        StylusUI.aplicarEstiloComboBox(port2ComboBox);
        formPanel.add(port2ComboBox);

        eq2ComboBox.addActionListener((e -> {
            Equipment selected2 = (Equipment) eq2ComboBox.getSelectedItem();
            if (selected2 != null) {
                port2ComboBox.setModel(new DefaultComboBoxModel<>(selected2.getPortsNotInUse().toArray(new Port[0])));
            }
        }));

        JLabel wireType = new JLabel(rb.getString("Tipo de Cable"));
        StylusUI.aplicarEstiloEtiqueta(wireType);
        formPanel.add(wireType);
        wireComboBox = new JComboBox<>(coordinator.getWireTypes().values().toArray(new WireType[0]));
        StylusUI.aplicarEstiloComboBox(wireComboBox);
        formPanel.add(wireComboBox);


        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Agregar/Cambiar Conexion"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            Port port1 = (Port) port1ComboBox.getSelectedItem();
            Port port2 = (Port) port2ComboBox.getSelectedItem();
            WireType wire = (WireType) wireComboBox.getSelectedItem();

            try {
                assert port2 != null;
                assert port1 != null;

                coordinator.updateConnection(c,new Connection(port1, port2, wire));
                JOptionPane.showMessageDialog(this, rb.getString("Red creada con éxito"));
                frame.setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,rb.getString( "Error al crear equipo: ") + ex.getMessage());
            }

        });

        frame.add(this);
        frame.setVisible(true);

    }
    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {this.coordinator=coordinator;
    }
}