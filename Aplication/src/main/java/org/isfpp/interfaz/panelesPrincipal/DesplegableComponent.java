package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.Web;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DesplegableComponent<T> {
    private boolean isExpanded = false;
    private final JPanel panel;
    private final JButton toggleButton;
    private final JTable table;
    private final List<T> dataList;
    private final Web web;

    public DesplegableComponent(String titulo, List<T> dataList, PanelDerecho panelDerecho, Web web) {
        this.web = web;
        this.dataList = dataList;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        toggleButton = new JButton(STR."▶ \{titulo}");
        StylusUI.aplicarEstiloBoton(toggleButton, false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        //toggleButton.addActionListener(e -> toggle());

        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment equipment) {
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getDescription();
                data[i][2] = equipment;
            } else if (item instanceof Location location) {
                data[i][0] = location.getCode();
                data[i][1] = location.getDescription();
                data[i][2] = location;
            } else if (item instanceof Connection) {
                data[i][0] = STR."\{((Connection) item).getPort1().getPortType().getCode()} - \{((Connection) item).getPort2().getPortType().getCode()}";
                data[i][1] = ((Connection) item).getWire().getDescription();
                data[i][2] = item;
            } else {
                data[i][0] = item.toString();
                data[i][1] = "";
                data[i][2] = item;
            }
        }

        String[] columnNames = {"Nombre", "Descripción", "Objeto"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        StylusUI.aplicarEstiloTabla(table, false);
        table.setVisible(false);

        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        scrollPane.setColumnHeaderView(table.getTableHeader());

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        toggle();

        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    T selectedItem = (T) table.getValueAt(selectedRow, 2);
                    switch (selectedItem) {
                        case Equipment selectedEquipment ->
                                panelDerecho.updateProperties(selectedEquipment.toString().replace("-", "\n"), selectedEquipment.getEquipmentType().getCode());
                        case Location selectedLocation ->
                                panelDerecho.updateProperties(selectedLocation.toString().replace("-", "\n"), "LOC");
                        case Connection selectedConnection ->
                                panelDerecho.updateProperties(String.format("%s", selectedConnection.toString()), selectedConnection.getWire().getCode());
                        case null, default ->
                            // Fallback para cualquier otro tipo de objeto
                                panelDerecho.updateProperties(selectedItem.toString(), "Descripción no disponible");
                    }
                }
            }
        });
    }

    private void toggle() {
        isExpanded = !isExpanded;
        table.setVisible(isExpanded);
        table.getTableHeader().setVisible(isExpanded);
        toggleButton.setText(isExpanded ? STR."▼ \{toggleButton.getText().substring(2)}" : "▶ " + toggleButton.getText().substring(2));
        panel.revalidate();
        panel.repaint();
    }

    public JPanel getPanel() {
        return panel;
    }

    public void removeSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            T selectedItem = dataList.get(selectedRow);
            dataList.remove(selectedRow);
            if (selectedItem instanceof Equipment equipment) {
                web.getHardware().remove(equipment.getCode());
            } else if (selectedItem instanceof Location location) {
                web.getLocations().remove(location.getCode());
            } else if (selectedItem instanceof Connection connection) {
                web.getConnections().remove(connection);
            }
            updateTable();
        }
    }

    private void updateTable() {
        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment equipment) {
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getDescription();
                data[i][2] = equipment;
            } else if (item instanceof Location location) {
                data[i][0] = location.getCode();
                data[i][1] = location.getDescription();
                data[i][2] = location;
            } else if (item instanceof Connection) {
                data[i][0] = STR."\{((Connection) item).getPort1()} - \{((Connection) item).getPort2().getEquipment().getCode()}";
                data[i][1] = ((Connection) item).getWire().getDescription();
                data[i][2] = item;
            } else {
                data[i][0] = item.toString();
                data[i][1] = "";
                data[i][2] = item;
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, new String[]{"Nombre", "Descripción", "Objeto"});
        table.setModel(model);

        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);
    }
}
