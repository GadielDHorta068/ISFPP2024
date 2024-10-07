package org.isfpp.dao.Secuencial;

import org.isfpp.dao.LocationDAO;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LocationSequentialDAO implements LocationDAO {
    private HashMap<String, Location> map;
    private String name;
    private boolean update;

    public LocationSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("Equipments");
        update = true;
    }

    private HashMap<String, Location> readFromFile(String fileName) {
        HashMap<String, Location> map = new HashMap<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            String[] minireader, minireader2;
            while (inFile.hasNext()) {
                Location location = new Location();
                location.setCode(inFile.next());
                location.setDescription(inFile.next());
                map.put(location.getCode(),location);
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

    private void writeToFile(HashMap<String,Location> locationMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            for (Location location: locationMap.values()) {
                outFile.format("%s;%s;\n", location.getCode(),location.getDescription());
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
    public void insert(Location location) {
        map.put(location.getCode(),location);
        writeToFile(map,name);
        update = true;
    }

    @Override
    public void update(Location location) {
        map.replace(location.getCode(),location);
        update = true;
    }

    @Override
    public void erase(Location location) {
        map.remove(location.getCode());
        writeToFile(map, name);
        update = true;
    }

    @Override
    public List<Location> searchAll() {
        if (update) {
            map = readFromFile(name);
            update = false;
        }
        return new ArrayList<>(map.values());
    }
}
