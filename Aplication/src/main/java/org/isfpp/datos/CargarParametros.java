package org.isfpp.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargarParametros {
    private static String equipementFile;
    private static String connectionFile;
    private static String wireTypeFile;
    private static String equipementTypeFile;
    private static String portTypeFile;
    private static String locationFile;


    public static void parametros() throws IOException {
        Properties prop = new Properties();
        try (InputStream input = Cargar.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: " + "config.properties");
            }
            prop.load(input);
        }
        equipementFile = prop.getProperty("rs.equipment");
        connectionFile = prop.getProperty("rs.connection");
        wireTypeFile = prop.getProperty("rs.wireType");
        equipementTypeFile = prop.getProperty("rs.equipmentType");
        portTypeFile = prop.getProperty("rs.portType");
        locationFile = prop.getProperty("rs.location");
    }

    public static String getequipementFile() {return equipementFile;}
    public static String getconnectionFile() {return connectionFile;}
    public static String getWireTypeFile() {return wireTypeFile;}
    public static String getquipementTypeFile() {return equipementTypeFile;}
    public static String getportTypeFile() {return portTypeFile;}
    public static String getlocationFile() {return locationFile;}
}
