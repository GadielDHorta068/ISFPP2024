package org.isfpp.interfaz;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IconUtil {
    private static final Properties prop = new Properties();

    /*
     * Bloque estático para cargar el archivo de propiedades
     */
    static {
        try (InputStream input = IconUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                prop.load(input);
            } else {
                System.err.println("Archivo de propiedades no encontrado.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Obtiene un icono basado en el nombre del icono especificado en el archivo de propiedades.
     *
     * @param iconName el nombre del icono
     * @return el icono cargado como ImageIcon, o null si no se encuentra el icono
     */
    public static ImageIcon getIcon(String iconName) {
        String iconFileName = prop.getProperty("icon." + iconName);
        if (iconFileName != null) {
            ImageIcon icon = new ImageIcon(IconUtil.class.getClassLoader().getResource(iconFileName));
            // Convertimos el ImageIcon a BufferedImage
            Image image = icon.getImage();
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Creamos el Graphics2D para dibujar con antialiasing
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // Devolvemos el BufferedImage como ImageIcon
            return new ImageIcon(bufferedImage);
        } else {
            System.err.println("Icono no encontrado en el archivo de propiedades.");
            return null;
        }
    }

    /**
     * Método principal para probar la carga de iconos.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        ImageIcon icon = IconUtil.getIcon("charly");
        if (icon != null) {
            JOptionPane.showMessageDialog(null, "Icono cargado", "Icono", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }
}