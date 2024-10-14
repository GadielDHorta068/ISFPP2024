package org.isfpp.datos;

import org.isfpp.modelo.*;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Properties;
import java.util.Scanner;

public class Cargar {

    /**
     * Carga una red a partir de varios archivos de configuración.
     *
     * @param name              Nombre de la red.
     * @param equipmentFile     Archivo de equipos.
     * @param connectionFile    Archivo de conexiones.
     * @param locationFile      Archivo de ubicaciones.
     * @param portTypeFile      Archivo de tipos de puertos.
     * @param wireTypeFile      Archivo de tipos de cables.
     * @param equipmentTypeFile Archivo de tipos de equipos.
     * @return Objeto Web que representa la red.
     * @throws FileNotFoundException Si alguno de los archivos no se encuentra.
     */
    public static Web cargarRed(String name, String equipmentFile, String connectionFile, String locationFile,
                                String portTypeFile, String wireTypeFile, String equipmentTypeFile) throws FileNotFoundException {
        Web red = new Web(name);

        loadEquipmentType(red, equipmentTypeFile);
        loadPortTypes(red, portTypeFile);
        loadLocations(red, locationFile);
        loadEquipments(red, equipmentFile);
        loadWireType(red, wireTypeFile);
        loadConnections(red, connectionFile);

        return red;
    }

