package org.isfpp.dao.Secuencial;

import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.WireType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ConnectionSequentialDAO implements ConnectionDAO {
    private List<Connection> list;
    private String name;
    private boolean update;
    private Hashtable<String, Equipment> equipments;
    private HashMap<String, PortType> portTypes;
    private Hashtable<String, WireType> wireTypes;

    public ConnectionSequentialDAO() {
        ResourceBundle rb = ResourceBundle.getBundle("secuencial");
        name = rb.getString("PortType");
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

    private void writeToFile(List<Connection> connectionList, String fileName) {
        Formatter outFile = null;
        try {
            outFile = new Formatter(fileName);
            for (Connection connection: connectionList) {
                outFile.format("%s;%s;%s;%s;%s;\n", connection.getPort1().getEquipment().getCode(), connection.getPort1().getPortType().getCode(),
                        connection.getPort2().getEquipment().getCode(), connection.getPort2().getPortType().getCode(),
                        connection.getWire().getCode());
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
    public void insert(Connection connection) {
        list.add(connection);
        writeToFile(list,name);
        update = true;
    }

    @Override
    public void update(Connection connection) {
        int pos = list.indexOf(connection);
        list.set(pos, connection);
        writeToFile(list, name);
        update = true;
    }

    @Override
    public void erase(Connection connection) {
        list.remove(connection);
        writeToFile(list, name);
        update = true;
    }

    @Override
    public List<Connection> searchAll() {
        if (update) {
            list = readFromFile(name);
            update = false;
        }
        return list;
    }

    private Hashtable<String, Equipment> readEquipments() {
        Hashtable<String,Equipment> equipments = new Hashtable<>();
        EquipmentDAO EquipmentDAO = new EquipmentSequentialDAO();
        List<Equipment> portTypeList = EquipmentDAO.searchAll();
        for (Equipment equipment: portTypeList)
            equipments.put(equipment.getCode(),equipment);
        return equipments;
    }

    private Hashtable<String, PortType> readPortTypes() {
        Hashtable<String,PortType> portTypes = new Hashtable<>();
        PortTypeDAO portTypeDAO = new PortTypeSequentialDAO();
        List<PortType> portTypeList = portTypeDAO.searchAll();
        for (PortType portType: portTypeList)
            portTypes.put(portType.getCode(),portType);
        return portTypes;
    }
    private Hashtable<String, WireType> readWireTypes(){
        Hashtable<String,WireType> wireTypes = new Hashtable<>();
        WireTypeDAO portTypeDAO = new WireTypeSequentialDAO();
        List<WireType> wireTypeList = portTypeDAO.searchAll();
        for (WireType wireType: wireTypeList)
            wireTypes.put(wireType.getCode(),wireType);
        return wireTypes;
    }
}
