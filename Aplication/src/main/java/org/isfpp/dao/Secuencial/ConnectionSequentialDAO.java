package org.isfpp.dao.Secuencial;

import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.abstractDao.AbstractConnectionDAO;
import org.isfpp.modelo.Connection;

import java.util.*;

public class ConnectionSequentialDAO extends AbstractConnectionDAO implements ConnectionDAO {
    private static List<Connection> list;
    private static  boolean update = true;

    public ConnectionSequentialDAO() {
        super();
    }

    @Override
    public void insert(Connection connection) {
        list.add(connection);
        appendToFile(connection, fileName);
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
            equipments = readEquipments();
            portTypes = readPortTypes();
            wireTypes = readWireTypes();

            list = readFromFile(fileName);
            update = false;
        }
        return list;
    }

    @Override
    public void insertAllIn(String directory) {
       super.insertAllIn(directory, list);
    }

    @Override
    public List<Connection> searchAllIn(String directory) {
        for (Connection connection: super.searchAllIn(directory))
            list.add(connection);
        return list;
    }
}
