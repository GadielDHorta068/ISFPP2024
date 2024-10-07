package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.PortType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class EquipmentTypeSequentialDAO implements EquipmentTypeDAO {
    private HashMap<String, EquipmentType> map;
    private String name;
    private boolean update;

    public EquipmentTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("Equipments");
        update = true;
    }

    private HashMap<String, EquipmentType> readFromFile(String fileName) {
        HashMap<String, EquipmentType> map = new HashMap<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
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

    private void writeToFile(HashMap<String,EquipmentType> equipmentTypeMap, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            for (EquipmentType equipmentType: equipmentTypeMap.values()) {
                outFile.format("%s;%s;\n", equipmentType.getCode(),equipmentType.getDescription());
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
    public void insert(EquipmentType equipmentType) {
        map.put(equipmentType.getCode(),equipmentType);
        writeToFile(map,name);
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
        writeToFile(map, name);
        update = true;
    }

    @Override
    public List<EquipmentType> searchAll() {
        if (update) {
            map = readFromFile(name);
            update = false;
        }
        return new ArrayList<>(map.values());
    }
}
