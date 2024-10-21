package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.ConnectionDAO;
import org.isfpp.modelo.Connection;

import java.util.List;

public class ConnectionServiceImpl implements ConnectionService{
    private ConnectionDAO connectionDAO;
    public ConnectionServiceImpl() {
        connectionDAO = (ConnectionDAO) Factory.getInstancia("CONNECTION");
    }


    public void insert(Connection connection) {
        connectionDAO.insert(connection);
    }


    public void update(Connection connection) {
        connectionDAO.update(connection);

    }

    @Override
    public void erase(Connection connection) {
        connectionDAO.erase(connection);

    }

    @Override
    public void insertAllIn(String directory) {
        connectionDAO.searchAllIn(directory);
    }

    @Override
    public List<Connection> searchAll() {
        return connectionDAO.searchAll();
    }

    @Override
    public List<Connection> searchAllIn(String directory) {
        return connectionDAO.searchAllIn(directory);
    }

}


