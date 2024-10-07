package org.isfpp.dao.Secuencial;

import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.modelo.WireType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WireTypeSequentialDAO implements WireTypeDAO {
    private HashMap<String, WireType> map;
    private String name;
    private boolean update;

    public WireTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("Equipments");
        update = true;
    }

    private HashMap<String, WireType> readFromFile(String fileName) {
        HashMap<String, WireType> map = new HashMap<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
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

    private void writeToFile(HashMap<String,WireType> wireTypeMap, String fileName) {
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
        writeToFile(map,name);
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
        writeToFile(map, name);
        update = true;
    }

    @Override
    public List<WireType> searchAll() {
        if (update) {
            map = readFromFile(name);
            update = false;
        }
        return new ArrayList<>(map.values());
    }
}
