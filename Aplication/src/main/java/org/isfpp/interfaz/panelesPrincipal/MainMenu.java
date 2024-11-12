package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.*;
import org.isfpp.logica.Lan;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.util.ResourceBundle;

public class MainMenu {
    private Coordinator coordinator;
    private JFrame frame;
    private PanelDerecho panelDerecho;
    private JPanel panelIzquierdo;
    private ResourceBundle rb;
    private int select;

    public MainMenu() {
        frame = new JFrame("ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        panelIzquierdo = new JPanel();
        select = 0;
    }

    private <T> DesplegableComponent<T>  agregarDesplegable(String clave, java.util.List<T> datos, PanelDerecho panelDerecho) {
        DesplegableComponent<T> desplegable = new DesplegableComponent<>();
        coordinator.addTabla(desplegable);
        desplegable.setCoordinator(coordinator);
        desplegable.IniciarTabla(rb.getString(clave), datos, panelDerecho);
        return desplegable;
    }

    public void components(Lan lan) {
        rb = coordinator.getResourceBundle();
        StylusUI.inicializar(false);
        panelDerecho = new PanelDerecho();

        DesplegableComponent<Equipment> desplegableEquipos = agregarDesplegable("equipos",
                new ArrayList<>(lan.getHardware().values()), panelDerecho);
        DesplegableComponent<Location> desplegableUbicaciones = agregarDesplegable("ubicaciones",
                new ArrayList<>(lan.getLocations().values()), panelDerecho);
        DesplegableComponent<Connection> desplegableConexiones = agregarDesplegable("conexiones",
                lan.getConnections(), panelDerecho);

        BarraMenu barraMenu = new BarraMenu(lan);
        barraMenu.setCoordinador(coordinator);

        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(desplegableEquipos.getPanel());
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

        select = 1;
    }

    public void principalMenu(Lan lan) {
        if (select != 1) {
            frame.remove(panelIzquierdo);
            frame.repaint();
            // Crear los desplegables con nuevos datos para el secundary menu
            DesplegableComponent<Equipment> desplegableEquipos = agregarDesplegable("equipos",
                    new ArrayList<>(lan.getHardware().values()), panelDerecho);
            DesplegableComponent<Location> desplegableUbicaciones = agregarDesplegable("ubicaciones",
                    new ArrayList<>(lan.getLocations().values()), panelDerecho);
            DesplegableComponent<Connection> desplegableConexiones = agregarDesplegable("conexiones",
                    lan.getConnections(), panelDerecho);

            // Limpiar y actualizar el panel izquierdo con los nuevos componentes
            panelIzquierdo.removeAll();
            StylusUI.aplicarEstiloPanel(panelIzquierdo);
            panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
            panelIzquierdo.add(desplegableEquipos.getPanel());
            panelIzquierdo.add(desplegableConexiones.getPanel());
            panelIzquierdo.add(desplegableUbicaciones.getPanel());
            frame.add(panelIzquierdo, BorderLayout.WEST);
            panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth() - 262, 400));

            // Volver a agregar los paneles actualizados al frame

            // Revalidar y repintar los paneles para asegurar que los cambios se reflejen
            panelIzquierdo.revalidate();
            panelIzquierdo.repaint();
            frame.revalidate();
            frame.repaint();

            this.select = 1;
        }
    }


    public void secundaryMenu(Lan lan) {
        if (select != 2) {
            frame.remove(panelIzquierdo);
            frame.repaint();
            // Crear los desplegables con los datos de tipos para el secundary menu
            DesplegableComponent<EquipmentType> desplegableEquipos = agregarDesplegable("tipos_equipos",
                    new ArrayList<>(lan.getEquipmentTypes().values()), panelDerecho);
            DesplegableComponent<WireType> desplegableUbicaciones = agregarDesplegable("tipos_cables",
                    new ArrayList<>(lan.getWireTypes().values()), panelDerecho);
            DesplegableComponent<PortType> desplegableConexiones = agregarDesplegable("tipos_puetos",
                    new ArrayList<>(lan.getPortTypes().values()), panelDerecho);

            // Limpiar y actualizar el panel izquierdo con los nuevos componentes
            panelIzquierdo.removeAll();
            StylusUI.aplicarEstiloPanel(panelIzquierdo);
            panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
            panelIzquierdo.add(desplegableEquipos.getPanel());
            panelIzquierdo.add(desplegableUbicaciones.getPanel());
            panelIzquierdo.add(desplegableConexiones.getPanel());
            frame.add(panelIzquierdo, BorderLayout.WEST);
            panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth() - 262, 400));

            // Volver a agregar los paneles actualizados al frame

            // Revalidar y repintar los paneles para asegurar que los cambios se reflejen
            panelIzquierdo.revalidate();
            panelIzquierdo.repaint();
            frame.revalidate();
            frame.repaint();

            this.select = 2;
        }
    }



    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public int getSelect(){
        return this.select;
    }
}
