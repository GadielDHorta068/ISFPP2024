package org.isfpp.dao.abstractDao;

import org.isfpp.modelo.EquipmentType;

import java.io.*;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AbstractEquimentTypeDAO {
    protected String fileName;

    public AbstractEquimentTypeDAO(){
        ResourceBundle rb = ResourceBundle.getBundle("sequential");
        fileName = rb.getString("rs.equipmentType");
    }

    protected Hashtable<String, EquipmentType> readFromFile(String fileName) {
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

    protected void writeToFile(Hashtable<String, EquipmentType> equipmentTypeMap, String fileName) {
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

    protected void appendToFile(EquipmentType equipmentType, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s;%s;", equipmentType.getCode(), equipmentType.getDescription()));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file of Connection.");
            e.printStackTrace();
        }
    }

    public void insertAllIn(String directory, Hashtable<String, EquipmentType> map) {
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

        return equipmentHashtable; // Retornar la lista de conexiones leídas
    }
}
