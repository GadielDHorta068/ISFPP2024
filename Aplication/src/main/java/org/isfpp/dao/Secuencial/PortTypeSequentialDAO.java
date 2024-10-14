package org.isfpp.dao.Secuencial;


import org.isfpp.dao.PortTypeDAO;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.PortType;

import java.io.*;
import java.util.*;

public class PortTypeSequentialDAO implements PortTypeDAO {
    private Hashtable<String, PortType> map;
    private String fileName;
    private boolean update;

    public PortTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.portType");
        update = true;
    }

    private Hashtable<String, PortType> readFromFile(String fileName) {
        Hashtable<String, PortType> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                PortType portType = new PortType();
                portType.setCode(inFile.next());
                portType.setDescription(inFile.next());
                portType.setSpeed(inFile.nextInt());
                map.put(portType.getCode(),portType);
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

    private void writeToFile(Hashtable<String,PortType> portTypeMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (PortType portType: portTypeMap.values()) {
                writer.write(String.format("%s;%s;%s;\n", portType.getCode(), portType.getDescription(), portType.getSpeed()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(PortType type) {
        map.put(type.getCode(),type);
        writeToFile(map, fileName);
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
        writeToFile(map,fileName);
        update = true;
    }

    @Override
    public Hashtable<String,PortType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }
}
