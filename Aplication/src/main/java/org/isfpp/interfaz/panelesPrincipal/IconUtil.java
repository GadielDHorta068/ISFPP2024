package org.isfpp.interfaz.panelesPrincipal;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class IconUtil {
    private static final Properties prop = new Properties();

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

    public static ImageIcon getIcon(String iconName) {
        String iconFileName = prop.getProperty("icon." + iconName);
        if (iconFileName != null) {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(IconUtil.class.getClassLoader().getResource(iconFileName)));

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
    public static void main(String[] args) {
        ImageIcon icon = IconUtil.getIcon("pc");
        if (icon != null) {
            JOptionPane.showMessageDialog(null, "Icono cargado", "Icono", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }
}
