package org.isfpp.dao;
import org.isfpp.modelo.Connection;

import java.util.ArrayList;
import java.util.List;

public interface ConnectionDAO {
    void insert(Connection connection);  // Inserta una nueva Connection

    void update(Connection connection);  // Actualiza una Connection existente

    void erase(Connection connection);                 // Elimina una Connection por su ID

    void insertAllIn(String directory);

    List<Connection> searchAll();   // Lee todas las Connections

    List<Connection> searchAllIn(String directory);
}