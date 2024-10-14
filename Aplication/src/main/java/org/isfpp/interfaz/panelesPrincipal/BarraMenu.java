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

/**
 * Clase que representa la barra de menú en la interfaz de usuario.
 */
public class BarraMenu {
    private final Web web;
    private Coordinator coordinator;

    /**
     * Constructor para inicializar la barra de menú.
     *
     * @param web Objeto de tipo Web.
     */
    public BarraMenu(Web web) {
        this.web = web;
    }

    /**
     * Crea y configura la barra de menú.
     *
     * @return JMenuBar La barra de menú configurada.
     */
    public JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        StylusUI.styleMenuBar(menuBar);

        menuBar.add(crearArchivoMenu());
        menuBar.add(crearEditarMenu());
        menuBar.add(crearAyudaMenu());
        menuBar.add(crearHerramientasMenu());

        return menuBar;
    }

    /**
     * Crea el menú "Archivo" y sus elementos.
     *
     * @return JMenu El menú "Archivo".
     */
    private JMenu crearArchivoMenu() {
        JMenu archivoMenu = new JMenu("Archivo");
        StylusUI.styleMenu(archivoMenu);

        archivoMenu.add(crearMenuItem("Cargar", this::accionCargar));
        archivoMenu.add(crearMenuItem("Guardar", this::accionGuardar));
        archivoMenu.add(crearMenuItem("Salir", e -> System.exit(0)));

        return archivoMenu;
    }

    /**
     * Acción a ejecutar cuando se selecciona "Cargar" en el menú.
     *
     * @param actionEvent Evento de acción.
     */
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

    /**
     * Acción a ejecutar cuando se selecciona "Guardar" en el menú.
     *
     * @param actionEvent Evento de acción.
     */
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
                guardar.saveAll(web, directory);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Crea el menú "Editar" y sus elementos.
     *
     * @return JMenu El menú "Editar".
     */
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

    /**
     * Acción a ejecutar cuando se selecciona "Eliminar" en el menú.
     *
     * @param actionEvent Evento de acción.
     */
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

    /**
     * Acción a ejecutar cuando se selecciona "Editar" en el menú.
     *
     * @param actionEvent Evento de acción.
     */
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

    /**
     * Crea el menú "Ayuda" y sus elementos.
     *
     * @return JMenu El menú "Ayuda".
     */
    private JMenu crearAyudaMenu() {
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        
        ayudaMenu.add(crearMenuItem("Como Usar" , e -> abrirManual()));
        ayudaMenu.add(crearMenuItem("Acerca de" , e -> acercaDe()));
        return ayudaMenu;
    }

    /**
     * Acción a ejecutar cuando se selecciona "Acerca de" en el menú.
     */
    private void acercaDe() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {

                URI uri = new URI("""
                        https://github.com/GadielDHorta068/ISFPP2024""");
                Desktop.getDesktop().browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre el manual de usuario en formato PDF.
     */
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

    /**
     * Crea el menú "Herramientas" y sus elementos.
     *
     * @return JMenu El menú "Herramientas".
     */
    private JMenu crearHerramientasMenu() {
        JMenu herramientasMenu = new JMenu("Herramientas");
        StylusUI.styleMenu(herramientasMenu);

        herramientasMenu.add(crearMenuItem("Ping en rango", e -> iniciarPing()));
        herramientasMenu.add(crearMenuItem("Equipos Activos", e -> iniciarPingEquipos()));
        herramientasMenu.add(crearMenuItem("Conexiones", e -> iniciarConnectionIssues()));
        herramientasMenu.add(crearMenuItem("ON/OFF equipo", e -> cambiarEstado()));
        herramientasMenu.add(crearMenuItem("Visualizar Grafo", e -> new VisualizarGrafo(web.getHardware(), web.getConnections())));
        herramientasMenu.add(crearMenuItem("Traceroute", e -> iniciarTraceroute()));

        return herramientasMenu;
    }

    private void cambiarEstado() {
        if (coordinator.getSelectedItem() instanceof Equipment eq){
            eq.setStatus(!eq.isStatus());
        }
    }

    /**
     * Inicia un escaneo de ping en rango de IPs.
     */
    private void iniciarPing() {
        IPFrame ipFrame = new IPFrame();
        ipFrame.setCoordinator(coordinator);
        ipFrame.scanIp();
    }

    /**
     * Inicia un escaneo de ping en equipos activos.
     */
    private void iniciarPingEquipos() {
        PingListEquipment pingList = new PingListEquipment();
        pingList.setCoordinator(coordinator);
        pingList.ping();
    }

    /**
     * Inicia la detección de problemas de conexión.
     */
    private void iniciarConnectionIssues() {
        ConnectionIssues connection = new ConnectionIssues();
        connection.setCoordinator(coordinator);
        connection.scanIp();
    }

    /**
     * Inicia un traceroute.
     */
    private void iniciarTraceroute() {
        Traceroute traceroute = new Traceroute();
        traceroute.setCoordinator(coordinator);
        traceroute.trace();
    }

    /**
     * Crea un elemento de menú y lo configura con la acción correspondiente.
     *
     * @param texto  El texto del elemento de menú.
     * @param accion La acción a ejecutar al seleccionar el elemento.
     * @return JMenuItem El elemento de menú configurado.
     */
    private JMenuItem crearMenuItem(String texto, java.awt.event.ActionListener accion) {
        JMenuItem menuItem = new JMenuItem(texto);
        StylusUI.styleMenuItem(menuItem);
        menuItem.addActionListener(accion);
        return menuItem;
    }

    /**
     * Establece el coordinador de la barra de menú.
     *
     * @param coordinator El coordinador.
     */
    public void setCoordinador(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}