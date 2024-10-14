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
        try  {
            InputStream input = new FileInputStream("Aplication/src/main/resources/config.properties");
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: " + "config.properties");
            }// load a properties file
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
    public static String geequipementTypeFile() {return equipementTypeFile;}
    public static String getportTypeFile() {return portTypeFile;}
    public static String getlocationFile() {return locationFile;}
}
