package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.EquipmentType;

import java.io.*;
import java.util.*;

public class EquipmentTypeSequentialDAO implements EquipmentTypeDAO {
    private static String fileName;
    private Hashtable<String, EquipmentType> map;
    private boolean update;

    public EquipmentTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.equipmentType");
        System.out.println("la"+ fileName);
        update = true;
    }

    private Hashtable<String, EquipmentType> readFromFile(String fileName) {
        Hashtable<String, EquipmentType> map = new Hashtable<>();
        Scanner inFile = null;

        System.out.println(fileName);
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
    public void insertAllIn(String directory) {
        boolean check = true;
        // Validación: el directorio no debe ser nulo ni vacío
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            check = false;
        }

        // Validación: Verificar si el directorio existe y es un directorio válido
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            check = false;
        }

        // Crear la ruta completa al archivo
        File file = new File(directory, fileName);

        // Validación: Verificar si el archivo existe y es un archivo regular
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            check = false;
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            check = false;
        }

        // Validación: Verificar si el archivo es legible
        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            check = false;
        }

        if (check)
            writeToFile(map, file.getAbsolutePath());
    }

    @Override
    public Hashtable<String,EquipmentType> searchAll() {
        if (update) {
            map = readFromFile("data" + File.separator + "tipoEquipo.txt");
            update = false;
        }
        return map;
    }

    @Override
    public Hashtable<String, EquipmentType> searchAllIn(String directory) {
        // Validación: el directorio no debe ser nulo ni vacío
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            return new Hashtable<>();
        }

        // Validación: Verificar si el directorio existe y es un directorio válido
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            return new Hashtable<>();

        }
        // Crear la ruta completa al archivo
        File file = new File(directory, fileName);

        // Validación: Verificar si el archivo existe y es un archivo regular
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        // Validación: Verificar si el archivo es legible
        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            return new Hashtable<>();
        }
        Hashtable<String,EquipmentType> equipmentHashtable =  readFromFile(file.getAbsolutePath());
        // Intentar leer el archivo y manejar posibles excepciones
        // Leer el archivo y devolver la lista de conexiones

        update = false;
        return equipmentHashtable; // Retornar la lista de conexiones leídas
    }
}
