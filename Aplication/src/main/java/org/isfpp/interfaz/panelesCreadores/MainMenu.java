package org.isfpp.interfaz.panelesCreadores;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.ResourceExtractor;
import org.isfpp.interfaz.panelesPrincipal.BarraMenu;
import org.isfpp.interfaz.panelesPrincipal.DesplegableComponent;
import org.isfpp.interfaz.panelesPrincipal.PanelDerecho;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainMenu {
    Coordinator coordinator;
    JFrame frame;

    public MainMenu() {
        frame = new JFrame("ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    public void components(Web web) {
        StylusUI.inicializar(false);
        ResourceExtractor.extractResourcesToExecutionDir();
        PanelDerecho panelDerecho = new PanelDerecho();

        DesplegableComponent<Equipment> desplegableNodos = new DesplegableComponent<>();
        coordinator.addTabla(desplegableNodos);
        desplegableNodos.setCoordinator(coordinator);
        desplegableNodos.IniciarTabla("Equipos", new ArrayList<>(web.getHardware().values()), panelDerecho);

        DesplegableComponent<Location> desplegableUbicaciones = new DesplegableComponent<>();
        coordinator.addTabla(desplegableUbicaciones);
        desplegableUbicaciones.setCoordinator(coordinator);
        desplegableUbicaciones.IniciarTabla("Ubicaciones", new ArrayList<>(web.getLocations().values()), panelDerecho);

        DesplegableComponent<Connection> desplegableConexiones = new DesplegableComponent<>();
        coordinator.addTabla(desplegableConexiones);
        desplegableConexiones.setCoordinator(coordinator);
        desplegableConexiones.IniciarTabla("Conexiones", web.getConnections(), panelDerecho);

        BarraMenu barraMenu = new BarraMenu(web);
        barraMenu.setCoordinador(coordinator);
        JPanel panelIzquierdo = new JPanel();
        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(desplegableNodos.getPanel());
        panelIzquierdo.add(desplegableConexiones.getPanel());
        panelIzquierdo.add(desplegableUbicaciones.getPanel());
        frame.setJMenuBar(barraMenu.crearBarraMenu());
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho.crearPanelDerecho(), BorderLayout.EAST);

        frame.setSize(800, 600);
        panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth() - 262, 400));
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void SetCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
