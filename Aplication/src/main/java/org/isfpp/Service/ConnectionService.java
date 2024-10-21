package org.isfpp.Service;

import org.isfpp.modelo.Connection;

import java.util.List;

public interface ConnectionService {


    void insert(Connection connection);

    void update(Connection connection);

    void erase(Connection connection);

    /**
     * Inserta todas las conexiones existentes dentro del directorio dado
     * @param directory directorio donde se guardaran todas las conexiones
     */
    void insertAllIn(String directory);

    List<Connection> searchAll();

    /**
     * Busca todas las conexiones que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran las conexiones
     * @return Lista con todas las conexiones encontradas
     */
    List<Connection> searchAllIn(String directory);
}
