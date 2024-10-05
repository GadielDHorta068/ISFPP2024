package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class Cargar {
    public static Web cargarRed(String name, String equipmentFile, String connectionFile, String locationFile, String portTypeFile, String wireTypeFile, String equipmentTypeFile) throws FileNotFoundException {
        //Varios io stream por archivo en orden para no generar una conexion a la nada si no estan los nodos
        Web red = new Web(name);

        loadPortTypes(red, portTypeFile);
        loadWireType(red, wireTypeFile);
        loadEquipmentType(red, equipmentTypeFile);
        loadLocations(red, locationFile);
        loadEquipments(red, equipmentFile);
        loadConnections(red, connectionFile);

        return red;
    }

    public static void loadEquipments(Web red, String fileName) throws FileNotFoundException {
        Scanner read;
        String code, description, make, model;
        String[] portsArray, ipsArray;
        boolean status;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            make = read.next();
            model = read.next();
            EquipmentType equipmentType = red.getEquipmentTypes().get(read.next());
            Location location = red.getLocations().get(read.next());
            portsArray = read.next().split(",");
            ipsArray = read.next().split(",");
            status = read.nextBoolean();

            Equipment newEquipment = red.addEquipment(code, description, make, model, red.getPortTypes().get(portsArray[0]), Integer.getInteger(portsArray[1]),equipmentType, location, status);
            for (int i = 0; i < ipsArray.length; i++)
                newEquipment.addIp(ipsArray[i]);

            for (int i = 2; i < portsArray.length; i += 2)
                newEquipment.addPort(red.getPortTypes().get(portsArray[i]));
        }
        read.close();
    }

    public static void loadPortTypes(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        String code, description;
        int speed;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            speed = read.nextInt();

            red.addPort(code, description, speed);
        }
        read.close();
    }
    //modificarFile
    public static void loadConnections(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        WireType wireType;
        Port port1, port2;
        String[] portReader;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            portReader = read.next().split(",");
            port1 = red.getHardware().get(portReader[0]).checkPort(red.getPortTypes().get(portReader[1]));
            portReader = read.next().split(",");
            port2 = red.getHardware().get(portReader[0]).checkPort(red.getPortTypes().get(portReader[1]));

            wireType = red.getWireTypes().get(read.next());

            red.addConection(wireType,port1,port2);
        }
        read.close();
    }

    public static void loadWireType(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        String code, description;
        int speed;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            speed = read.nextInt();

            red.addWire(code, description, speed);
        }
        read.close();
    }

    public static void loadEquipmentType(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        String code, description;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            red.addEquipmentType(code, description);
        }

        read.close();
    }

    public static void loadLocations(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        String code, description;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");
        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            red.addLocation(code, description);
        }

        read.close();
    }
}