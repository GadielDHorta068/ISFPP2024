package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.dao.Secuencial.ConnectionSecuencialDAO;
import org.isfpp.modelo.Connection;

import java.util.List;

public class ConnectionServiceImpl implements ConnectionService{
    private GenericDAO ConnectionDAO;

    public ConnectionServiceImpl() {
        ConnectionDAO = new ConnectionSecuencialDAO();
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


