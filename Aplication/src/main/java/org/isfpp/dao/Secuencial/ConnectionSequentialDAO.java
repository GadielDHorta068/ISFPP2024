package org.isfpp.dao.Secuencial;

import org.isfpp.controller.Coordinator;
import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.WireType;

import java.io.*;
import java.util.*;

public class ConnectionSequentialDAO implements ConnectionDAO {
    Coordinator coordinator = new Coordinator();
    private List<Connection> list;
    private String fileName;
    private boolean update;
    private Hashtable<String, Equipment> equipments;
    private Hashtable<String, PortType> portTypes;
    private Hashtable<String, WireType> wireTypes;

    public ConnectionSequentialDAO() {
        equipments = readEquipments();
        portTypes = readPortTypes();
        wireTypes = readWireTypes();
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.connection");
        update = true;
    }

    private List<Connection> readFromFile(String fileName) {
        List<Connection> map = new ArrayList<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            while (inFile.hasNext()) {
                Connection connection = new Connection();
                connection.setPort1(equipments.get(inFile.next()).checkPort(portTypes.get(inFile.next())));
                connection.setPort2(equipments.get(inFile.next()).checkPort(portTypes.get(inFile.next())));
                connection.setWire(wireTypes.get(inFile.next()));
                map.add(connection);
            }
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error in file record structure");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error reading from file.");
            illegalStateException.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inFile != null)
                inFile.close();
        }

        try{
            File file = new File("equipos1.txt");
            file.setReadable(true);
            file.setWritable(true);
            if (file.isFile())
                System.out.println("archivo creado: "+file.getPath());
        } catch (Exception e) {
            System.err.println("Error opening file.");
            throw new RuntimeException("dada");
        }
        return map;
    }

    private void writeToFile(List<Connection> connectionList, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (Connection connection: connectionList) {
                writer.write(String.format("%s;%s;%s;%s;%s;\n", connection.getPort1().getEquipment().getCode(), connection.getPort1().getPortType().getCode(),
                        connection.getPort2().getEquipment().getCode(), connection.getPort2().getPortType().getCode(),
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
    private void appendToFile(Connection connection, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(String.format("%s;%s;%s;%s;%s;\n", connection.getPort1().getEquipment().getCode(), connection.getPort1().getPortType().getCode(),
                        connection.getPort2().getEquipment().getCode(), connection.getPort2().getPortType().getCode(),
                        connection.getWire().getCode()));

                writer.newLine();  // Escribir salto de línea después de cada línea

        }catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error creating file Connection.");
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file of Connection.");
        } catch (IOException ioException) {
            System.err.println("Error in File of Connection");
        }
    }
    @Override
    public void insert(Connection connection) {
        list.add(connection);
        appendToFile(connection,fileName);
        update = true;
    }

    @Override
    public void update(Connection connection) {
        int pos = list.indexOf(connection);
        list.set(pos, connection);
        writeToFile(list, fileName);
        update = true;
    }

    @Override
    public void erase(Connection connection) {
        list.remove(connection);
        writeToFile(list, fileName);
        update = true;
    }

    @Override
    public List<Connection> searchAll() {
        if (update) {
            list = readFromFile(fileName);
            update = false;
        }
        return list;
    }

    private Hashtable<String, Equipment> readEquipments() {
        EquipmentDAO EquipmentDAO = new EquipmentSequentialDAO();
        return EquipmentDAO.searchAll();
    }

    private Hashtable<String, PortType> readPortTypes() {
        PortTypeDAO portTypeDAO = new PortTypeSequentialDAO();
        return portTypeDAO.searchAll();
    }
    private Hashtable<String, WireType> readWireTypes(){
        WireTypeDAO wireTypeDAO = new WireTypeSequentialDAO();
        return wireTypeDAO.searchAll();
    }
}
