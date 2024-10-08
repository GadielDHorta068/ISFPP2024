package org.isfpp.dao.Secuencial;

import org.isfpp.dao.LocationDAO;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.Location;
import org.isfpp.modelo.PortType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class LocationSequentialDAO implements LocationDAO {
    private Hashtable<String, Location> map;
    private String fileName;
    private boolean update;

    public LocationSequentialDAO() {
        fileName = CargarParametros.getlocationFile();
        update = true;
    }

    private Hashtable<String, Location> readFromFile(String fileName) {
        Hashtable<String, Location> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + fileName);
            }
            inFile = new Scanner(inputStream);
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

    private void writeToFile(Hashtable<String,Location> locationMap, String fileName) {
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
