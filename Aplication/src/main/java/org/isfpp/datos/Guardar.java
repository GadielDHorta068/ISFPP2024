package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Guardar {


    public void SaveEquipments(String fileName, HashMap<String, Equipment> hardware){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribimos los objetos en el archivo en el formato personalizado
            String data;
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (Equipment equipment: hardware.values()){
                data = equipment.getCode()+";"+
                        equipment.getDescription()+";"+
                        equipment.getMarca()+";"+
                        equipment.getModelo()+";"+
                        equipment.getEquipmentType()+";"+
                        equipment.getLocation()+";";

                //i = 1;
                //VER UNA FORMA DE SACAR LA CANTIDAD DE PORTS Y TIPO
                //for (Port por:equipment.getPorts()){
                //VER
                //}

                i = 1;
                for (String ip: equipment.getIpAdresses()){
                    if (i < equipment.getIpAdresses().size())
                        data +=  ip+",";
                    else
                        data += ip+";";

                    i++;
                }

                //if (true)
                //    data += "true;";
                //else
                //   data += "false;";

                writer.write(data);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConnetions(String fileName, HashMap<String, Connection> connections) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (Connection connection : connections.values()) {
                data = connection.getEquipment1().getCode() + ";" +
                        connection.getEquipment2().getCode() + ";" +
                        connection.getWire().getCode() + ";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveWireTypes(String fileName, HashMap<String, WireType> wireTypes){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (WireType wireType : wireTypes.values()) {
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

    public void saveEquipmentTypes(String fileName, HashMap<String, EquipmentType> equipmentTypes){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (EquipmentType equipmentType : equipmentTypes.values()) {
                data = equipmentType.getCode() + ";" +
                        equipmentType.getDescription() + ";";

                writer.write(data);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePortTypes(String fileName, HashMap<String, PortType> portTypes){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (PortType portType : portTypes.values()) {
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

    public void saveLocations(String fileName, HashMap<String, Location> locations){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String data;

            for (Location location: locations.values()) {
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