package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.PortType;

import java.io.*;
import java.util.*;

public class EquipmentTypeSequentialDAO implements EquipmentTypeDAO {
    private Hashtable<String, EquipmentType> map;
    private String fileName;
    private boolean update;

    public EquipmentTypeSequentialDAO() {
        fileName = CargarParametros.getquipementTypeFile();
        update = true;
    }

    private Hashtable<String, EquipmentType> readFromFile(String fileName) {
        Hashtable<String, EquipmentType> map = new Hashtable<>();
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
                EquipmentType equipmentType = new EquipmentType();
                equipmentType.setCode(inFile.next());
                equipmentType.setDescription(inFile.next());

                map.put(equipmentType.getCode(),equipmentType);
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

    private void writeToFile(Hashtable<String,EquipmentType> equipmentTypeMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (EquipmentType equipmentType: equipmentTypeMap.values()) {
                writer.write(String.format("%s;%s;", equipmentType.getCode(),equipmentType.getDescription()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file.");
        }
    }

    @Override
    public void insert(EquipmentType equipmentType) {
        map.put(equipmentType.getCode(),equipmentType);
        writeToFile(map,fileName);
        update = true;
    }

    @Override
    public void update(EquipmentType equipmentType) {
        map.replace(equipmentType.getCode(),equipmentType);
        update = true;
    }

    @Override
    public void erase(EquipmentType equipmentType) {
        map.remove(equipmentType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String,EquipmentType> searchAll() {
        if (update) {
            map = readFromFile("data" + File.separator + "tipoEquipo.txt");
            update = false;
        }
        return map;
    }
}
