package org.isfpp.dao.Secuencial;


import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.PortType;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class PortTypeSequentialDAO implements PortTypeDAO {
    private HashMap<String, PortType> map;
    private String name;
    private boolean update;

    public PortTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("PortType");
        update = true;
    }

    private HashMap<String, PortType> readFromFile(String fileName) {
        HashMap<String, PortType> map = new HashMap<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                PortType portType = new PortType();
                portType.setCode(inFile.next());
                portType.setDescription(inFile.next());
                portType.setCode(inFile.next());
                map.put(portType.getCode(),portType);
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

    private void writeToFile(HashMap<String,PortType> portTypeMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            for (PortType portType: portTypeMap.values()) {
                outFile.format("%s;%s;%s;\n", portType.getCode(), portType.getDescription(), portType.getSpeed());
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
    public void insert(PortType type) {
        map.put(type.getCode(),type);
        writeToFile(map, name);
        update = true;
    }

    @Override
    public void update(PortType portType) {
        map.replace(portType.getCode(),portType);
        update = true;
    }

    @Override
    public void erase(PortType portType) {
        map.remove(portType.getCode());
        writeToFile(map,name);
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
}
