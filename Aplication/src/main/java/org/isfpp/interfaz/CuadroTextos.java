package org.isfpp.interfaz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CuadroTextos {
    public JPanel crearCuadroTextos() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        String[] items = {"Conexion", "Nodo", "Cable"};
        for (String item : items) {
            JButton button = new JButton(item);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Clicked: " + item);
                }
            });
            panel.add(button);
        }

        return panel;
    }
}
