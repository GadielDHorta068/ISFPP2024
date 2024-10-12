package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Cargar {
   public static Web cargarRed(String name, String equipmentFile, String connectionFile, String locationFile,
                               String portTypeFile, String wireTypeFile, String equipmentTypeFile) throws FileNotFoundException {
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
        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: " + fileName);
        }
        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        Equipment newEquipment;
        String code, description, marca, model;
        String[] portsArray, ipsArray;
        boolean status;

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
            System.out.println(status);

            newEquipment = red.addEquipment(code, description, marca, model, red.getPortTypes().get(portsArray[0]), Integer.parseInt(portsArray[1]), equipmentType, location, status);
            for (String s : ipsArray) newEquipment.addIp(s);

            for (int i = 0; i < portsArray.length; i += 2)
                for (int cap = 0;cap < Integer.parseInt(portsArray[i + 1]);cap++)
                    newEquipment.addPort(red.getPortTypes().get(portsArray[i]));
        }
        read.close();
    }

    public static void loadPortTypes(Web red, String fileName) throws FileNotFoundException {
        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: "+fileName);
        }
        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        String code, description;
        int speed;

        while (read.hasNext()) {
            code = read.next();
            description = read.next();
            speed = read.nextInt();

            red.addPort(code, description, speed);
        }
        read.close();
    }


    public static void loadConnections(Web red, String fileName) throws FileNotFoundException{
        WireType wireType;
        Equipment equipment1, equipment2;
        PortType portType1, portType2;

        // Mensaje de depuración
        System.out.println("Cargando conexiones desde el archivo: "+ fileName);

        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: "+fileName);
        }
        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            equipment1 = red.getHardware().get(read.next());
            portType1 = red.getPortTypes().get(read.next());
            equipment2 = red.getHardware().get(read.next());
            portType2 = red.getPortTypes().get(read.next());
            wireType = red.getWireTypes().get(read.next());

            red.addConnection(equipment1.checkPort(portType1),equipment2.checkPort(portType2),wireType);
        }
        read.close();
    }

    public static void loadWireType(Web red, String fileName) throws FileNotFoundException{
        String code, description;
        int speed;

        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: "+fileName);
        }
        Scanner read = new Scanner(inputStream);
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
        String code, description;
        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: "+fileName);
        }
        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            red.addEquipmentType(code, description);
        }

        read.close();
    }

    public static void loadLocations(Web red, String fileName) throws FileNotFoundException{

        String code, description;

        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("Archivo no encontrado: "+fileName);
        }
        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            code = read.next();
            description = read.next();

            red.addLocation(code, description);
        }

        read.close();
    }
    /**
     * Metodo para cargar la red desde el archivo propierties
     * El mismo debe ser llamado como el siguiente ejemplo
     *  <blockquote><pre>
     *      Web web = Cargar.cargarRedDesdePropiedades(config.properties)
     *  </pre></blockquote>
     * @param propertiesFile archivo de propiedades
     * @return  Web a ser devuelta
     * @throws IOException No se encuentra el archivo propierties
     */
    public static Web cargarRedDesdePropiedades(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Cargar.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: "+propertiesFile);
            }
            properties.load(input);
        }
        String name = properties.getProperty("rs.name", "defaultName");
        String equipmentFile = properties.getProperty("rs.equipment");
        String connectionFile = properties.getProperty("rs.connection");
        String locationFile = properties.getProperty("rs.location");
        String portTypeFile = properties.getProperty("rs.portType");
        String wireTypeFile = properties.getProperty("rs.wireType");
        String equipmentTypeFile = properties.getProperty("rs.equipmentType");

        return cargarRed(name, equipmentFile, connectionFile, locationFile, portTypeFile, wireTypeFile, equipmentTypeFile);
    }

}