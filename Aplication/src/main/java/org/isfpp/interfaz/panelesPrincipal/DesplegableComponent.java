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
    private JScrollPane scrollPane;
    private PanelDerecho panelDerecho;
    private List<T> dataList;
    private Web web;

    public DesplegableComponent(String titulo, List<T> dataList, PanelDerecho panelDerecho, Web web) {
        this.web = web;
        this.dataList = dataList;
        this.panelDerecho = panelDerecho;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        toggleButton = new JButton("▶ " + titulo);
        StylusUI.aplicarEstiloBoton(toggleButton, false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        toggleButton.addActionListener(e -> toggle());

        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getDescription();
                data[i][2] = equipment;
            } else if (item instanceof Location) {
                Location location = (Location) item;
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

        String[] columnNames = {"Nombre", "Descripción/IP", "Objeto"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactivar edición en la tabla
            }
        };

        table = new JTable(model);
        StylusUI.aplicarEstiloTabla(table, false);
        table.setVisible(false);

        // Ocultar la tercera columna que contiene el objeto completo
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);

        scrollPane = new JScrollPane(table);
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
                                panelDerecho.updateProperties(selectedEquipment.toString(), selectedEquipment.getEquipmentType().getCode());
                        case Location selectedLocation ->
                                panelDerecho.updateProperties(STR."\{selectedLocation.getCode()} \{selectedLocation.getDescription()}", "help");
                        case Connection selectedConnection ->
                                panelDerecho.updateProperties(STR."\{selectedConnection.getPort1()} - \{selectedConnection.getPort2()}", selectedConnection.getWire().getCode());
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
        toggleButton.setText(isExpanded ? "▼ " + toggleButton.getText().substring(2) : "▶ " + toggleButton.getText().substring(2));
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
            if (selectedItem instanceof Equipment) {
                Equipment equipment = (Equipment) selectedItem;
                web.getHardware().remove(equipment.getCode());
            } else if (selectedItem instanceof Location) {
                Location location = (Location) selectedItem;
                web.getLocations().remove(location.getCode());
            } else if (selectedItem instanceof Connection) {
                Connection connection = (Connection) selectedItem;
                web.getConnections().remove(connection);
            }
            updateTable();
        }
    }

    private void updateTable() {
        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getDescription();
                data[i][2] = equipment;
            } else if (item instanceof Location) {
                Location location = (Location) item;
                data[i][0] = location.getCode();
                data[i][1] = location.getDescription();
                data[i][2] = location;
            } else if (item instanceof Connection) {
                data[i][0] = ((Connection) item).getPort1() + " - " + ((Connection) item).getPort2().getEquipment().getCode();
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

        // Volver a ocultar la tercera columna
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);
    }
}
