package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

import java.util.Scanner;

public class Cargar {
    public static Web cargarRed(String nombre, HashMap<String, Equipment> Hardware, HashMap<String, Connection> connections, HashMap<String, Location>) {
        Web red = new Web(nombre);

        //Varios io stream por archivo en orden para no generar una conexion a la nada si no estan los nodos


        return red;
    }

    public HashMap<String, Equipment> cargarEquipments(String fileName, HashMap<String, PortType> portTypes,
                   HashMap<String, EquipmetType> equipmetTypes, HashMap<String, Location> locations) throws FileNotFoundException {
        HashMap<String, Equipment> hardware = new HashMap<>();
        Scanner read;
        String code, description, marca, model;
        String[] portsArray, ipsArray;
        boolean status;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            marca = read.next();
            model = read.next();
            EquipmetType equipmetType = equipmetTypes.get(read.next());
            Location location = locations.get(read.next());
            portsArray = read.next().split(",");
            ipsArray = read.next().split(",");
            status = read.nextBoolean();

            Equipment newEquipment = new Equipment(code, description, marca, model,
                    null, equipmetType, location, status);
            for (int i = 0; i < ipsArray.length; i++)
                newEquipment.addIp(ipsArray[i]);
            for (int i = 0; i < portsArray.length; i += 2)
                newEquipment.addPort(portTypes.get(portsArray[i]), Integer.getInteger(portsArray[i + 1]));

        }
        read.close();
        return hardware;
    }

    public List<PortType> cargarPortTypes(String fileName) throws FileNotFoundException{
        List<PortType> ports = new ArrayList<>();
        Scanner read;
        String code, description;
        int speed;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            speed = read.nextInt();

            PortType newPortType = new PortType(code, description, speed);
            ports.add(newPortType);
        }
        read.close();
        return ports;
    }

    public HashMap<String, Connection> cargarConnections(String fileName,
                    HashMap<String, WireType> wireList, HashMap<String, Equipment> hardware) throws FileNotFoundException{

        HashMap<String, Connection> connections = new HashMap<>();
        Scanner read;
        WireType wireTipe;
        Equipment equipment1, equipment2;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            equipment1 = hardware.get(read.next());
            equipment2 = hardware.get(read.next());
            wireTipe = wireList.get(read.next());

            Connection newConnection = new Connection(wireTipe, equipment1, equipment2);
            connections.put(equipment1.getCode() + "-" + equipment2.getCode(), newConnection);
        }

        read.close();
        return connections;
    }

    public HashMap<String, WireType> cargarWireType(String fileName) throws FileNotFoundException{
        HashMap<String, WireType> wireList = new HashMap<>();
        Scanner read;
        String code, description;
        int speed;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            speed = read.nextInt();

            WireType newWireType = new WireType(code, description, speed);
            wireList.put(code, newWireType);
        }
        read.close();
        return wireList;
    }

    public HashMap<String, EquipmentType> cargarEquipmentType(String fileName) throws FileNotFoundException{
        HashMap<String, EquipmentType> equipmentList = new HashMap<>();
        Scanner read;
        String code, description;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            EquipmentType newEquipmentType = new EquipmentType(code, description);
            equipmentList.put(code, newEquipmentType);
        }

        read.close();
        return equipmentList;
    }

    public HashMap<String, Location> cargarLocations(String fileName) throws FileNotFoundException{
        HashMap<String, Location> locationList = new HashMap<>();
        Scanner read;
        String code, description;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            Location newLocation = new Location(code, description);
            locationList.put(code, newLocation);
        }

        read.close();
        return locationList;
    }
}