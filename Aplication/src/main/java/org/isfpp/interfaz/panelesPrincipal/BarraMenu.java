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
import java.util.ResourceBundle;


public class BarraMenu {
    private final LAN LAN;
    private Coordinator coordinator;
    private ResourceBundle rb;

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
        this.rb=coordinator.getResourceBundle();
        JMenu archivoMenu = new JMenu(rb.getString("archivo"));
        StylusUI.styleMenu(archivoMenu);

        archivoMenu.add(crearMenuItem(rb.getString("cargar"), this::accionCargar));
        archivoMenu.add(crearMenuItem(rb.getString("guardar"), this::accionGuardar));
        archivoMenu.add(crearMenuItem(rb.getString("salir"), e -> System.exit(0)));

        return archivoMenu;
    }

    private void accionCargar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                Cargar cargar = new Cargar();
                coordinator.updateTablas(cargar.cargarRedDesdeDirectorio(directory));
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
            if (!dataDir.exists()) {
                dataDir.mkdirs(); // Crea la subcarpeta 'data' si no existe
            }
            try {
                Guardar guardar = new Guardar();
                guardar.saveAll(LAN, directory);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private JMenu crearEditarMenu() {
        JMenu editarMenu = new JMenu(rb.getString("editar"));
        StylusUI.styleMenu(editarMenu);

        editarMenu.add(crearMenuItem(rb.getString("agregar_equipo"), e -> {
            EquipmentFormPanel equipmentPanel = new EquipmentFormPanel();
            equipmentPanel.setCoordinator(coordinator);
            equipmentPanel.run();
        }));

        editarMenu.add(crearMenuItem(rb.getString("agregar_puerto"), e -> {
            PortTypeFormPanel portTypePanel = new PortTypeFormPanel();
            portTypePanel.setCoordinator(coordinator);
            portTypePanel.run();
        }));

        editarMenu.add(crearMenuItem(rb.getString("agregar_ubicacion"), e -> {
            LocationFormPanel locationPanel = new LocationFormPanel();
            locationPanel.setCoordinator(coordinator);
            locationPanel.run();
        }));

        editarMenu.add(crearMenuItem(rb.getString("agregar_connexion"), e -> {
            EditConnection editConnection = new EditConnection();
            editConnection.setCoordinator(coordinator);
            editConnection.run(null);
        }));

        editarMenu.add(crearMenuItem(rb.getString("eliminar"), this::accionEliminar));
        editarMenu.add(crearMenuItem(rb.getString("editar_puerto"), this::accionEditarPuerto));
        editarMenu.add(crearMenuItem(rb.getString("editar"), this::accionEditar));


        return editarMenu;
    }

    private void accionEditarPuerto(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> {
                    EditPortsFromEquipment ed = new EditPortsFromEquipment();
                    ed.setCoordinator(coordinator);
                    ed.run(equipment.getCode());
                }
                default ->  JOptionPane.showMessageDialog(null, rb.getString("seleccionar_equipo"));
            }
        }
    }

    private void accionEliminar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> coordinator.eraseEquipment(equipment);
                case Location location -> coordinator.eraseLocation(location);
                case PortType puerto -> coordinator.erasePort(puerto);
                case Connection connection -> coordinator.eraseConnection(connection);
                default -> System.out.println(rb.getString("clase_no_detectada") + seleccionado.getClass());
            }
        }
    }

    private void accionEditar(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            switch (seleccionado) {
                case Equipment equipment -> {
                    EditEquipmentFormPanel equipmentPanel = new EditEquipmentFormPanel();
                    equipmentPanel.setCoordinator(coordinator);
                    equipmentPanel.run(equipment.getCode());
                }
                case Location location -> {
                    EditLocationFormPanel locationPanel = new EditLocationFormPanel();
                    locationPanel.setCoordinator(coordinator);
                    locationPanel.run( location.getCode());
                }
                case PortType puerto -> {
                    EditPortTypeFormPanel portTypePanel = new EditPortTypeFormPanel();
                    portTypePanel.setCoordinator(coordinator);
                    portTypePanel.run(puerto.getCode());
                }
                case Connection connection -> {
                    JOptionPane.showMessageDialog(null,rb.getString( "Error_crear_conexion"));
                }
                default -> System.out.println(rb.getString("clase_no_detectada") + seleccionado.getClass());
            }
        }
    }

    private JMenu crearAyudaMenu() {
        JMenu ayudaMenu = new JMenu(rb.getString("ayuda"));
        StylusUI.styleMenu(ayudaMenu);
        
        ayudaMenu.add(crearMenuItem(rb.getString("como_usar") , e -> abrirManual()));
        ayudaMenu.add(crearMenuItem(rb.getString("acerca_de") , e -> acercaDe()));
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
            System.out.println(rb.getString("manual_not_found"));
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
                System.out.println(rb.getString("Desktop_no_sistema."));
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
        JMenu herramientasMenu = new JMenu(rb.getString("herramientas"));
        StylusUI.styleMenu(herramientasMenu);

        herramientasMenu.add(crearMenuItem(rb.getString("ping_rango"), e -> iniciarPing()));
        herramientasMenu.add(crearMenuItem(rb.getString("equipos_activos"), e -> iniciarPingEquipos()));
        herramientasMenu.add(crearMenuItem(rb.getString("conexiones"), e -> iniciarConnectionIssues()));
        herramientasMenu.add(crearMenuItem(rb.getString("ver_grafo"), e ->   iniciarVerGrafo()));
        herramientasMenu.add(crearMenuItem(rb.getString("traceroute"), e -> iniciarTraceroute()));
        herramientasMenu.add(crearMenuItem(rb.getString("alternar_estado"), e -> alternarEstado()));

        return herramientasMenu;
    }

    private void alternarEstado() {
        if (coordinator.getSelectedItem() instanceof Equipment equipo){
            equipo.setStatus(!equipo.isStatus());
        }
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
