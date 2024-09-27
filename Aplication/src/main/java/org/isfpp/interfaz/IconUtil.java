package org.isfpp.interfaz;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
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
            return new ImageIcon(IconUtil.class.getClassLoader().getResource(iconFileName));
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