    /**
     * Carga los equipos en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param filePath Ruta del archivo de equipos.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadEquipments(Web red, String filePath) throws FileNotFoundException {
        InputStream inputStream;

        // Verificar si es un archivo local
        File file = new File(filePath);
        if (file.exists()) {
            // Cargar desde el sistema de archivos local
            try (Scanner read = new Scanner(file)) {
                read.useDelimiter("\\s*;\\s*");
                processEquipments(read, red);
            }
        } else {
            // Cargar desde el JAR
            inputStream = Cargar.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + filePath);
            }

            try (Scanner read = new Scanner(inputStream)) {
                read.useDelimiter("\\s*;\\s*");
                processEquipments(read, red);
            }
        }
    }

    private static void processEquipments(Scanner read, Web red) {
        Equipment newEquipment;
        String code, description, marca, model;
        String[] portsArray, ipsArray;
        boolean status;
        String ipPattern = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";

        while (read.hasNextLine()) {
            String line = read.nextLine().trim();
            if (!line.endsWith(";")) {
                line += ";"; // Asegúrate de que la línea termine con un punto y coma
            }

            String[] parts = line.split(";");
            if (parts.length < 8) { // Debe haber al menos 8 partes (Código, descripción, marca, modelo, tipo, ubicación, IPs, estado)
                System.err.println("Error de entrada: Línea con formato incorrecto: " + line);
                continue;
            }

            try {
                code = parts[0]; // Código del equipo
                description = parts[1]; // Descripción
                marca = parts[2]; // Marca
                model = parts[3]; // Modelo
                EquipmentType equipmentType = red.getEquipmentTypes().get(parts[4]); // Tipo de equipo
                Location location = red.getLocations().get(parts[5]); // Ubicación
                portsArray = parts[6].split(","); // Puertos
                ipsArray = parts[7].split(","); // Direcciones IP
                String statusStr = parts[8]; // Estado

                // Validar el estado
                if (!statusStr.equalsIgnoreCase("true") && !statusStr.equalsIgnoreCase("false")) {
                    throw new InputMismatchException("El valor de estado no es válido: " + statusStr);
                }
                status = Boolean.parseBoolean(statusStr);

                // Crear el nuevo equipo
                newEquipment = red.addEquipment(code, description, marca, model,
                        red.getPortTypes().get(portsArray[0]),
                        Integer.parseInt(portsArray[1]),
                        equipmentType, location, status);

                if (newEquipment == null) {
                    System.err.println("Error al agregar equipo: " + code);
                    continue; // Saltar a la siguiente línea
                }

                // Manejo de direcciones IP, ya sea una única o múltiples
                for (String ip : ipsArray) {
                    ip = ip.trim();
                    if (!ip.isEmpty() && ip.matches(ipPattern)) {
                        System.out.println(ip);
                        newEquipment.addIp(ip);
                    }
                }

                // Manejo de puertos
                for (int i = 0; i < portsArray.length; i += 2) {
                    String portType = portsArray[i];
                    int portCount = Integer.parseInt(portsArray[i + 1]);
                    for (int cap = 0; cap < portCount; cap++) {
                        newEquipment.addPort(red.getPortTypes().get(portType));
                    }
                }
            } catch (InputMismatchException e) {
                System.err.println("Error de entrada: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    /**
     * Carga los tipos de puertos en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param filePath Ruta del archivo de tipos de puertos.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadPortTypes(Web red, String filePath) throws FileNotFoundException {
        File file = new File(filePath);

        // Verificar si es un archivo local
        if (file.exists()) {
            // Cargar desde el sistema de archivos local
            try (Scanner read = new Scanner(file)) {
                read.useDelimiter("\\s*;\\s*");
                while (read.hasNext()) {
                    String code = read.next();
                    String description = read.next();
                    int speed = read.nextInt();
                    red.addPort(code, description, speed);
                }
            }
        } else {
            // Cargar desde el JAR
            InputStream inputStream = Cargar.class.getResourceAsStream("/" + filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + filePath);
            }

            try (Scanner read = new Scanner(inputStream)) {
                read.useDelimiter("\\s*;\\s*");
                while (read.hasNext()) {
                    String code = read.next();
                    String description = read.next();
                    int speed = read.nextInt();
                    red.addPort(code, description, speed);
                }
            }
        }
    }

    /**
     * Carga las conexiones en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param fileName Nombre del archivo de conexiones.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadConnections(Web red, String fileName) throws FileNotFoundException {
        WireType wireType;
        Equipment equipment1, equipment2;
        PortType portType1, portType2;

        System.out.println("Cargando conexiones desde el archivo: " + fileName);

        InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);

        // Intentar cargar desde el sistema de archivos si no se encuentra en el classpath
        if (inputStream == null) {
            File file = new File(fileName);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
            } else {
                throw new FileNotFoundException("Archivo no encontrado: " + fileName);
            }
        }

        Scanner read = new Scanner(inputStream);
        read.useDelimiter("\\s*;\\s*");

        while (read.hasNext()) {
            equipment1 = red.getHardware().get(read.next());
            portType1 = red.getPortTypes().get(read.next());
            equipment2 = red.getHardware().get(read.next());
            portType2 = red.getPortTypes().get(read.next());
            wireType = red.getWireTypes().get(read.next());

            red.addConnection(equipment1.checkPort(portType1), equipment2.checkPort(portType2), wireType);
        }
        read.close();
    }

    /**
     * Carga los tipos de cables en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param filePath Ruta del archivo de tipos de cables.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadWireType(Web red, String filePath) throws FileNotFoundException {
        String code, description;
        int speed;

        File file = new File(filePath);

        // Verificar si es un archivo local
        if (file.exists()) {
            // Cargar desde el sistema de archivos local
            try (Scanner read = new Scanner(file)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();
                    speed = read.nextInt();

                    red.addWire(code, description, speed);
                }
            }
        } else {
            // Cargar desde el JAR
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + filePath);
            }

            try (Scanner read = new Scanner(inputStream)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();
                    speed = read.nextInt();

                    red.addWire(code, description, speed);
                }
            }
        }
    }

    /**
     * Carga los tipos de equipos en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param filePath Ruta del archivo de tipos de equipos.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadEquipmentType(Web red, String filePath) throws FileNotFoundException {
        String code, description;

        File file = new File(filePath);

        // Verificar si es un archivo local
        if (file.exists()) {
            // Cargar desde el sistema de archivos local
            try (Scanner read = new Scanner(file)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();

                    red.addEquipmentType(code, description);
                }
            }
        } else {
            // Cargar desde el JAR
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + filePath);
            }

            try (Scanner read = new Scanner(inputStream)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();

                    red.addEquipmentType(code, description);
                }
            }
        }
    }

    /**
     * Carga las ubicaciones en la red a partir de un archivo.
     *
     * @param red      Objeto Web que representa la red.
     * @param filePath Ruta del archivo de ubicaciones.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public static void loadLocations(Web red, String filePath) throws FileNotFoundException {
        String code, description;

        File file = new File(filePath);

        // Verificar si es un archivo local
        if (file.exists()) {
            // Cargar desde el sistema de archivos local
            try (Scanner read = new Scanner(file)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();

                    red.addLocation(code, description);
                }
            }
        } else {
            // Cargar desde el JAR
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + filePath);
            }

            try (Scanner read = new Scanner(inputStream)) {
                read.useDelimiter("\\s*;\\s*");

                while (read.hasNext()) {
                    code = read.next();
                    description = read.next();

                    red.addLocation(code, description);
                }
            }
        }
    }

    /**
     * Método para cargar la red desde un archivo de propiedades.
     * Debe ser llamado de la siguiente forma:
     * <blockquote><pre>
     * Web web = Cargar.cargarRedDesdePropiedades(config.properties)
     * </pre></blockquote>
     *
     * @param propertiesFile Archivo de propiedades.
     * @return Objeto Web que representa la red.
     * @throws IOException Si no se encuentra el archivo de propiedades.
     */
    public static Web cargarRedDesdePropiedades(String propertiesFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Cargar.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new FileNotFoundException("Archivo de propiedades no encontrado: " + propertiesFile);
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
        System.out.println(portTypeFile);

        return cargarRed(name, equipmentFile, connectionFile, locationFile, portTypeFile, wireTypeFile, equipmentTypeFile);
    }

    /**
     * Carga la red desde un directorio específico.
     *
     * @param directoryPath Ruta del directorio.
     * @return Objeto Web que representa la red.
     * @throws IOException Si no se encuentra la carpeta 'data' o algún archivo.
     */
    public Web cargarRedDesdeDirectorio(String directoryPath) throws IOException {
        File dataDir = new File(directoryPath, "data");
        if (!dataDir.exists()) {
            throw new FileNotFoundException("La carpeta 'data' no fue encontrada en: " + dataDir.getAbsolutePath());
        }

        String name = "red local";
        String equipmentFile = new File(dataDir, "equipo.txt").getAbsolutePath();
        String connectionFile = new File(dataDir, "conexion.txt").getAbsolutePath();
        String locationFile = new File(dataDir, "ubicacion.txt").getAbsolutePath();
        String portTypeFile = new File(dataDir, "tipoPuerto.txt").getAbsolutePath();
        String wireTypeFile = new File(dataDir, "tipoCable.txt").getAbsolutePath();
        String equipmentTypeFile = new File(dataDir, "tipoEquipo.txt").getAbsolutePath();
        System.out.println(portTypeFile);

        return cargarRed(name, equipmentFile, connectionFile, locationFile, portTypeFile, wireTypeFile, equipmentTypeFile);
    }
}