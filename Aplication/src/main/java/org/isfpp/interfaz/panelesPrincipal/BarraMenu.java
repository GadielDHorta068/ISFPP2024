package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
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
    private final LAN LAN;
    private Coordinator coordinator;

    public BarraMenu(LAN LAN) {
        this.LAN = LAN;
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
            coordinator.searchAllOf(directory);
            coordinator.updateTablas();
        }
    }

    private void accionGuardar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            File dataDir = new File(fileChooser.getSelectedFile(), "data");

            coordinator.insertAllInto(directory);

        }
    }

    private JMenu crearEditarMenu() {
        JMenu editarMenu = new JMenu("Editar");
        StylusUI.styleMenu(editarMenu);

        editarMenu.add(crearMenuItem("Agregar Equipo", e -> new EquipmentFormPanel(LAN)));
        editarMenu.add(crearMenuItem("Agregar tipo Puerto", e -> new PortTypeFormPanel(LAN)));
        editarMenu.add(crearMenuItem("Agregar Ubicacion", e -> new LocationFormPanel(LAN)));
        editarMenu.add(crearMenuItem("Agregar Conexion", e -> new EditConnection(LAN, null)));
        editarMenu.add(crearMenuItem("Eliminar", this::accionEliminar));
        editarMenu.add(crearMenuItem("Editar", this::accionEditar));

        return editarMenu;
    }

    private void accionEliminar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> coordinator.eraseEquipment(equipment);
                case Location location -> coordinator.eraseLocation(location);
                case PortType puerto -> coordinator.erasePort(puerto);
                case Connection connection -> coordinator.eraseConnection(connection);
                default -> System.out.println("Clase no detectada: " + seleccionado.getClass());
            }
            coordinator.updateTablas();
        }
    }

    private void accionEditar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> new EditEquipmentFormPanel(LAN, equipment.getCode());
                case Location location -> new EditLocationFormPanel(LAN, location.getCode());
                case PortType puerto -> new EditPortTypeFormPanel(LAN, puerto.getCode());
                case Connection connection -> new EditConnection(LAN, connection);
                default -> System.out.println("Clase no detectada: " + seleccionado.getClass());
            }
        }
    }

    private JMenu crearAyudaMenu() {
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);

        ayudaMenu.add(crearMenuItem("Como Usar" , e -> abrirManual()));
        ayudaMenu.add(crearMenuItem("Acerca de" , e -> acercaDe()));
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
        herramientasMenu.add(crearMenuItem("Visualizar Grafo", e ->   iniciarVerGrafo()));
        herramientasMenu.add(crearMenuItem("Traceroute", e -> iniciarTraceroute()));

        return herramientasMenu;
    }

    private void iniciarVerGrafo() {
        VisualizarGrafo visualizarGrafo=new VisualizarGrafo();
        visualizarGrafo.setCoordinator(coordinator);
        visualizarGrafo.Visualizar();
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
