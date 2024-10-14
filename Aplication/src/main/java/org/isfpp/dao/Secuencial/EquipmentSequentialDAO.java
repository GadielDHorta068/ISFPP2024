package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;

import java.io.*;
import java.util.*;

public class EquipmentSequentialDAO implements EquipmentDAO{
    private Hashtable<String, Equipment> map;
    private String fileName;
    private boolean update;
    private Hashtable<String, PortType> portTypes;
    private Hashtable<String,EquipmentType> equipmentTypes;
    private Hashtable<String,Location> locations;

    public EquipmentSequentialDAO() {
        equipmentTypes = readEquipmentTypes();
        locations = readLocations();
        portTypes = readPortTypes();
        fileName = CargarParametros.getequipementFile();
        update = true;
    }

    private Hashtable<String, Equipment> readFromFile(String fileName) {
        Hashtable<String, Equipment> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + fileName);
            }
            inFile = new Scanner(inputStream);
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
                    for (int cap = 0;cap < Integer.parseInt(minireader[i + 1]);cap++)
                        equipment.addPort(portTypes.get(minireader[i]));

                minireader = inFile.next().split(",");
                for (int i = 0; i < minireader.length;i++)
                   equipment.addIp(minireader[i]);

                equipment.setStatus(inFile.nextBoolean());
                map.put(equipment.getCode(),equipment);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error opening file.");
            fileNotFoundException.printStackTrace();
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error in file record structure");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error reading from file.");
            illegalStateException.printStackTrace();
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String,Equipment> equipmentMap, String fileName) {
        System.out.println(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            HashMap<PortType, Integer> portMap;
            for (Equipment equipment: equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                writer.write(String.format("%s;%s;%s;%s;%s;%s;",equipment.getCode(),
                        equipment.getDescription(),equipment.getMake(),equipment.getModel(),
                        equipment.getEquipmentType().getCode(),equipment.getLocation().getCode()));

                for (PortType portType: portMap.keySet())
                    writer.write(String.format("%s,%s",portType.getCode(),portMap.get(portType)));
                writer.write(";");

                for (int i = 1; i<equipment.getIpAdresses().size();i++)
                    if (!(i == equipment.getIpAdresses().size()))
                        writer.write(String.format("%s,",equipment.getIpAdresses().get(i)));
                    else
                        writer.write(String.format("%s;",equipment.getIpAdresses().get(i)));

                if (equipment.isStatus())
                    writer.write(String.format("true;"));
                else
                    writer.write(String.format("false;"));

                writer.newLine();  // Escribir salto de línea después de cada línea
            }
            System.out.println("Archivo reescrito exitosamente.");
        }catch (IOException e) {
            e.printStackTrace();  // Manejo de excepciones
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file.");
        }
    }

    @Override
    public void insert(Equipment equipment) {
        map.put(equipment.getCode(),equipment);
        //writeToFile(map,fileName);
        update = true;
    }

    @Override
    public void update(Equipment equipment) {
        map.replace(equipment.getCode(),equipment);
        update = true;
    }

    @Override
    public void erase(Equipment equipment) {
        map.remove(equipment.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String,Equipment> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }

    private Hashtable<String, EquipmentType> readEquipmentTypes() {
        EquipmentTypeDAO equipmentTypeDAO = new EquipmentTypeSequentialDAO();
       return equipmentTypeDAO.searchAll();
    }
    private Hashtable<String,Location> readLocations(){
        LocationDAO locationDAO = new LocationSequentialDAO();
        return  locationDAO.searchAll();
    }

    private Hashtable<String, PortType> readPortTypes() {
        PortTypeDAO portTypeDAO = new PortTypeSequentialDAO();
        return portTypeDAO.searchAll();
    }
}
