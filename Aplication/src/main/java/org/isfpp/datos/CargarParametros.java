package org.isfpp.datos;

public class CargarParametros {
    private static String equipementFile;
    private static String connectionFile;
    private static String wireTypeFile;
    private static String equipementTypeFile;
    private static String portTypeFile;
    private static String locationFile;

    public static void parametros() throws IOException{
        Properties prop = new Properties();
        InputStream  Imput = new FileInputStream("config.properties");

        prop.load(Imput);
        equipementFile = prop.getProperty("");
        connectionFile = prop.getProperty("");
        wireTypeFile = prop.getProperty("");
        equipementTypeFile = prop.getProperty("");
        portTypeFile = prop.getProperty("");
        locationFile = prop.getProperty("");
    }

     public static String getequipementFile() {return equipementFile;}
    public static String getconnectionFile() {return connectionFile;}
    public static String getWireTypeFile() {return wireTypeFile;}
    public static String geequipementTypeFile() {return equipementTypeFile;}
    public static String getportTypeFile() {return portTypeFile;}
    public static String getlocationFile() {return locationFile;}
}
