package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.panelesPrincipal.BarraMenu;
import org.isfpp.interfaz.panelesPrincipal.DesplegableComponent;
import org.isfpp.interfaz.panelesPrincipal.PanelDerecho;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;
import org.isfpp.logica.Lan;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainMenu {
    Coordinator coordinator;
    JFrame frame;
    private ResourceBundle rb;

    public MainMenu() {
        frame = new JFrame("ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    public void components(Lan lan) {
        rb = coordinator.getResourceBundle();
        StylusUI.inicializar(false);
        PanelDerecho panelDerecho = new PanelDerecho();

        DesplegableComponent<Equipment> desplegableNodos = new DesplegableComponent<>();
        coordinator.addTabla(desplegableNodos);
        desplegableNodos.setCoordinator(coordinator);
        desplegableNodos.IniciarTabla(rb.getString("equipos"), new ArrayList<>(lan.getHardware().values()), panelDerecho);

        DesplegableComponent<Location> desplegableUbicaciones = new DesplegableComponent<>();
        coordinator.addTabla(desplegableUbicaciones);
        desplegableUbicaciones.setCoordinator(coordinator);
        desplegableUbicaciones.IniciarTabla(rb.getString("ubicaciones"), new ArrayList<>(lan.getLocations().values()), panelDerecho);

        DesplegableComponent<Connection> desplegableConexiones = new DesplegableComponent<>();
        coordinator.addTabla(desplegableConexiones);
        desplegableConexiones.setCoordinator(coordinator);
        desplegableConexiones.IniciarTabla(rb.getString("conexiones"), lan.getConnections(), panelDerecho);

        BarraMenu barraMenu = new BarraMenu(lan);
        barraMenu.setCoordinador(coordinator);
        JPanel panelIzquierdo = new JPanel();
        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(desplegableNodos.getPanel());
        panelIzquierdo.add(desplegableConexiones.getPanel());
        panelIzquierdo.add(desplegableUbicaciones.getPanel());
        frame.setJMenuBar(barraMenu.crearBarraMenu());
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho.crearPanelDerecho(coordinator.getResourceBundle()), BorderLayout.EAST);

        frame.setSize(800, 600);
        panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth() - 262, 400));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setTitle(rb.getString("titulo_principal") + coordinator.getUser());
    }

    public void SetCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
