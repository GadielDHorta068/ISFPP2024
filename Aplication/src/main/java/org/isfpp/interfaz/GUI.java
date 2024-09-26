package org.isfpp.interfaz;

import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Web;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prueba interfaz por modulos ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        StylusUI.inicializar(false);

        BarraMenu barraMenu = new BarraMenu();
        PanelDerecho panelDerecho = new PanelDerecho();

        Web web = new Web("Red Principal");

        List<String> ipList1 = new ArrayList<>();
        ipList1.add("192.168.1.1");
        List<String> ipList2 = new ArrayList<>();
        ipList2.add("192.168.1.2");

        web.addEquipment(new Equipment("EQ1", "Router", "Marca A", "Modelo X", ipList1, new ArrayList<>(), null, null));
        web.addEquipment(new Equipment("EQ2", "Switch", "Marca B", "Modelo Y", ipList2, new ArrayList<>(), null, null));

        DesplegableComponent desplegableNodos = new DesplegableComponent("Equipos", web, panelDerecho);
        DesplegableComponent desplegableConexiones = new DesplegableComponent("Conexiones", web, panelDerecho);
        DesplegableComponent desplegableCables = new DesplegableComponent("Cables", web, panelDerecho);


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
