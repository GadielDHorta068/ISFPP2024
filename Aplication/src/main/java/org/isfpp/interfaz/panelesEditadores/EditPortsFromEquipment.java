package org.isfpp.interfaz.panelesEditadores;

import org.apache.log4j.Logger;
import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.logica.Lan;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Port;
import org.isfpp.modelo.PortType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditPortsFromEquipment extends JPanel {
    private final static org.apache.log4j.Logger logger = Logger.getLogger(EditPortsFromEquipment.class);
    private Coordinator coordinator;
    private ResourceBundle rb;

    public void run(String codeOriginal) {
        rb = coordinator.getResourceBundle();

        JFrame frame = new JFrame(rb.getString("modificar_puertos"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        setLayout(new BorderLayout());

        List<PortType> portTypes = new ArrayList<>(coordinator.getPortTypes().values());
        Equipment equipment = coordinator.getHardware().get(codeOriginal);
        Map<PortType, Integer> portQuantities = equipment.getAllPortsTypes();

        JPanel formPanel = new JPanel(new GridLayout(portTypes.size() + 1, 2, 10, 20));
        StylusUI.aplicarEstiloPanel(formPanel);

        // Encabezados de las columnas
        JLabel tipoPuertoLabel = new JLabel("Tipo de Puerto");
        JLabel cantidadLabel = new JLabel("Cantidad");
        StylusUI.aplicarEstiloEtiqueta(tipoPuertoLabel);
        StylusUI.aplicarEstiloEtiqueta(cantidadLabel);
        formPanel.add(tipoPuertoLabel);
        formPanel.add(cantidadLabel);

        // Generar filas para cada tipo de puerto
        for (PortType portType : portTypes) {
            JLabel portTypeLabel = new JLabel(portType.getCode());
            StylusUI.aplicarEstiloEtiqueta(portTypeLabel);
            formPanel.add(portTypeLabel);

            // Panel para contener el botón -, el campo de texto y el botón +
            JPanel quantityPanel = new JPanel(new BorderLayout());
            quantityPanel.setPreferredSize(new Dimension(100, 30)); // Ajusta el tamaño según necesites

            // Obtener la cantidad del puerto desde el equipo
            int initialQuantity = portQuantities.getOrDefault(portType, 0);
            JTextField quantityField = new JTextField(String.valueOf(initialQuantity), 3);
            quantityField.setHorizontalAlignment(JTextField.CENTER); // Ajuste centrado
            quantityField.setEditable(false); // Campo solo lectura
            StylusUI.aplicarEstiloCampoTexto(quantityField);

            // Botón para disminuir la cantidad
            JButton decrementButton = new JButton("-");
            StylusUI.aplicarEstiloBoton(decrementButton, true);
            decrementButton.addActionListener(e -> {
                int currentQuantity = Integer.parseInt(quantityField.getText());

                // Obtener el número de puertos ocupados para este tipo específico
                long occupiedPorts = equipment.getPortsNotInUse().stream()
                        .filter(port -> port.getPortType().equals(portType) && port.isInUse())
                        .count();

                System.out.println(currentQuantity + "---"+ occupiedPorts + equipment.getPortsNotInUse());
                // Verificar si se puede decrementar
                if (currentQuantity > occupiedPorts) {
                    Port portToRemove = equipment.checkPort(portType);
                    if (portToRemove != null) {
                        equipment.deletePort(portToRemove);
                        coordinator.updateEquipment(codeOriginal, equipment);
                        int newQuantity = currentQuantity - 1;
                        quantityField.setText(String.valueOf(newQuantity));
                    }
                } else {
                    JOptionPane.showMessageDialog(this, rb.getString("No_se_puede_disminuir_puerto_ocupado"));
                }
            });

            // Botón para incrementar la cantidad
            JButton incrementButton = new JButton("+");
            StylusUI.aplicarEstiloBoton(incrementButton, true);
            incrementButton.addActionListener(e -> {
                equipment.addPort(portType);
                coordinator.updateEquipment(codeOriginal, equipment);
                int newQuantity = Integer.parseInt(quantityField.getText()) + 1;
                quantityField.setText(String.valueOf(newQuantity));
            });

            quantityPanel.add(decrementButton, BorderLayout.WEST);
            quantityPanel.add(quantityField, BorderLayout.CENTER);
            quantityPanel.add(incrementButton, BorderLayout.EAST);
            formPanel.add(quantityPanel);
        }

        add(formPanel, BorderLayout.CENTER);

        JButton createButton = new JButton(rb.getString("Modificar_tipo_Puerto"));
        StylusUI.aplicarEstiloBoton(createButton, true);
        add(createButton, BorderLayout.SOUTH);

        createButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, rb.getString("Tipo_de_puerto_modificado_exito"));
            logger.info("puerto editado, equipo: "+ equipment.getCode());
            frame.setVisible(false);
        });

        frame.add(this);
        frame.pack();
        frame.setSize(470,frame.getHeight());
        frame.setVisible(true);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
