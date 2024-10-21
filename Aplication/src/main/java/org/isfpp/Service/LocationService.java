package org.isfpp.Service;

import org.isfpp.modelo.Location;
import java.util.Hashtable;

public interface LocationService {

    void insert(Location location);

    void update(Location location);

    void erase(Location location);

    /**
     * Inserta todas las ubicaciones existentes dentro del directorio dado
     * @param directory directorio donde se guardaran todas las ubicaciones
     */
    void insertAllIn(String directory);

    Hashtable<String,Location> searchAll();

    /**
     * Busca todas las ubicaciones que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran todas las ubicaciones
     * @return Lista con todos todas las ubicaciones encontradas
     */
    Hashtable<String,Location> searchAllIn(String directory);

}
