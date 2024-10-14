package org.isfpp.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase para cargar los parámetros de configuración desde un archivo "config.properties".
 */
public class CargarParametros {
    private static String equipementFile;
    private static String connectionFile;
    private static String wireTypeFile;
    private static String equipementTypeFile;
    private static String portTypeFile;
    private static String locationFile;

    /**
     * Método para cargar los parámetros de configuración.
     *
     * @throws IOException si hay un error al leer el archivo de propiedades.
     */
    public static void parametros() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = Cargar.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: " + "config.properties");
            }
            prop.load(input);
        }
        equipementFile = prop.getProperty("rs.equipment");
        System.out.println(convertToAbsolutePath(equipementFile));
        connectionFile = prop.getProperty("rs.connection");
        wireTypeFile = prop.getProperty("rs.wireType");
        equipementTypeFile = prop.getProperty("rs.equipmentType");
        portTypeFile = prop.getProperty("rs.portType");
        locationFile = prop.getProperty("rs.location");
    }

    /**
     * Convierte una ruta relativa a una ruta absoluta.
     *
     * @param relativePath la ruta relativa a convertir.
     * @return la ruta absoluta.
     */
    private static String convertToAbsolutePath(String relativePath) {
        return new java.io.File(relativePath).getAbsolutePath();
    }

    /**
     * @return la ruta del archivo de equipos.
     */
    public static String getequipementFile() {
        return equipementFile;
    }

    /**
     * @return la ruta del archivo de conexiones.
     */
    public static String getconnectionFile() {
        return connectionFile;
    }

    /**
     * @return la ruta del archivo de tipos de cables.
     */
    public static String getWireTypeFile() {
        return wireTypeFile;
    }

    /**
     * @return la ruta del archivo de tipos de equipos.
     */
    public static String getquipementTypeFile() {
        return equipementTypeFile;
    }

    /**
     * @return la ruta del archivo de tipos de puertos.
     */
    public static String getportTypeFile() {
        return portTypeFile;
    }

    /**
     * @return la ruta del archivo de localizaciones.
     */
    public static String getlocationFile() {
        return locationFile;
    }
}