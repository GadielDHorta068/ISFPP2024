package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.HashMap;

public class Guardar {

    public void saveAll(Web red, String equipmentFile, String equipmentTypeFile, String locationFile, String portTypeFile,
                        String wireTypeFile, String connectionFile){

        saveEquipments(equipmentFile, red);
        saveConnetions(equipmentFile,red);
        saveLocations(locationFile, red);
        saveEquipmentTypes(equipmentTypeFile, red);
        savePortTypes(portTypeFile, red);
        saveWireTypes(wireTypeFile, red);
    }

    public void saveEquipments(String fileName, Web red){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribimos los objetos en el archivo en el formato personalizado
            String data;
            HashMap<PortType, Integer> ports;
            StringBuilder sb = new StringBuilder();
            for (Equipment equipment: red.getHardware().values()){
                data = equipment.getCode()+";"+
                        equipment.getDescription()+";"+
                        equipment.getMake()+";"+
                        equipment.getModel()+";"+
                        equipment.getEquipmentType()+";"+
                        equipment.getLocation()+";";

                ports = equipment.getAllPortsTypes();
                int portSize = ports.size();
                for (PortType portType: ports.keySet()){
                    portSize--;
                    data += portType.getCode()+",";
                    if (portSize != 0)
                        data += ports.get(portType)+",";
                    else data+= ports.get(portType)+";";
                }

                for (String ip: equipment.getIpAdresses()){
                    if (!equipment.getIpAdresses().getLast().equals(ip))
                        data +=  ip+",";
                    else
                        data += ip+";";
                }

                if (equipment.isStatus())
                    data += "true;";
                else
                    data += "false;";

                writer.write(data);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConnetions(String fileName, Web red) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (Connection connection : red.getConections()) {
                data = connection.getPort1().getEquipment().getCode() + ","
                        + connection.getPort1().getPortType().getCode() + ";"
                        + connection.getPort2().getEquipment().getCode() + ","
                        + connection.getPort2().getPortType().getCode() + ";"
                        + connection.getWire().getCode() + ";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveWireTypes(String fileName, Web red){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (WireType wireType : red.getWireTypes().values()) {
                data = wireType.getCode() + ";" +
                        wireType.getDescription() + ";" +
                        wireType.getSpeed() + ";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveEquipmentTypes(String fileName, Web red){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (EquipmentType equipmentType : red.getEquipmentTypes().values()) {
                data = equipmentType.getCode() + ";" +
                        equipmentType.getDescription() + ";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePortTypes(String fileName, Web red){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (PortType portType : red.getPortTypes().values()) {
                data = portType.getCode()+";"+
                        portType.getDescription()+";"+
                        portType.getSpeed()+";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLocations(String fileName, Web red){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (Location location: red.getLocations().values()) {
                data = location.getCode()+";"+
                        location.getDescription()+";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}