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

        Object[][] data = new Object[dataList.size()][2];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getIpAdresses();
            } else if (item instanceof Location) {
                Location location = (Location) item;
                data[i][0] = location.getCode();
                data[i][1] = location.getDescription();
            } else if (item instanceof Connection) {
                // Maneja el tipo Connection u otros tipos
                data[i][0] = STR."\{((Connection) item).getEquipment1()}\{((Connection) item).getEquipment2()}";
                data[i][1] = ((Connection) item).getWire();
            }
        }

        //Aca levantar el nombre del string del objeto
        String[] columnNames = {"Nombre", "IP/Descripción"};

        table = new JTable(data, columnNames);
        StylusUI.aplicarEstiloTabla(table, false);
        table.setVisible(false);

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
                    Equipment selectedEquipment = (Equipment) table.getValueAt(selectedRow, 0);
                    panelDerecho.updateProperties(selectedEquipment.toString(), selectedEquipment.getEquipmentType().getDescription());
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

    public void removeSelectedEquipment() {
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
        Object[][] data = new Object[dataList.size()][2];
        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            if (item instanceof Equipment) {
                Equipment equipment = (Equipment) item;
                data[i][0] = equipment.getCode();
                data[i][1] = equipment.getIpAdresses();
            } else if (item instanceof Location) {
                Location location = (Location) item;
                data[i][0] = location.getCode();
                data[i][1] = location.getDescription();
            } else if (item instanceof Connection) {
                data[i][0] = ((Connection) item).getEquipment1() + " - " + ((Connection) item).getEquipment2();
                data[i][1] = ((Connection) item).getWire();
            }
        }
        table.setModel(new DefaultTableModel(data, new String[]{"Nombre", "IP/Descripción"}));
    }
}
