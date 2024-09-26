package org.isfpp.interfaz;

import org.isfpp.modelo.Web;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    public static void main(String[] args) {
        // Crear el marco de la ventana
        JFrame frame = new JFrame("Prueba interfaz por modulos ISFPP24");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Crear una instancia de la clase Web y agregar equipos de ejemplo
        Web web = new Web("Red Local");
        web.addEquipment(new Equipment("Router", "192.168.1.1"));
        web.addEquipment(new Equipment("Switch", "192.168.1.2"));
        web.addEquipment(new Equipment("Servidor", "192.168.1.10"));

        // Convertir los equipos del HashMap en una lista
        List<Equipment> equipmentList = new ArrayList<>(web.getHardware().values());

        // Crear instancias de los componentes desplegables con los equipos
        DesplegableComponent desplegableNodos = new DesplegableComponent("Equipos", equipmentList);

        // Panel izquierdo con los componentes desplegables
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.add(desplegableNodos.getPanel());

        // Añadir los componentes al marco
        frame.add(panelIzquierdo, BorderLayout.WEST);

        // Hacer visible la ventana
        frame.pack();
        frame.setVisible(true);
    }
}
