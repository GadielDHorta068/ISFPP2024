package org.isfpp.dao.Secuencial;

import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.WireType;

import java.io.*;
import java.util.*;

public class WireTypeSequentialDAO implements WireTypeDAO {
    private Hashtable<String, WireType> map;
    private String fileName;
    private boolean update;
//Aplication/src/main/resources/config.properties
    public WireTypeSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.wireType");
        update = true;
    }

    private Hashtable<String, WireType> readFromFile(String fileName) {
        Hashtable<String, WireType> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");

            while (inFile.hasNext()) {
                WireType wireType = new WireType();
                wireType.setCode(inFile.next());
                wireType.setDescription(inFile.next());
                wireType.setSpeed(inFile.nextInt());

                map.put(wireType.getCode(),wireType);
            }
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error in file record structure");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error reading from file.");
            illegalStateException.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally{
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String,WireType> wireTypeMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (WireType wireType: wireTypeMap.values()) {
                writer.write(String.format("%s;%s;%s;\n", wireType.getCode(), wireType.getDescription(), wireType.getSpeed()));
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
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
    public Hashtable<String,WireType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }


    @Override
    public Hashtable<String, WireType> searchAllIn(String directory) {
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
        Hashtable<String,WireType> equipmentHashtable =  readFromFile(file.getAbsolutePath());
        // Intentar leer el archivo y manejar posibles excepciones
        // Leer el archivo y devolver la lista de conexiones

        update = false;
        return equipmentHashtable; // Retornar la lista de conexiones leídas
    }

}
