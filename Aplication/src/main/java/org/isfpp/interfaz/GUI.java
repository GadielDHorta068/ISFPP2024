package org.isfpp.interfaz;

import org.isfpp.datos.Cargar;
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
import java.io.IOException;
import java.util.ArrayList;

public class GUI {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Prueba interfaz por modulos ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        StylusUI.inicializar(false);

        PanelDerecho panelDerecho = new PanelDerecho();

        Web web = Cargar.cargarRedDesdePropiedades("config.properties");

        DesplegableComponent<Equipment> desplegableNodos = new DesplegableComponent<>("Equipos", new ArrayList<>(web.getHardware().values()), panelDerecho, web);
        DesplegableComponent<Location> desplegableCables = new DesplegableComponent<>("Ubicaciones", new ArrayList<>(web.getLocations().values()), panelDerecho, web);
        DesplegableComponent<Connection> desplegableConexiones = new DesplegableComponent<>("Conexiones", web.getConnections(), panelDerecho, web);

        BarraMenu barraMenu = new BarraMenu(web);
        JPanel panelIzquierdo = new JPanel();
        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(desplegableNodos.getPanel());
        panelIzquierdo.add(desplegableConexiones.getPanel());
        panelIzquierdo.add(desplegableCables.getPanel());


        frame.setJMenuBar(barraMenu.crearBarraMenu());
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho.crearPanelDerecho(), BorderLayout.EAST);

        frame.setSize(800, 600);
        panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth()- 262, 400));
        frame.setVisible(true);
    }
}
