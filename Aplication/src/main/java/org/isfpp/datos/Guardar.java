package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class Guardar {

    public void saveAll(Web red, String directory) throws IOException {
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

    // Guardar datos de los equipos
    public void saveEquipments(String fileName, Web red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Equipment equipment : red.getHardware().values()) {
                //SW02;Switch 2;HP Aruba;1930 48G 4SFP/SFP+;SW;SS;1G,48,SFP+,4;166.82.100.100;true;
                //SW02;Switch 2;HP Aruba;1930 48G 4SFP/SFP+;SW;SS;1G,96;SFP+,4;166.82.100.100,166.82.1.14,166.82.100.103,true
                StringBuilder data = new StringBuilder(equipment.getCode() + ";" +
                        equipment.getDescription() + ";" +
                        equipment.getMake() + ";" +
                        equipment.getModel() + ";" +
                        equipment.getEquipmentType().getCode() + ";" +
                        equipment.getLocation().getCode() + ";");

                // Guardar tipos de puerto y cantidades
                HashMap<PortType, Integer> ports = equipment.getAllPortsTypes();
                for (PortType portType : ports.keySet()) {
                    data.append(portType.getCode()).append(",").append(ports.get(portType)).append(";");
                }

                // Guardar direcciones IP
                for (String ip : equipment.getIpAdresses()) {
                    data.append(ip).append(",");
                }

                data.append(equipment.isStatus() ? "true" : "false");

                writer.write(data.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Guardar conexiones
    public void saveConnections(String fileName, Web red) {
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

    // Guardar tipos de cable
    public void saveWireTypes(String fileName, Web red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (WireType wireType : red.getWireTypes().values()) {
                //C5;UTP Cat. 5;100;
                //C5;UTP Cat. 5;100;
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

    // Guardar tipos de equipos
    public void saveEquipmentTypes(String fileName, Web red) {
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

    // Guardar tipos de puertos
    public void savePortTypes(String fileName, Web red) {
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

    // Guardar ubicaciones
    public void saveLocations(String fileName, Web red) {
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
