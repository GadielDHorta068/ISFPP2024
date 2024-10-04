package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class Cargar {
    public static Web cargarRed(String name, String equipmentFile, String connectionFile, String locationFile, String portTypeFile, String wireTypeFile, String equipmentTypeFile) throws FileNotFoundException {
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
        Equipment newEquipment;
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
            EquipmentType equipmentType = red.getEquipmentTypes().get(read.next());
            Location location = red.getLocations().get(read.next());
            portsArray = read.next().split(",");
            ipsArray = read.next().split(",");
            status = read.nextBoolean();

            /* addEquipment(String code, String description, String marca, String model, PortType portType, int cantidad,
            EquipmentType equipmentType, Location location, Boolean status)*/

            newEquipment = red.addEquipment(code, description, marca, model, equipmentType, location, status);
            for (int i = 0; i < ipsArray.length; i++)
                newEquipment.addIp(ipsArray[i]);

            for (int i = 0; i < portsArray.length; i += 2)
                newEquipment.addPort(red.getPortTypes().get(portsArray[i]), Integer.getInteger(portsArray[i + 1]));
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

    public static void loadConnections(Web red, String fileName) throws FileNotFoundException{
        Scanner read;
        WireType wireType;
        Equipment equipment1, equipment2;

        read = new Scanner(new File(fileName));
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            equipment1 = red.getHardware().get(read.next());
            equipment2 = red.getHardware().get(read.next());
            wireType = red.getWireTypes().get(read.next());

            red.addConection(wireType,equipment1,equipment2);
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