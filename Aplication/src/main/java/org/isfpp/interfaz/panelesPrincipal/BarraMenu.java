package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Guardar;
import org.isfpp.interfaz.IPFrame;
import org.isfpp.interfaz.VisualizarGrafo;
import org.isfpp.interfaz.panelesCreadores.EquipmentFormPanel;
import org.isfpp.interfaz.panelesCreadores.LocationFormPanel;
import org.isfpp.interfaz.panelesCreadores.PortTypeFormPanel;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Web;

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


        salirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para salir
                System.exit(0);
            }
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

        verGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VisualizarGrafo(web.getHardware() , web.getConnections());
            }
        });

        //eliminarItem.addActionListener(e -> desplegableComponent.removeSelectedEquipment());
        editarMenu.add(agregarEquipoItem);
        editarMenu.add(agregarPuertoItem);
        editarMenu.add(agregarUbicacionItem);
        editarMenu.add(eliminarItem);
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        JMenu herramientasMenu = new JMenu("Herramientas");
        JMenuItem ipScan = new JMenuItem("Scan IP");
        herramientasMenu.add(ipScan);
        StylusUI.styleMenuItem(ipScan);
        StylusUI.styleMenu(herramientasMenu);
        herramientasMenu.add(verGrafo);

        ipScan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IPFrame();
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
