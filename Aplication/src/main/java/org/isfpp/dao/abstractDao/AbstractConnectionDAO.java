package org.isfpp.dao.abstractDao;

import org.isfpp.connection.Factory;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.WireType;

import java.io.*;
import java.util.*;

public abstract class AbstractConnectionDAO {
    protected Hashtable<String, Equipment> equipments;
    protected Hashtable<String, PortType> portTypes;
    protected Hashtable<String, WireType> wireTypes;
    protected String fileName;

    public AbstractConnectionDAO(){
        equipments = readEquipments();
        portTypes = readPortTypes();
        wireTypes = readWireTypes();
        ResourceBundle rb = ResourceBundle.getBundle("sequential");
        fileName = rb.getString("rs.connection");
    }

    protected Hashtable<String, Equipment> readEquipments() {
        EquipmentDAO equipmentDAO = (EquipmentDAO) Factory.getInstancia("EQUIPMENT");
        return equipmentDAO.searchAll();
    }

    protected Hashtable<String, PortType> readPortTypes() {
        PortTypeDAO portTypeDAO = (PortTypeDAO) Factory.getInstancia("PORTTYPE");
        return portTypeDAO.searchAll();
    }

    protected Hashtable<String, WireType> readWireTypes() {
        WireTypeDAO wireTypeDAO = (WireTypeDAO) Factory.getInstancia("WIRETYPE");
        return wireTypeDAO.searchAll();
    }

    protected List<Connection> readFromFile(String fileName) {
        List<Connection> connections = new ArrayList<>();
        Scanner inFile = null;

        try {
            File file = new File(fileName); // Carga el archivo directamente
            inFile = new Scanner(file);
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                Connection connection = new Connection();
                connection.setPort1(equipments.get(inFile.next()).checkPort(portTypes.get(inFile.next())));
                connection.setPort2(equipments.get(inFile.next()).checkPort(portTypes.get(inFile.next())));
                connection.setWire(wireTypes.get(inFile.next()));
                connections.add(connection);
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
        return connections;
    }

    protected void writeToFile(List<Connection> connectionList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Connection connection : connectionList) {
                writer.write(String.format("%s;%s;%s;%s;%s;\n",
                        connection.getPort1().getEquipment().getCode(),
                        connection.getPort1().getPortType().getCode(),
                        connection.getPort2().getEquipment().getCode(),
                        connection.getPort2().getPortType().getCode(),
                        connection.getWire().getCode()));

                writer.newLine();  // Escribir salto de línea después de cada línea
            }
        }catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error creating file Connection.");
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file of Connection.");
        } catch (IOException ioException) {
            System.err.println("Error in File of Connection");
        }
    }

    protected void appendToFile(Connection connection, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.format("%s;%s;%s;%s;%s;\n",
                    connection.getPort1().getEquipment().getCode(),
                    connection.getPort1().getPortType().getCode(),
                    connection.getPort2().getEquipment().getCode(),
                    connection.getPort2().getPortType().getCode(),
                    connection.getWire().getCode()));
        } catch (IOException e) {
            System.err.println("Error appending to file of Connection.");
            e.printStackTrace();
        }
    }

    public void insertAllIn(String directory, List<Connection> list) {
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
            writeToFile(list, file.getAbsolutePath());
    }

    public List<Connection> searchAllIn(String directory) {
        // Validación: el directorio no debe ser nulo ni vacío
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            return new ArrayList<>();
        }

        // Validación: Verificar si el directorio existe y es un directorio válido
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            return new ArrayList<>();
        }

        // Crear la ruta completa al archivo
        File file = new File(directory, fileName);

        // Validación: Verificar si el archivo existe y es un archivo regular
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            return new ArrayList<>();
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            return new ArrayList<>();
        }

        // Validación: Verificar si el archivo es legible
        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            return new ArrayList<>();
        }

        List<Connection> connectionList =  readFromFile(file.getAbsolutePath());
        // Intentar leer el archivo y manejar posibles excepciones
        // Leer el archivo y devolver la lista de conexiones
        return connectionList; // Retornar la lista de conexiones leídas
    }

}
