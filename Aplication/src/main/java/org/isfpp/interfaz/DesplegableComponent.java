package org.isfpp.interfaz;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Web;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DesplegableComponent {
    private boolean isExpanded = false;
    private final JPanel panel;
    private final JButton toggleButton;
    private final JTable table;
    private JScrollPane scrollPane;
    private PanelDerecho panelDerecho;  // Referencia al PanelDerecho

    public DesplegableComponent(String titulo, Web web, PanelDerecho panelDerecho) {
        this.panelDerecho = panelDerecho;  // Inicializar referencia al PanelDerecho

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        toggleButton = new JButton("▶ " + titulo);
        StylusUI.aplicarEstiloBoton(toggleButton,false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        toggleButton.addActionListener(e -> toggle());
        
        List<Equipment> equipmentList = new ArrayList<>(web.getHardware().values());
        Object[][] data = new Object[equipmentList.size()][2];

        for (int i = 0; i < equipmentList.size(); i++) {
            Equipment equipment = equipmentList.get(i);
            data[i][0] = equipment.getCode();
            data[i][1] = equipment.getIp();  
        }

        String[] columnNames = {"Nombre", "IP"};

        table = new JTable(data, columnNames);
        StylusUI.aplicarEstiloTabla(table,false);
        table.setVisible(false);


        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        scrollPane = new JScrollPane(table);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        scrollPane.setColumnHeaderView(table.getTableHeader());


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    Equipment selectedEquipment = equipmentList.get(row);
                    panelDerecho.updateProperties(selectedEquipment.toString());
                    panelDerecho.setIcon(selectedEquipment.getDescription().toLowerCase());

                }
            }
        });

        panel.add(toggleButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        toggle();
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
