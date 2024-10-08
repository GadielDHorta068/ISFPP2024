package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Guardar;
import org.isfpp.interfaz.IPFrame;
import org.isfpp.interfaz.VisualizarGrafo;
import org.isfpp.interfaz.panelesCreadores.EquipmentFormPanel;
import org.isfpp.interfaz.panelesCreadores.LocationFormPanel;
import org.isfpp.interfaz.panelesCreadores.PortTypeFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditEquipmentFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditLocationFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditPortTypeFormPanel;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BarraMenu {
    private final Web web;
    private Coordinator coordinator;
    //Obtener en una variable el item seleccionado de las tablas

    public BarraMenu(Web web) {
        this.web = web;
    }

    public JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        StylusUI.styleMenuBar(menuBar);

        JMenu archivoMenu = new JMenu("Archivo");
        StylusUI.styleMenu(archivoMenu);
        JMenuItem cargarItem = new JMenuItem("Cargar");
        StylusUI.styleMenuItem(cargarItem);
        JMenuItem guardarItem = new JMenuItem("Guardar");
        StylusUI.styleMenuItem(guardarItem);
        JMenuItem salirItem = new JMenuItem("Salir");
        StylusUI.styleMenuItem(salirItem);

        cargarItem.addActionListener(e -> {
            // Lógica para cargar
            System.out.println("Cargar seleccionado");
        });

        guardarItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                String directory = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println(STR."Guardar seleccionado: \{directory}");

                // Actualizar las rutas de los archivos en el objeto Guardar
                try {
                    Guardar guardar = new Guardar("config.properties");
                    //guardar.saveAll();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        salirItem.addActionListener(e -> {
            // Lógica para salir
            System.exit(0);
        });

        archivoMenu.add(cargarItem);
        archivoMenu.add(guardarItem);
        archivoMenu.add(salirItem);

        menuBar.add(archivoMenu);

        JMenu editarMenu = new JMenu("Editar");
        StylusUI.styleMenu(editarMenu);

        JMenuItem agregarEquipoItem = new JMenuItem("Agregar Equipo");
        StylusUI.styleMenuItem(agregarEquipoItem);
        JMenuItem agregarPuertoItem = new JMenuItem("Agregar tipo Puerto");
        StylusUI.styleMenuItem(agregarPuertoItem);
        JMenuItem agregarUbicacionItem = new JMenuItem("Agregar Ubicacion");
        StylusUI.styleMenuItem(agregarUbicacionItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        StylusUI.styleMenuItem(eliminarItem);
        JMenuItem verGrafo = new JMenuItem("Visualizar Grafo");
        StylusUI.styleMenuItem(verGrafo);

        JMenuItem editarItem = new JMenuItem("Editar");
        StylusUI.styleMenuItem(editarItem);

        editarItem.addActionListener(e -> {
            Object editar = coordinator.getSelectedItem();
            switch (editar) {
                case Equipment equipment -> new EditEquipmentFormPanel(web, equipment.getCode());
                case Location location -> new EditLocationFormPanel(web, location.getCode());
                case PortType puerto -> new EditPortTypeFormPanel(web, puerto.getCode());
                case null, default -> {
                    assert editar != null;
                    System.out.println(STR."Clase no detectada\{editar.getClass()}");
                }
            }

        });
        agregarEquipoItem.addActionListener(e -> new EquipmentFormPanel(web));

        agregarPuertoItem.addActionListener(e -> new PortTypeFormPanel(web));

        agregarUbicacionItem.addActionListener(_ -> new LocationFormPanel(web));

        verGrafo.addActionListener(_ -> new VisualizarGrafo(web.getHardware() , web.getConnections()));

        eliminarItem.addActionListener(e ->{
            Object editar = coordinator.getSelectedItem();
            switch (editar) {
                case Equipment equipment -> web.eraseEquipment(equipment);
                case Location location -> web.eraseLocation(location);
                case PortType puerto -> web.erasePort(puerto);
                case Connection connection -> web.eraseConnection(connection);
                case null, default -> {
                    assert editar != null;
                    System.out.println(STR."Clase no detectada\{editar.getClass()}");
                }
            }
                });
        editarMenu.add(agregarEquipoItem);
        editarMenu.add(agregarPuertoItem);
        editarMenu.add(agregarUbicacionItem);
        editarMenu.add(eliminarItem);
        editarMenu.add(editarItem);
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        JMenu herramientasMenu = new JMenu("Herramientas");
        JMenuItem ipScan = new JMenuItem("Scan IP");
        herramientasMenu.add(ipScan);
        StylusUI.styleMenuItem(ipScan);
        StylusUI.styleMenu(herramientasMenu);
        herramientasMenu.add(verGrafo);


        ipScan.addActionListener(e -> new IPFrame());

        menuBar.add(editarMenu);
        menuBar.add(ayudaMenu);
        menuBar.add(herramientasMenu);

        return menuBar;
    }
    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
