package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.modelo.Connection;

import java.util.List;

public interface ConnectionService {


    void insert(Connection connection);

    void update(Connection connection);

    void erase(Connection connection);

    List<Connection> searchAll();

}
