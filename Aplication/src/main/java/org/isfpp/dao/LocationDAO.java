package org.isfpp.dao;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface LocationDAO {
    void insert(Location location);  // Inserta un nuevo Location

    void update(Location location);  // Actualiza un Location existente

    void erase(Location location);             // Elimina un Location por su ID

    void insertAllIn(String directory);

    Hashtable<String,Location> searchAll();        // Lee todos los Locations

    Hashtable<String, Location> searchAllIn(String directory);

}