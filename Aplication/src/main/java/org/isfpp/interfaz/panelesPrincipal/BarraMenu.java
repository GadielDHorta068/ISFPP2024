package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Guardar;
import org.isfpp.interfaz.panelesAddons.IPFrame;
import org.isfpp.interfaz.panelesAddons.VisualizarGrafo;
import org.isfpp.interfaz.panelesCreadores.EquipmentFormPanel;
import org.isfpp.interfaz.panelesCreadores.LocationFormPanel;
import org.isfpp.interfaz.panelesCreadores.PortTypeFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditConnection;
import org.isfpp.interfaz.panelesEditadores.EditEquipmentFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditLocationFormPanel;
import org.isfpp.interfaz.panelesEditadores.EditPortTypeFormPanel;
import org.isfpp.interfaz.panelesAddons.PingListEquipment;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.interfaz.panelesAddons.Traceroute;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BarraMenu {
    private final Web web;
    private Coordinator coordinator;

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

        cargarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para cargar
                System.out.println("Cargar seleccionado");
            }
        });

        guardarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String directory = fileChooser.getSelectedFile().getAbsolutePath();
                    System.out.println("Guardar seleccionado: " + directory);

                    // Actualizar las rutas de los archivos en el objeto Guardar
                    try {
                        Guardar guardar = new Guardar("config.properties");
                        //guardar.saveAll();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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
        JMenuItem agregarConItem = new JMenuItem("Agregar Conexion");
        StylusUI.styleMenuItem(agregarConItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        StylusUI.styleMenuItem(eliminarItem);
        JMenuItem verGrafo = new JMenuItem("Visualizar Grafo");
        StylusUI.styleMenuItem(verGrafo);
        JMenuItem traceRouter = new JMenuItem("Traceroute");
        StylusUI.styleMenuItem(traceRouter);

        JMenuItem editarItem = new JMenuItem("Editar");
        StylusUI.styleMenuItem(editarItem);

        editarItem.addActionListener(e -> {
            Object editar = coordinator.getSelectedItem();
            switch (editar) {
                case Equipment equipment -> new EditEquipmentFormPanel(web, equipment.getCode());
                case Location location -> new EditLocationFormPanel(web, location.getCode());
                case PortType puerto -> new EditPortTypeFormPanel(web, puerto.getCode());
                case Connection connection -> new EditConnection(web, connection);
                case null, default -> {
                    assert editar != null;
                    System.out.println(STR."Clase no detectada\{editar.getClass()}");
                }

            }
        });

        agregarConItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditConnection(web, null);
            }
        });

        agregarUbicacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LocationFormPanel(web);
                coordinator.updateTablas();
            }
        });
        agregarEquipoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EquipmentFormPanel(web);
            }
        });


        agregarPuertoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PortTypeFormPanel(web);
            }
        });

        agregarUbicacionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LocationFormPanel(web);
            }
        });

        verGrafo.addActionListener(_ -> new VisualizarGrafo(web.getHardware(), web.getConnections()));

        eliminarItem.addActionListener(e -> {
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
            coordinator.updateTablas();
        });

        //eliminarItem.addActionListener(e -> desplegableComponent.removeSelectedEquipment());
        editarMenu.add(agregarEquipoItem);
        editarMenu.add(agregarPuertoItem);
        editarMenu.add(agregarUbicacionItem);
        editarMenu.add(agregarConItem);
        editarMenu.add(eliminarItem);
        editarMenu.add(editarItem);
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        JMenu herramientasMenu = new JMenu("Herramientas");
        JMenuItem ipScan = new JMenuItem("Scan IP");
        JMenuItem ipList = new JMenuItem("lista IP");
        herramientasMenu.add(ipScan);
        herramientasMenu.add(ipList);
        StylusUI.styleMenuItem(ipScan);
        StylusUI.styleMenuItem(ipList);
        StylusUI.styleMenu(herramientasMenu);

        herramientasMenu.add(verGrafo);
        herramientasMenu.add(traceRouter);
        ipList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PingListEquipment pingListEquipment = new PingListEquipment();
                pingListEquipment.setCoordinator(coordinator);
                pingListEquipment.ping();
            }
        });


        traceRouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Traceroute traceroute = new Traceroute();
                traceroute.setCoordinator(coordinator);
                traceroute.trace();
            }
        });

        ipScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IPFrame ipFrame = new IPFrame();
                ipFrame.setCoordinator(coordinator);
                ipFrame.scanIp();
            }
        });

        menuBar.add(editarMenu);
        menuBar.add(ayudaMenu);
        menuBar.add(herramientasMenu);

        return menuBar;
    }


    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}

