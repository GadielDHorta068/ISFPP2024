package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.Guardar;
import org.isfpp.interfaz.panelesAddons.ConnectionIssues;
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
import java.io.File;
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

        cargarItem.addActionListener(e -> {

            System.out.println("Cargar seleccionado");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                String directory = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println("Cargar seleccionado: " + directory);

                try {
                    Cargar cargar = new Cargar();
                    coordinator.setWeb(cargar.cargarRedDesdeDirectorio(directory));
                    coordinator.updateTablas();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        guardarItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                String directory = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println("Guardar seleccionado: " + directory);
                File selectedDirectory = fileChooser.getSelectedFile().getAbsoluteFile();
                File dataDir = new File(selectedDirectory, "data");
                if (!dataDir.exists()) {
                    dataDir.mkdirs();  // Crea la subcarpeta 'data' si no existe
                }
                System.out.println("Carpeta 'data' creada en: " + dataDir.getAbsolutePath());

                try {
                    Guardar guardar = new Guardar();
                    guardar.saveAll(web, directory);
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
        JMenuItem agregarConItem = new JMenuItem("Agregar Conexion");
        StylusUI.styleMenuItem(agregarConItem);
        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        StylusUI.styleMenuItem(eliminarItem);
        JMenuItem verGrafo = new JMenuItem("Visualizar Grafo");
        StylusUI.styleMenuItem(verGrafo);
        JMenuItem traceRouter = new JMenuItem("Traceroute");
        StylusUI.styleMenuItem(traceRouter);
        JMenuItem cambiarEstado = new JMenuItem("On/Off Equipo");
        StylusUI.styleMenuItem(cambiarEstado);
        JMenuItem editarItem = new JMenuItem("Editar");
        StylusUI.styleMenuItem(editarItem);

        cambiarEstado.addActionListener(e -> {
            Object editar = coordinator.getSelectedItem();
            if(editar instanceof Equipment eq){
                eq.setStatus(!eq.isStatus());
              //  JOptionPane op = new JOptionPane(eq.getCode() + "estado activo cambiado a " + eq.isStatus());
                coordinator.updateTablas();
            }
        });

        editarItem.addActionListener(e -> {
            Object editar = coordinator.getSelectedItem();
            switch (editar) {
                case Equipment equipment -> new EditEquipmentFormPanel(web, equipment.getCode());
                case Location location -> new EditLocationFormPanel(web, location.getCode());
                case PortType puerto -> new EditPortTypeFormPanel(web, puerto.getCode());
                case Connection connection -> new EditConnection(web, connection);
                case null, default -> {
                    assert editar != null;
                    System.out.println("Clase no detectada " +editar.getClass());
                }

            }
        });

        agregarConItem.addActionListener(e -> new EditConnection(web, null));

        agregarUbicacionItem.addActionListener(e -> {
            new LocationFormPanel(web);
            coordinator.updateTablas();
        });
        agregarEquipoItem.addActionListener(e -> new EquipmentFormPanel(web));


        agregarPuertoItem.addActionListener(e -> new PortTypeFormPanel(web));

        agregarUbicacionItem.addActionListener(e -> new LocationFormPanel(web));

        verGrafo.addActionListener(e -> new VisualizarGrafo(web.getHardware(), web.getConnections()));

        eliminarItem.addActionListener(e -> {
            Object editar = coordinator.getSelectedItem();
            switch (editar) {
                case Equipment equipment -> web.eraseEquipment(equipment);
                case Location location -> web.eraseLocation(location);
                case PortType puerto -> web.erasePort(puerto);
                case Connection connection -> web.eraseConnection(connection);
                case null, default -> {
                    assert editar != null;
                    System.out.println("Clase no detectada " +editar.getClass());
                }
            }
            coordinator.updateTablas();
        });

        editarMenu.add(agregarEquipoItem);
        editarMenu.add(agregarPuertoItem);
        editarMenu.add(agregarUbicacionItem);
        editarMenu.add(agregarConItem);
        editarMenu.add(eliminarItem);
        editarMenu.add(editarItem);

        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        JMenu herramientasMenu = new JMenu("Herramientas");
        JMenuItem ipScan = new JMenuItem("Ping en rango");
        JMenuItem ipList = new JMenuItem("Equipos Activos");
        JMenuItem connectionIssues = new JMenuItem("Conexiones");
        herramientasMenu.add(ipScan);
       herramientasMenu.add(ipList);
       herramientasMenu.add(cambiarEstado);
        herramientasMenu.add(connectionIssues);
        StylusUI.styleMenuItem(connectionIssues);
        StylusUI.styleMenuItem(ipScan);
        StylusUI.styleMenuItem(ipList);
        StylusUI.styleMenu(herramientasMenu);

        herramientasMenu.add(verGrafo);
        herramientasMenu.add(traceRouter);
        ipList.addActionListener(e -> {
            PingListEquipment pingListEquipment = new PingListEquipment();
            pingListEquipment.setCoordinator(coordinator);
            pingListEquipment.ping();
        });


        traceRouter.addActionListener(e -> {
            Traceroute traceroute = new Traceroute();
            traceroute.setCoordinator(coordinator);
            traceroute.trace();
        });
        connectionIssues.addActionListener(e -> {
            ConnectionIssues connection = new ConnectionIssues();
            connection.setCoordinator(coordinator);
            connection.scanIp();
        });


        ipScan.addActionListener(e -> {
            IPFrame ipFrame = new IPFrame();
            ipFrame.setCoordinator(coordinator);
            ipFrame.scanIp();
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

