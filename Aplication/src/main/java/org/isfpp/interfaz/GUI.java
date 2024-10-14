package org.isfpp.interfaz;

import org.isfpp.interfaz.panelesPrincipal.BarraMenu;
import org.isfpp.interfaz.panelesPrincipal.PanelDerecho;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GUI {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Prueba interfaz por modulos ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        StylusUI.inicializar(false);

        PanelDerecho panelDerecho = new PanelDerecho();

        Web web = Web.getWeb();

   //     DesplegableComponent<Equipment> desplegableNodos = new DesplegableComponent<>();
      //  desplegableNodos.IniciarTabla("Equipos", new ArrayList<>(web.getHardware().values()), panelDerecho, coordinator);
     //   DesplegableComponent<Location> desplegableCables = new DesplegableComponent<>("Ubicaciones", new ArrayList<>(web.getLocations().values()), panelDerecho, null);
    //    DesplegableComponent<Connection> desplegableConexiones = new DesplegableComponent<>("Conexiones", web.getConnections(), panelDerecho, null);

        BarraMenu barraMenu = new BarraMenu(web);
        JPanel panelIzquierdo = new JPanel();
        StylusUI.aplicarEstiloPanel(panelIzquierdo);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
   //     panelIzquierdo.add(desplegableNodos.getPanel());
    //    panelIzquierdo.add(desplegableConexiones.getPanel());
 //       panelIzquierdo.add(desplegableCables.getPanel());


        frame.setJMenuBar(barraMenu.crearBarraMenu());
        frame.add(panelIzquierdo, BorderLayout.WEST);
        frame.add(panelDerecho.crearPanelDerecho(), BorderLayout.EAST);

        frame.setSize(800, 600);
        panelIzquierdo.setPreferredSize(new Dimension(frame.getWidth()- 262, 400));
        frame.setVisible(true);
    }
}
