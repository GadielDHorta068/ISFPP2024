package org.isfpp.interfaz;

import org.isfpp.controller.Settings;
import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.panelesPrincipal.BarraMenu;
import org.isfpp.interfaz.panelesPrincipal.PanelDerecho;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.LAN;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class GUI {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Prueba interfaz por modulos ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        StylusUI.inicializar(false);
        Settings s=new Settings();

        PanelDerecho panelDerecho = new PanelDerecho();

        LAN LAN = Cargar.cargarRedDesdePropiedades("config.properties");

        BarraMenu barraMenu = new BarraMenu(LAN);
        JPanel panelIzquierdo = new JPanel();
        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));


        frame.setJMenuBar(barraMenu.crearBarraMenu());
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho.crearPanelDerecho(s.getResourceBundle()), BorderLayout.EAST);

        frame.setSize(800, 600);
        panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth() - 262, 400));
        frame.setVisible(true);
    }
}
