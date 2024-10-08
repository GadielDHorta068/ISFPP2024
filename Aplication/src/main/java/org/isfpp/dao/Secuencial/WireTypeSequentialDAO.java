package org.isfpp.dao.Secuencial;

import org.isfpp.dao.WireTypeDAO;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.WireType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class WireTypeSequentialDAO implements WireTypeDAO {
    private Hashtable<String, WireType> map;
    private String fileName;
    private boolean update;

    public WireTypeSequentialDAO() {
        fileName = CargarParametros.getWireTypeFile();
        update = true;
    }

    private Hashtable<String, WireType> readFromFile(String fileName) {
        Hashtable<String, WireType> map = new Hashtable<>();
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
                WireType wireType = new WireType();
                wireType.setCode(inFile.next());
                wireType.setDescription(inFile.next());
                wireType.setSpeed(inFile.nextInt());

                map.put(wireType.getCode(),wireType);
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

    private void writeToFile(Hashtable<String,WireType> wireTypeMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            for (WireType wireType: wireTypeMap.values()) {
                outFile.format("%s;%s;%s;\n", wireType.getCode(),wireType.getDescription(),wireType.getSpeed());
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
    public void insert(WireType wireType) {
        map.put(wireType.getCode(),wireType);
        writeToFile(map,fileName);
        update = true;
    }

    @Override
    public void update(WireType wireType) {
        map.replace(wireType.getCode(),wireType);
        update = true;
    }

    @Override
    public void erase(WireType wireType) {
        map.remove(wireType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String,WireType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }
}
