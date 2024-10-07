package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EquipmentSequentialDAO implements EquipmentDAO{
    private HashMap<String, Equipment> map;
    private String name;
    private boolean update;
    private Hashtable<String, PortType> portTypes;
    private Hashtable<String,EquipmentType> equipmentTypes;
    private Hashtable<String,Location> locations;

    public EquipmentSequentialDAO() {
        equipmentTypes = readEquipmentTypes();
        locations = readLocations();
        portTypes = readPortTypes();
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("Equipments");
        update = true;
    }

    private HashMap<String, Equipment> readFromFile(String fileName) {
        HashMap<String, Equipment> map = new HashMap<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            String[] minireader, minireader2;
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

                minireader2 = inFile.next().split(",");
                for (int i = 0; i< minireader2.length;i++)
                   equipment.addIp(minireader2[i]);

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

    private void writeToFile(HashMap<String,Equipment> equipmentMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            HashMap<PortType, Integer> portMap;
            for (Equipment equipment: equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                outFile.format("%s;%s;%s;%s;%s;%s;", equipment.getCode(),
                                equipment.getDescription(),equipment.getMake(),equipment.getModel(),
                                equipment.getEquipmentType().getCode(),equipment.getLocation().getCode());

                for (PortType portType: portMap.keySet())
                    outFile.format("%s,%s",portType.getCode(),portMap.get(portType));
                outFile.format(";");

                for (int i = 1; i<equipment.getIpAdresses().size();i++)
                    if (!(i ==equipment.getIpAdresses().size()))
                        outFile.format("%s,",equipment.getIpAdresses().get(i));
                    else
                        outFile.format("%s;",equipment.getIpAdresses().get(i));

                if (equipment.isStatus())
                    outFile.format("true;\n");
                else
                    outFile.format("false;\n");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error creating file.");
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
        writeToFile(map,name);
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
        writeToFile(map, name);
        update = true;
    }

    @Override
    public List searchAll() {
        if (update) {
            map = readFromFile(name);
            update = false;
        }
        return new ArrayList<>(map.values());
    }

    private Hashtable<String, EquipmentType> readEquipmentTypes() {
        Hashtable<String, EquipmentType> equipmentTypes = new Hashtable<String, EquipmentType>();
        EquipmentTypeDAO equipmentTypeDAO = new EquipmentTypeSequentialDAO();
        List<EquipmentType> typeList = equipmentTypeDAO.searchAll();
        for (EquipmentType type: typeList)
            equipmentTypes.put(type.getCode(), type);
        return equipmentTypes;
    }
    private Hashtable<String,Location> readLocations(){
        Hashtable<String,Location> locations = new Hashtable<>();
        LocationDAO locationDAO = new LocationSequentialDAO();
        List<Location> locationList = locationDAO.searchAll();
        for (Location location: locationList)
            locations.put(location.getCode(),location);
        return locations;
    }

    private Hashtable<String, PortType> readPortTypes() {
        Hashtable<String,PortType> portTypes = new Hashtable<>();
        PortTypeDAO portTypeDAO = new PortTypeSequentialDAO();
        List<PortType> portTypeList = portTypeDAO.searchAll();
        for (PortType portType: portTypeList)
            portTypes.put(portType.getCode(),portType);
        return portTypes;
    }
}
