package org.isfpp.interfaz.stylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WindowUtils {

    /**
     * Cierra la ventana con una animación de desvanecimiento.
     *
     * @param frame La ventana que se cerrará con la animación.
     */
    public static void closeWithFadeOut(JFrame frame) {
        // Hacer visible la ventana y asegurarse de que soporte opacidad
        frame.setVisible(true);
        frame.setOpacity(1.0f); // Asegurar que comience completamente opaco

        Timer timer = new Timer(20, new ActionListener() {
            float opacity = 1.0f; // Inicia con opacidad completa

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f; // Reduce la opacidad en cada paso
                if (opacity <= 0) {
                    ((Timer) e.getSource()).stop(); // Detiene el timer
                    frame.dispose(); // Cierra la ventana
                } else {
                    frame.setOpacity(opacity); // Aplica la opacidad
                }
            }
        });
        timer.start(); // Inicia la animación
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ventana con Animación de Cierre");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(400, 300);

        // Configurar botón para cerrar con animación
        JButton closeButton = new JButton("Cerrar con Animación");
        closeButton.addActionListener(e -> closeWithFadeOut(frame));

        frame.add(closeButton, BorderLayout.CENTER);
        frame.setOpacity(1.0f); // Asegurar opacidad inicial completa
        frame.setUndecorated(true);
        frame.setVisible(true);
    }
}
