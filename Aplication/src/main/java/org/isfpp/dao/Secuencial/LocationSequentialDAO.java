package org.isfpp.dao.Secuencial;

import org.isfpp.dao.LocationDAO;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.Location;

import java.io.*;
import java.util.*;

public class LocationSequentialDAO implements LocationDAO {
    private Hashtable<String, Location> map;
    private String fileName;
    private boolean update;

    public LocationSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.location");
        update = true;
    }

    private Hashtable<String, Location> readFromFile(String fileName) {
        Hashtable<String, Location> map = new Hashtable<>();
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

    private void writeToFile(Hashtable<String,Location> locationMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Location location: locationMap.values()) {
             writer.write(String.format("%s;%s;\n", location.getCode(),location.getDescription()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Location location) {
        map.put(location.getCode(),location);
        writeToFile(map,fileName);
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
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String,Location> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }
}
