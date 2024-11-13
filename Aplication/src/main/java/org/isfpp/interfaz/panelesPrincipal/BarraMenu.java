package org.isfpp.interfaz.panelesPrincipal;

import org.apache.log4j.Logger;
import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.panelesAddons.*;
import org.isfpp.interfaz.panelesCreadores.*;
import org.isfpp.interfaz.panelesEditadores.*;
import org.isfpp.logica.Lan;
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
    private static final Logger log = Logger.getLogger(BarraMenu.class);
    private ResourceBundle rb;
    private final Lan lan;
    private Coordinator coordinator;

    /**
     * Constructor por defecto
     * @param lan Red a ser tratada
     */
    public BarraMenu(Lan lan) {
        this.lan = Lan.getLan();
    }

    /**
     * Crear la barra con sus botones y entradas
     * @return JMenuBar
     */
    public JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        StylusUI.styleMenuBar(menuBar);

        menuBar.add(crearArchivoMenu());
       // menuBar.add(crearVistaMenu());
        menuBar.add(crearEditarMenu());
        menuBar.add(crearAyudaMenu());
        menuBar.add(crearHerramientasMenu());
        menuBar.add(crearMenuItem("vista",this::switchTablas));
        return menuBar;
    }

    /**
     * Crear seccion Archivo de la barra
     * @return JMenu
     */
    private JMenu crearArchivoMenu() {
        this.rb=coordinator.getResourceBundle();
        JMenu archivoMenu = new JMenu(rb.getString("archivo"));
        StylusUI.styleMenu(archivoMenu);

        archivoMenu.add(crearMenuItem(rb.getString("cargar"), this::accionCargar));
        archivoMenu.add(crearMenuItem(rb.getString("guardar"), this::accionGuardar));
        archivoMenu.add(crearMenuItem(rb.getString("salir"), e -> System.exit(0)));

        return archivoMenu;
    }

    /**
     * Cargar una red desde archivo
     * @param actionEvent Listener del boton
     */
    private void accionCargar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            coordinator.searchAllOf(directory);
            coordinator.updateTablas(lan);
        }
    }

    /**
     * Guardar a archivo la red
     * @param actionEvent
     */
    private void accionGuardar(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String directory = fileChooser.getSelectedFile().getAbsolutePath();
            File dataDir = new File(fileChooser.getSelectedFile(), "data");

            coordinator.insertAllInto(directory);

        }
    }

    private void switchTablas(ActionEvent actionEvent) {
        System.out.println(coordinator.getMainMenu().getSelect());
        int i = coordinator.getMainMenu().getSelect();
        switch (i){
            case 1: verTablasDeTipos(actionEvent);
                break;
            case 2: verTablaPrincipal(actionEvent);
                break;
        }
    }

    private void verTablaPrincipal(ActionEvent actionEvent) {
        MainMenu menu = coordinator.getMainMenu();
        menu.principalMenu(coordinator.getWeb());
    }


    private void verTablasDeTipos(ActionEvent actionEvent) {
        MainMenu menu = coordinator.getMainMenu();
        menu.secondaryMenu(coordinator.getWeb());
    }

    /**
     * Crear menu desplegable con el panel de edicion/ creacion/ eliminacion
     * @return JMenu
     */
    private JMenu crearEditarMenu() {
        JMenu editarMenu = new JMenu(rb.getString("editar"));
        StylusUI.styleMenu(editarMenu);

        JMenu subMenuAgregar = new JMenu(rb.getString("agregar"));
        editarMenu.add(subMenuAgregar);
        StylusUI.styleMenu(subMenuAgregar);
        subMenuAgregar.setBackground(StylusUI.COLOR_PRIMARIO);
        subMenuAgregar.setOpaque(true);


        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_equipo"), e -> {
            EquipmentFormPanel equipmentPanel = new EquipmentFormPanel();
            equipmentPanel.setCoordinator(coordinator);
            equipmentPanel.run();
        }));

        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_puerto"), e -> {
            PortTypeFormPanel portTypePanel = new PortTypeFormPanel();
            portTypePanel.setCoordinator(coordinator);
            portTypePanel.run();
        }));

        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_ubicacion"), e -> {
            LocationFormPanel locationPanel = new LocationFormPanel();
            locationPanel.setCoordinator(coordinator);
            locationPanel.run();
        }));

        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_connexion"), e -> {
            EditConnection editConnection = new EditConnection();
            editConnection.setCoordinator(coordinator);
            editConnection.run(null);
        }));

        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_tipo_de_cable"), e -> {
            WireTypeFromPanel wireTypeFromPanel= new WireTypeFromPanel();
            wireTypeFromPanel.setCoordinator(coordinator);
            wireTypeFromPanel.run();
        }));

        subMenuAgregar.add(crearMenuItem(rb.getString("agregar_tipo_de_equipo"), e -> {
            EquipmentTypeFromPanel equipmentTypePanel= new EquipmentTypeFromPanel();
            equipmentTypePanel.setCoordinator(coordinator);
            equipmentTypePanel.run();
        }));

        editarMenu.add(crearMenuItem(rb.getString("eliminar"), this::accionEliminar));
        editarMenu.add(crearMenuItem(rb.getString("editar_puerto"), this::accionEditarPuerto));

        editarMenu.add(crearMenuItem(rb.getString("modificar"), this::accionEditar));
        return editarMenu;
    }

    /**
     * editar puertos
     * @param actionEvent metodo que se ejecuta al presionar el listener del boton
     */
    private void accionEditarPuerto(ActionEvent actionEvent) {
        Object seleccionado = coordinator.getSelectedItem();
        if (seleccionado != null) {
            if (seleccionado instanceof Equipment equipment) {
                EditPortsFromEquipment ed = new EditPortsFromEquipment();
                ed.setCoordinator(coordinator);
                ed.run(equipment.getCode());
            } else {
                JOptionPane.showMessageDialog(null, rb.getString("seleccionar_equipo"));
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
        }else {
            JOptionPane.showMessageDialog(null,rb.getString( "seleccionar_item"));
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
                case Connection connection -> {
                    EditConnection editConnection = new EditConnection();
                    editConnection.setCoordinator(coordinator);
                    editConnection.run(connection);
                    //JOptionPane.showMessageDialog(null,rb.getString( "Error_crear_conexion"));
                }
                case PortType puerto -> {
                    JOptionPane.showMessageDialog(null, rb.getString("no_editable_tipo_puerto"),
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                case WireType wireType -> {
                    JOptionPane.showMessageDialog(null, rb.getString("no_editable_tipo_cable"),
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                case EquipmentType equipmentType -> {
                    JOptionPane.showMessageDialog(null, rb.getString("no_editable_tipo_equipo"),
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                default -> System.out.println(rb.getString("clase_no_detectada") + seleccionado.getClass());
            }
        }
    }

    private JMenu crearAyudaMenu() {
        JMenu ayudaMenu = new JMenu(rb.getString("ayuda"));
        StylusUI.styleMenu(ayudaMenu);

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
            coordinator.updateTablas(lan);
        }else {
            JOptionPane.showMessageDialog(null, rb.getString("seleccionar_equipo"));
        }
    }

    private void iniciarVerGrafo() {
        ViewGraph viewGraph =new ViewGraph();
        viewGraph.setCoordinator(coordinator);
        viewGraph.Visualizar();
    }

    private void iniciarPing() {
        PingAll pingAll = new PingAll();
        pingAll.setCoordinator(coordinator);
        pingAll.scanIp();
    }

    private void iniciarPingEquipos() {
        ActiveEquipments pingList = new ActiveEquipments();
        pingList.setCoordinator(coordinator);
        pingList.ping();
    }

    private void iniciarConnectionIssues() {
        ConnectionsTo connection = new ConnectionsTo();
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
        menuItem.setIcon(new ImageIcon("img/COM.png"));
        return menuItem;
    }

    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
