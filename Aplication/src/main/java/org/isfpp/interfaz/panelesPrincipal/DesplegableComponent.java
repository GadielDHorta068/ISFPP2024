package org.isfpp.interfaz.panelesPrincipal;
import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.*;
import org.isfpp.logica.Lan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clase que representa un componente desplegable genérico para mostrar datos en una tabla.
 * @param <T> El tipo de los elementos que se mostrarán en la tabla.
 */
public class DesplegableComponent<T> {
    private boolean isExpanded = false;
    private JPanel panel;
    private JButton toggleButton;
    private JTable table;
    private List<T> dataList;
    private Coordinator coordinator;
    private Lan lan;
    private ResourceBundle rb;
    private PanelDerecho panelDerecho;

    /**
     * Constructor de la clase DesplegableComponent.
     */
    public DesplegableComponent() {
     //   this.rb = coordinator.getResourceBundle();
    }

    /**
     * Método para alternar la visibilidad de la tabla.
     */
    private void toggle() {
        isExpanded = !isExpanded;
        table.setVisible(isExpanded);
        table.getTableHeader().setVisible(isExpanded);
        toggleButton.setText(isExpanded ? "▼ " + toggleButton.getText().substring(2) : "▶ " + toggleButton.getText().substring(2));
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Método para obtener el panel principal del componente.
     * @return El panel principal.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Método para actualizar los datos de la tabla.
     */
    public void updateTable() {
        rb=coordinator.getResourceBundle();
        // Inicializa dataList basado en el tipo del primer elemento
        Object e = dataList.isEmpty() ? null : dataList.get(0);

        switch (e) {
            case Equipment eq ->
                    dataList = (List<T>) new ArrayList<>(lan.getHardware().values());
            case Location loc ->
                    dataList = (List<T>) new ArrayList<>(lan.getLocations().values());
            case Connection con ->
                    dataList = (List<T>) new ArrayList<>(lan.getConnections());
            case WireType wire ->
                    dataList = (List<T>) new ArrayList<>(lan.getWireTypes().values());
            case PortType port ->
                    dataList = (List<T>) new ArrayList<>(lan.getPortTypes().values());
            case EquipmentType eqType ->
                dataList = (List<T>) new ArrayList<>(lan.getEquipmentTypes().values());
            default -> throw new IllegalStateException("Unexpected value: " + e);
        }

        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            switch (item) {
                case Equipment equipment -> {
                    data[i][0] = equipment.getCode();
                    data[i][1] = equipment.getDescription();
                    data[i][2] = equipment;
                }
                case Location location -> {
                    data[i][0] = location.getCode();
                    data[i][1] = location.getDescription();
                    data[i][2] = location;
                }
                case Connection connection -> {
                    data[i][0] = String.format("%s - %s - %s - %s",
                            connection.getPort1().getEquipment().getCode(),
                            connection.getPort1().getPortType().getCode(),
                            connection.getPort2().getPortType().getCode(),
                            connection.getPort2().getEquipment().getCode());
                    data[i][1] = connection.getWire().getDescription();
                    data[i][2] = connection;
                }
                case PortType portType ->{
                    data[i][0] = portType.getCode();
                    data[i][1] = String.format("%s-%s", portType.getDescription(),portType.getSpeed());
                    data[i][2] = portType;
                }
                case WireType wireType ->{
                    data[i][0] = wireType.getCode();
                    data[i][1] = String.format("%s-%s", wireType.getDescription(),wireType.getSpeed());
                    data[i][2] = wireType;
                }
                case EquipmentType equipmentType ->{
                    data[i][0] = equipmentType.getCode();
                    data[i][1] = equipmentType.getDescription();
                    data[i][2] = equipmentType;
                }

                case null, default -> {
                    data[i][0] = item.toString();
                    data[i][1] = "";
                    data[i][2] = item;
                }
            }
        }

        // Aquí vuelves a crear el modelo, pero asegúrate de que siga siendo no editable
        DefaultTableModel model = new DefaultTableModel(data, new String[]{rb.getString("nombre"), rb.getString("descripcion"), rb.getString("objeto")}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Asignar el nuevo modelo a la tabla
        table.setModel(model);

        // Esconder la columna de "Objeto" nuevamente
        table.getColumnModel().getColumn(2).setMinWidth(0);
        table.getColumnModel().getColumn(2).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(0);

        // Deshabilitar editores de celdas para evitar la edición
        table.setDefaultEditor(Object.class, null);

        // Forzar repintado de la tabla
        table.repaint();
    }

    /**
     * Método para establecer el coordinador del componente.
     * @param coordinator El objeto coordinador.
     */
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Método para inicializar la tabla con datos.
     * @param titulo El título del componente desplegable.
     * @param dataList La lista de datos a mostrar en la tabla.
     * @param panelDerecho El panel derecho donde se mostrarán propiedades adicionales.
     */
    public void IniciarTabla(String titulo, List<T> dataList, PanelDerecho panelDerecho) {
        this.lan = this.coordinator.getWeb();
        this.dataList = dataList;
        rb = coordinator.getResourceBundle();

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        toggleButton = new JButton("▶ " + titulo);
        StylusUI.aplicarEstiloBoton(toggleButton, false);
        toggleButton.setHorizontalAlignment(SwingConstants.LEFT);
        //toggleButton.addActionListener(e -> toggle());

        Object[][] data = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            T item = dataList.get(i);
            switch (item) {
                case Equipment equipment -> {
                    data[i][0] = equipment.getCode();
                    data[i][1] = equipment.getDescription();
                    data[i][2] = equipment;
                }
                case Location location -> {
                    data[i][0] = location.getCode();
                    data[i][1] = location.getDescription();
                    data[i][2] = location;
                }
                case Connection connection -> {
                    data[i][0] = String.format("%s - %s - %s - %s",
                            connection.getPort1().getEquipment().getCode(),
                            connection.getPort1().getPortType().getCode(),
                            connection.getPort2().getPortType().getCode(),
                            connection.getPort2().getEquipment().getCode());
                    data[i][1] = connection.getWire().getDescription();
                    data[i][2] = item;
                }
                case PortType portType ->{
                    data[i][0] = portType.getCode();
                    data[i][1] = String.format("%s <-> %s", portType.getDescription(),portType.getSpeed());
                    data[i][2] = portType;
                }
                case WireType wireType ->{
                    data[i][0] = wireType.getCode();
                    data[i][1] = String.format("%s <-> %s", wireType.getDescription(),wireType.getSpeed());
                    data[i][2] = wireType;
                }
                case EquipmentType equipmentType ->{
                    data[i][0] = equipmentType.getCode();
                    data[i][1] = equipmentType.getDescription();
                    data[i][2] = equipmentType;
                }
                case null, default -> {
                    assert item != null;
                    data[i][0] = item.toString();
                    data[i][1] = "";
                    data[i][2] = item;
                }
            }
        }

        String[] columnNames = {rb.getString("nombre"), rb.getString("descripcion"), rb.getString("objeto")};

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
                    coordinator.setSelectedItem(selectedItem);
                    switch (selectedItem) {
                        case Equipment selectedEquipment ->
                                panelDerecho.updateProperties(selectedEquipment);
                        case Location selectedLocation ->
                                panelDerecho.updateProperties(selectedLocation);
                        case Connection selectedConnection ->
                                panelDerecho.updateProperties(selectedConnection);
                        case WireType selectedWire ->
                                panelDerecho.updateProperties(selectedWire);
                        case PortType selectedPort ->
                                panelDerecho.updateProperties(selectedPort);
                        case EquipmentType selectedEqType ->
                                panelDerecho.updateProperties(selectedEqType);
                        case null, default ->
                            // Fallback para cualquier otro tipo de objeto
                                panelDerecho.updateProperties(selectedItem.toString(), rb.getString("descripcion_no_disponible"));
                    }
                }
            }
        });
    }
}
