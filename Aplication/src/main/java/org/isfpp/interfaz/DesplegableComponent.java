package org.isfpp.interfaz;

import org.isfpp.modelo.Equipment;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DesplegableComponent {
    private boolean isExpanded = false;
    private JPanel panel;
    private JButton toggleButton;
    private JTable table;
    private JScrollPane scrollPane;

    public DesplegableComponent(String titulo, List<Equipment> equipmentList) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        toggleButton = new JButton("▶ " + titulo);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        toggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggle();
            }
        });

        // Crear los datos de la tabla usando el nombre y la IP de los equipos
        String[] columnNames = {"Nombre", "IP"};
        Object[][] data = new Object[equipmentList.size()][2];
        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            data[i][0] = equipment.getName();  // Nombre del equipo
            data[i][1] = equipment.getIp();    // IP del equipo
        }

        // Crear la tabla con los datos de los equipos
        table = new JTable(data, columnNames);
        table.setVisible(false);

        // Alinear el texto de las celdas a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        // Crear un JScrollPane para la tabla
        scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void toggle() {
        isExpanded = !isExpanded;
        table.setVisible(isExpanded);
        table.getTableHeader().setVisible(isExpanded);
        toggleButton.setText(isExpanded ? "▼ " + toggleButton.getText().substring(2) : "▶ " + toggleButton.getText().substring(2));
        panel.revalidate();
        panel.repaint();
    }

    public JPanel getPanel() {
        return panel;
    }
}
