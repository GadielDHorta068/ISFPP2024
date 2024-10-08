package org.isfpp.dao.Secuencial;


import org.isfpp.dao.PortTypeDAO;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.PortType;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class PortTypeSequentialDAO implements PortTypeDAO {
    private Hashtable<String, PortType> map;
    private String fileName;
    private boolean update;

    public PortTypeSequentialDAO() {
        fileName = CargarParametros.getportTypeFile();
        update = true;
    }

    private Hashtable<String, PortType> readFromFile(String fileName) {
        Hashtable<String, PortType> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            InputStream inputStream = Cargar.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + fileName);
            }
            inFile = new Scanner(inputStream);
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                PortType portType = new PortType();
                portType.setCode(inFile.next());
                portType.setDescription(inFile.next());
                portType.setSpeed(inFile.nextInt());
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

    private void writeToFile(Hashtable<String,PortType> portTypeMap, String fileName) {
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
