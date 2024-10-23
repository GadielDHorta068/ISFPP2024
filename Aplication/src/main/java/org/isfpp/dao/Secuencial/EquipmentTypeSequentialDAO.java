package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.datos.CargarParametros;
import org.isfpp.modelo.EquipmentType;

import java.io.*;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EquipmentTypeSequentialDAO implements EquipmentTypeDAO {
    private Hashtable<String, EquipmentType> map;
    private String fileName;
    private boolean update;

    public EquipmentTypeSequentialDAO() {
        // Ruta relativa o absoluta del archivo
        fileName = "data" + File.separator + "tipoEquipo.txt";
        update = true;
    }

    private Hashtable<String, EquipmentType> readFromFile(String fileName) {
        Hashtable<String, EquipmentType> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            // Usar FileInputStream para cargar el archivo desde el sistema de archivos
            File file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException("Archivo no encontrado: " + fileName);
            }
            inFile = new Scanner(new FileInputStream(file));
            inFile.useDelimiter("\\s*;\\s*");

            while (inFile.hasNext()) {
                EquipmentType equipmentType = new EquipmentType();
                equipmentType.setCode(inFile.next());
                equipmentType.setDescription(inFile.next());

                map.put(equipmentType.getCode(), equipmentType);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error abriendo el archivo: " + fileName);
            fileNotFoundException.printStackTrace();
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error en la estructura del archivo.");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error leyendo el archivo.");
            illegalStateException.printStackTrace();
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String, EquipmentType> equipmentTypeMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (EquipmentType equipmentType : equipmentTypeMap.values()) {
                writer.write(String.format("%s;%s;", equipmentType.getCode(), equipmentType.getDescription()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error escribiendo en el archivo: " + fileName);
            e.printStackTrace();
        }
    }

    @Override
    public void insert(EquipmentType equipmentType) {
        map.put(equipmentType.getCode(), equipmentType);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void update(EquipmentType equipmentType) {
        map.replace(equipmentType.getCode(), equipmentType);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void erase(EquipmentType equipmentType) {
        map.remove(equipmentType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String, EquipmentType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }
}
