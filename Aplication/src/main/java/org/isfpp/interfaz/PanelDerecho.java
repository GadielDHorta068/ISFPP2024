package org.isfpp.interfaz;

import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PanelDerecho {
    public JPanel crearPanelDerecho() {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, getHeight());  // Ancho fijo de 250 px, altura adaptable
            }
        };

        StylusUI.aplicarEstiloPanel(panel);
        panel.setLayout(new BorderLayout());

        // Cargar y redimensionar la imagen del logo
        ImageIcon logoIcon = redimensionarImagen("Aplication/src/main/resources/pc.png", 100, 100);  // Cambiar el tamaño según tus necesidades
        JLabel logo = new JLabel(logoIcon);
        StylusUI.aplicarEstiloEtiqueta(logo);
        panel.add(logo, BorderLayout.NORTH);

        // Crear el área de texto con ajuste de línea
        JTextArea propiedades = new JTextArea("Propiedades del objeto seleccionado");
        propiedades.setLineWrap(true);  // Habilitar ajuste de línea
        propiedades.setWrapStyleWord(true);  // Ajustar solo por palabras completas
        propiedades.setEditable(false);  // Evitar que el usuario edite el texto

        JScrollPane scrollPane = new JScrollPane(propiedades);  // Agregar un scroll por si el texto es demasiado largo
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Método para redimensionar la imagen
    private ImageIcon redimensionarImagen(String ruta, int ancho, int alto) {
        try {
            BufferedImage imagenOriginal = ImageIO.read(new File(ruta));
            Image imagenRedimensionada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenRedimensionada);
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // En caso de error, podrías manejarlo mostrando un placeholder o algún mensaje
        }
    }
}
