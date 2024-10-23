package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Clase para guardar datos en archivos.
 */
public class Guardar {

    /**
     * Método para guardar todos los datos de la red en archivos.
     *
     * @param red       Objeto Web que contiene los datos de la red.
     * @param directory Directorio donde se guardarán los archivos.
     * @throws IOException Si ocurre un error de E/S.
     */
    public void saveAll(LAN red, String directory) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Cargar.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: " + "config.properties");
            }
            properties.load(input);
        }
        // Obtener rutas de los archivos desde el archivo properties
        String equipmentFile = directory + "/" + properties.getProperty("rs.equipment");
        String locationFile = directory + "/" + properties.getProperty("rs.location");
        String connectionFile = directory + "/" + properties.getProperty("rs.connection");
        String wireTypeFile = directory + "/" + properties.getProperty("rs.wireType");
        String portTypeFile = directory + "/" + properties.getProperty("rs.portType");
        String equipmentTypeFile = directory + "/" + properties.getProperty("rs.equipmentType");

        // Guardar los datos en los archivos correspondientes
        saveEquipments(equipmentFile, red);
        saveLocations(locationFile, red);
        saveConnections(connectionFile, red);
        saveWireTypes(wireTypeFile, red);
        savePortTypes(portTypeFile, red);
        saveEquipmentTypes(equipmentTypeFile, red);
    }

    /**
     * Método para guardar datos de los equipos.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de los equipos.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void saveEquipments(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Equipment equipment : red.getHardware().values()) {
                //SW02;Switch 2;HP Aruba;1930 48G 4SFP/SFP+;SW;SS;1G,48,SFP+,4;166.82.100.100;true;
                // SW02;Switch 2;HP Aruba;1930 48G 4SFP/SFP+;SW;SS;1G,96;SFP+,4;166.82.100.100,166.82.1.14,166.82.100.103;true
                StringBuilder data = new StringBuilder(equipment.getCode() + ";" +
                        equipment.getDescription() + ";" +
                        equipment.getMake() + ";" +
                        equipment.getModel() + ";" +
                        equipment.getEquipmentType().getCode() + ";" +
                        equipment.getLocation().getCode() + ";");

                HashMap<PortType, Integer> ports = equipment.getAllPortsTypes();
                for (PortType portType : ports.keySet()) {
                    data.append(portType.getCode()).append(",").append(ports.get(portType)).append(";");
                }


                StringJoiner ipJoiner = new StringJoiner(",");
                for (String ip : equipment.getIpAdresses()) {
                    ipJoiner.add(ip);
                }
                data.append(ipJoiner.toString());

                data.append(";");
                data.append(equipment.isStatus() ? "true" : "false");
                data.append(";");
                writer.write(data.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para guardar datos de las conexiones.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de las conexiones.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void saveConnections(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Connection connection : red.getConnections()) {
                //SW01;100M;SW02;1G;C5E;
                String data = connection.getPort2().getEquipment().getCode() + ";" +
                        connection.getPort2().getPortType().getCode() + ";" +
                        connection.getPort1().getEquipment().getCode() + ";" +
                        connection.getPort1().getPortType().getCode() + ";" +
                        connection.getWire().getCode() + ";";
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para guardar datos de los tipos de cables.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de los tipos de cables.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void saveWireTypes(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (WireType wireType : red.getWireTypes().values()) {
                String data = wireType.getCode() + ";" +
                        wireType.getDescription() + ";" +
                        wireType.getSpeed() + ";";
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para guardar datos de los tipos de equipos.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de los tipos de equipos.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void saveEquipmentTypes(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (EquipmentType equipmentType : red.getEquipmentTypes().values()) {
                String data = equipmentType.getCode() + ";" +
                        equipmentType.getDescription() + ";";
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para guardar datos de los tipos de puertos.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de los tipos de puertos.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void savePortTypes(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (PortType portType : red.getPortTypes().values()) {
                //SFP;4 Gbps Fibra;40;
                //SFP;4 Gbps Fibra;40;
                String data = portType.getCode() + ";" +
                        portType.getDescription() + ";" +
                        portType.getSpeed() + ";";
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para guardar datos de las ubicaciones.
     *
     * @param fileName Nombre del archivo donde se guardarán los datos de las ubicaciones.
     * @param red      Objeto Web que contiene los datos de la red.
     */
    public void saveLocations(String fileName, LAN red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Location location : red.getLocations().values()) {
                String data = location.getCode() + ";" +
                        location.getDescription() + ";";
                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}