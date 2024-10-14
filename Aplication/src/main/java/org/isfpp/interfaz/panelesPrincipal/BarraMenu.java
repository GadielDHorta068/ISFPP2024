package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.Guardar;
import org.isfpp.interfaz.panelesAddons.*;
import org.isfpp.interfaz.panelesCreadores.*;
import org.isfpp.interfaz.panelesEditadores.*;
import org.isfpp.modelo.*;
import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Objects;


public class BarraMenu {
    private final Web web;
    private Coordinator coordinator;

    public BarraMenu(Web web) {
        this.web = web;
    }

    public JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        StylusUI.styleMenuBar(menuBar);

        menuBar.add(crearArchivoMenu());
        menuBar.add(crearEditarMenu());
        menuBar.add(crearAyudaMenu());
        menuBar.add(crearHerramientasMenu());

        return menuBar;
    }

    private JMenu crearArchivoMenu() {
        JMenu archivoMenu = new JMenu("Archivo");
        StylusUI.styleMenu(archivoMenu);

        archivoMenu.add(crearMenuItem("Cargar", this::accionCargar));
        archivoMenu.add(crearMenuItem("Guardar", this::accionGuardar));
        archivoMenu.add(crearMenuItem("Salir", e -> System.exit(0)));

        return archivoMenu;
    }

    private void accionCargar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                Cargar cargar = new Cargar();
                coordinator.setWeb(cargar.cargarRedDesdeDirectorio(directory));
                coordinator.updateTablas();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void accionGuardar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            File dataDir = new File(fileChooser.getSelectedFile(), "data");
            try {
                Guardar guardar = new Guardar();
                guardar.saveAll(web, directory);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private JMenu crearEditarMenu() {
        JMenu editarMenu = new JMenu("Editar");
        StylusUI.styleMenu(editarMenu);

        editarMenu.add(crearMenuItem("Agregar Equipo", e -> new EquipmentFormPanel(web)));
        editarMenu.add(crearMenuItem("Agregar tipo Puerto", e -> new PortTypeFormPanel(web)));
        editarMenu.add(crearMenuItem("Agregar Ubicacion", e -> new LocationFormPanel(web)));
        editarMenu.add(crearMenuItem("Agregar Conexion", e -> new EditConnection(web, null)));
        editarMenu.add(crearMenuItem("Eliminar", this::accionEliminar));
        editarMenu.add(crearMenuItem("Editar", this::accionEditar));

        return editarMenu;
    }

    private void accionEliminar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> web.eraseEquipment(equipment);
                case Location location -> web.eraseLocation(location);
                case PortType puerto -> web.erasePort(puerto);
                case Connection connection -> web.eraseConnection(connection);
                default -> System.out.println("Clase no detectada: " + seleccionado.getClass());
            }
            coordinator.updateTablas();
        }
    }

    private void accionEditar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> new EditEquipmentFormPanel(web, equipment.getCode());
                case Location location -> new EditLocationFormPanel(web, location.getCode());
                case PortType puerto -> new EditPortTypeFormPanel(web, puerto.getCode());
                case Connection connection -> new EditConnection(web, connection);
                default -> System.out.println("Clase no detectada: " + seleccionado.getClass());
            }
        }
    }

    private JMenu crearAyudaMenu() {
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);

        ayudaMenu.add(crearMenuItem("Como Usar", e -> abrirManual()));
        ayudaMenu.add(crearMenuItem("Acerca de", e -> acercaDe()));
        return ayudaMenu;
    }

    private void acercaDe() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                URI uri = new URI("https://github.com/GadielDHorta068/ISFPP2024");
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void abrirManual() {
        InputStream pdfStream = getClass().getResourceAsStream("/Manual.pdf");

        if (pdfStream == null) {
            System.out.println("No se pudo encontrar el archivo manual.pdf");
            return;
        }

        try {
            File tempFile = File.createTempFile("manual", ".pdf");
            tempFile.deleteOnExit();

            // Escribe el contenido del PDF en el archivo temporal
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfStream.read(buffer)) != -1) {
                    fos.write(buffer);
                }
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                System.out.println("Desktop no es compatible en este sistema.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pdfStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private JMenu crearHerramientasMenu() {
        JMenu herramientasMenu = new JMenu("Herramientas");
        StylusUI.styleMenu(herramientasMenu);

        herramientasMenu.add(crearMenuItem("Ping en rango", e -> iniciarPing()));
        herramientasMenu.add(crearMenuItem("Equipos Activos", e -> iniciarPingEquipos()));
        herramientasMenu.add(crearMenuItem("Conexiones", e -> iniciarConnectionIssues()));
        herramientasMenu.add(crearMenuItem("On/Off Equipo", e -> cambiarEstado()));
        herramientasMenu.add(crearMenuItem("Visualizar Grafo", e -> new VisualizarGrafo(web.getHardware(), web.getConnections())));
        herramientasMenu.add(crearMenuItem("Traceroute", e -> iniciarTraceroute()));

        return herramientasMenu;
    }

    private void cambiarEstado() {
        if (coordinator.getSelectedItem() instanceof Equipment eq) {
            eq.setStatus(!eq.isStatus());
            coordinator.updateTablas();
        }
    }

    private void iniciarPing() {
        IPFrame ipFrame = new IPFrame();
        ipFrame.setCoordinator(coordinator);
        ipFrame.scanIp();
    }

    private void iniciarPingEquipos() {
        PingListEquipment pingList = new PingListEquipment();
        pingList.setCoordinator(coordinator);
        pingList.ping();
    }

    private void iniciarConnectionIssues() {
        ConnectionIssues connection = new ConnectionIssues();
        connection.setCoordinator(coordinator);
        connection.scanIp();
    }

    private void iniciarTraceroute() {
        Traceroute traceroute = new Traceroute();
        traceroute.setCoordinator(coordinator);
        traceroute.trace();
    }

    private JMenuItem crearMenuItem(String texto, java.awt.event.ActionListener accion) {
        JMenuItem menuItem = new JMenuItem(texto);
        StylusUI.styleMenuItem(menuItem);
        menuItem.addActionListener(accion);
        return menuItem;
    }

    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
