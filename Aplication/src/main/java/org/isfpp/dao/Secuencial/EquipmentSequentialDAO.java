package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.PortTypeDAO;
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
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.equipment");
        update = true;
    }

    private Hashtable<String, Equipment> readFromFile(String fileName) {
        Hashtable<String, Equipment> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
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
                if (minireader.length > 1)
                    for (int i = 0; i < minireader.length; i += 2)
                        for (int cap = 0;cap < Integer.parseInt(minireader[i + 1]);cap++)
                            equipment.addPort(portTypes.get(minireader[i]));

                minireader = inFile.next().split(",");
                for (int i = 0; i < minireader.length;i++)
                   equipment.addIp(minireader[i]);

                equipment.setStatus(inFile.nextBoolean());
                map.put(equipment.getCode(),equipment);
            }
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error in file record structure");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error reading from file.");
            illegalStateException.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String,Equipment> equipmentMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            HashMap<PortType, Integer> portMap;
            int index = 0;
            for (Equipment equipment: equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                outFile.format("%s;%s;%s;%s;%s;%s;", equipment.getCode(),
                                equipment.getDescription(),equipment.getMake(),equipment.getModel(),
                                equipment.getEquipmentType().getCode(),equipment.getLocation().getCode());

                index = 1;
                if (!portMap.isEmpty())
                    for (PortType portType: portMap.keySet()) {
                        if (!(index == portMap.size()))
                            outFile.format("%s,%s,", portType.getCode(), portMap.get(portType));
                        else
                            outFile.format("%s,%s;", portType.getCode(), portMap.get(portType));
                        index++;
                    }

                if (equipment.getIpAdresses().size() != 0)
                    for (int i = 0; i < equipment.getIpAdresses().size(); i++)
                        if (i + 1!= equipment.getIpAdresses().size())
                            outFile.format("%s,", equipment.getIpAdresses().get(i));
                        else
                            outFile.format("%s;", equipment.getIpAdresses().get(i));
                else
                    outFile.format(";");

                if (equipment.isStatus())
                    outFile.format("true;\n");
                else
                    outFile.format("false;\n");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error Not found file.");
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file.");
        } finally {
            if (outFile != null)
                outFile.close();
        }
    }

    @Override
    public void insert(Equipment equipment) {
        map.put(equipment.getCode(),equipment);
        writeToFile(map,fileName);
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
