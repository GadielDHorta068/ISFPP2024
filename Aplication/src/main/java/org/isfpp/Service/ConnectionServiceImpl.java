package org.isfpp.Service;

import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.Secuencial.ConnectionSequentialDAO;
import org.isfpp.modelo.Connection;

import java.util.List;

public class ConnectionServiceImpl implements ConnectionService{
    private ConnectionDAO ConnectionDAO;

    public ConnectionServiceImpl() {
        ConnectionDAO = new ConnectionSequentialDAO();
    }


    public void insert(Connection connection) {
        ConnectionDAO.insert(connection);
    }


    public void update(Connection connection) {
        ConnectionDAO.update(connection);

    }

    @Override
    public void erase(Connection connection) {
        ConnectionDAO.erase(connection);

    }

    @Override
    public List<Connection> searchAll() {
        return ConnectionDAO.searchAll();
    }
}


