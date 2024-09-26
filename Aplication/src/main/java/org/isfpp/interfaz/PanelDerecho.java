package org.isfpp.interfaz;

import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PanelDerecho {
    private JTextArea propiedades;

    public JPanel crearPanelDerecho() {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, getHeight());
            }
        };
        StylusUI.aplicarEstiloPanel(panel);
        panel.setLayout(new BorderLayout());

        // Logo en la parte superior
        ImageIcon logoIcon = new ImageIcon("Aplication/src/main/resources/pc.png");
        logoIcon = resizeImage(logoIcon, 100, 100);
        JLabel logo = new JLabel(logoIcon);
        StylusUI.aplicarEstiloEtiqueta(logo);
        panel.add(logo, BorderLayout.NORTH);

        // Propiedades del objeto clickeado
        propiedades = new JTextArea("Propiedades del objeto seleccionado");
        StylusUI.styleTextArea(propiedades);
        panel.add(propiedades, BorderLayout.CENTER);

        return panel;
    }

    public void updateProperties(String text) {
        propiedades.setText(text);
    }


    private ImageIcon resizeImage(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}
