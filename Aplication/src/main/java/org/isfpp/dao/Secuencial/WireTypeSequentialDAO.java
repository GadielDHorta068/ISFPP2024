package org.isfpp.dao.Secuencial;

import org.isfpp.dao.WireTypeDAO;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.WireType;

import java.io.*;
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
            File file = new File(fileName); // Cargar el archivo directamente
            inFile = new Scanner(file);
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                WireType wireType = new WireType();
                wireType.setCode(inFile.next());
                wireType.setDescription(inFile.next());
                wireType.setSpeed(inFile.nextInt());

                map.put(wireType.getCode(), wireType);
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
            if (inFile != null) {
                inFile.close();
            }
        }
        return map;
    }

    private void writeToFile(Hashtable<String, WireType> wireTypeMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (WireType wireType : wireTypeMap.values()) {
                writer.write(String.format("%s;%s;%s;\n", wireType.getCode(), wireType.getDescription(), wireType.getSpeed()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(WireType wireType) {
        map.put(wireType.getCode(), wireType);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void update(WireType wireType) {
        map.replace(wireType.getCode(), wireType);
        update = true;
    }

    @Override
    public void erase(WireType wireType) {
        map.remove(wireType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String, WireType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }// wewewe
}
