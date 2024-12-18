package org.isfpp.dao.abstractDao;

import org.isfpp.connection.Factory;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;

import java.io.*;
import java.util.*;

public abstract class AbstractEquipmentDAO {
    protected Hashtable<String, PortType> portTypes;
    protected Hashtable<String, EquipmentType> equipmentTypes;
    protected Hashtable<String, Location> locations;
    protected String fileName;

    public AbstractEquipmentDAO(){
        equipmentTypes = readEquipmentTypes();
        locations = readLocations();
        portTypes = readPortTypes();
        ResourceBundle rb = ResourceBundle.getBundle("sequential");
        fileName = rb.getString("rs.equipment");
    }

    protected Hashtable<String, EquipmentType> readEquipmentTypes() {
        EquipmentTypeDAO equipmentTypeDAO = (EquipmentTypeDAO) Factory.getInstancia("EQUIPMENTTYPE");
        return equipmentTypeDAO.searchAll();
    }

    protected Hashtable<String, Location> readLocations() {
        LocationDAO locationDAO = (LocationDAO) Factory.getInstancia("LOCATION");
        return locationDAO.searchAll();
    }

    protected Hashtable<String, PortType> readPortTypes() {
        PortTypeDAO portTypeDAO = (PortTypeDAO) Factory.getInstancia("PORTTYPE");
        return portTypeDAO.searchAll();
    }

    protected Hashtable<String, Equipment> readFromFile(String fileName) {
        Hashtable<String, Equipment> map = new Hashtable<>();
        Scanner inFile = null;


        try {
            // Cambia a FileInputStream para leer del sistema de archivos
            inFile = new Scanner(new FileInputStream(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            String[] minireader;
            while (inFile.hasNext()) {
                Equipment equipment = new Equipment();
                equipment.setCode(inFile.next());
                equipment.setDescription(inFile.next());
                equipment.setMake(inFile.next());
                equipment.setModel(inFile.next());
                equipment.setEquipmentType(equipmentTypes.get(inFile.next()));
                equipment.setLocation(locations.get(inFile.next()));
                minireader = inFile.next().split(",");
                for (int i = 0; i < minireader.length; i += 2)
                    for (int cap = 0; cap < Integer.parseInt(minireader[i + 1]); cap++)
                        equipment.addPort(portTypes.get(minireader[i]));

                minireader = inFile.next().split(",");
                for (String ip : minireader)
                    equipment.addIp(ip);

                equipment.setStatus(inFile.nextBoolean());
                map.put(equipment.getCode(), equipment);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error al abrir el archivo: " + fileName);
            fileNotFoundException.printStackTrace();
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error en la estructura del registro del archivo.");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error al leer desde el archivo.");
            illegalStateException.printStackTrace();
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    protected void writeToFile(Hashtable<String, Equipment> equipmentMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            HashMap<PortType, Integer> portMap;
            for (Equipment equipment : equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                writer.write(String.format("%s;%s;%s;%s;%s;%s;",
                        equipment.getCode(),
                        equipment.getDescription(),
                        equipment.getMake(),
                        equipment.getModel(),
                        equipment.getEquipmentType().getCode(),
                        equipment.getLocation().getCode()));
                int portIndex = 0;
                for (PortType portType : portMap.keySet()) {
                    writer.write(String.format("%s,%s%s", portType.getCode(), portMap.get(portType),
                            (portIndex < portMap.size() - 1 ? "," : ";")));
                    portIndex++;
                }
                for (int i = 0; i < equipment.getIpAdresses().size(); i++)
                    writer.write(String.format("%s%s", equipment.getIpAdresses().get(i),
                            (i < equipment.getIpAdresses().size() - 1 ? "," : ";")));

                writer.write(String.format("%s;", equipment.isStatus() ? "true" : "false"));
                writer.newLine();  // Escribir salto de línea después de cada línea
            }
            System.out.println("Archivo reescrito exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de excepciones
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error al escribir en el archivo.");
        }
    }

    protected void appendToFile(Equipment equipment, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            HashMap<PortType, Integer> portMap;
            portMap = equipment.getAllPortsTypes();
            writer.write(String.format("%s;%s;%s;%s;%s;%s;", equipment.getCode(),
                    equipment.getDescription(), equipment.getMake(), equipment.getModel(),
                    equipment.getEquipmentType().getCode(), equipment.getLocation().getCode()));

            for (PortType portType : portMap.keySet())
                writer.write(String.format("%s,%s", portType.getCode(), portMap.get(portType)));
            writer.write(";");

            for (int i = 0; i < equipment.getIpAdresses().size(); i++)
                writer.write(String.format("%s%s", equipment.getIpAdresses().get(i),
                        (i < equipment.getIpAdresses().size() - 1 ? "," : ";")));

            writer.write(String.format("%s;", equipment.isStatus() ? "true" : "false"));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file of Connection.");
            e.printStackTrace();
        }
    }

    public void insertAllIn(String directory, Hashtable<String, Equipment> map) {
        boolean check = true;
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            check = false;
        }

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            check = false;
        }

        File file = new File(directory, fileName);

        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            check = false;
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            check = false;
        }

        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            check = false;
        }


        if (check)
            writeToFile(map, file.getAbsolutePath());
    }

    public Hashtable<String, Equipment> searchAllIn(String directory) {
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            return new Hashtable<>();
        }

        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            return new Hashtable<>();

        }
        File file = new File(directory, fileName);

        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            return new Hashtable<>();
        }
        Hashtable<String,Equipment> equipmentHashtable =  readFromFile(file.getAbsolutePath());
        return equipmentHashtable; // Retornar la lista de conexiones leídas
    }

}
