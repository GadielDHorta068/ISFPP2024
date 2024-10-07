package org.isfpp.dao;
import org.isfpp.modelo.Location;
import java.util.List;

public interface LocationDAO {
    void insert(Location location);  // Inserta un nuevo Location

    void update(Location location);  // Actualiza un Location existente

    void erase(Location location);             // Elimina un Location por su ID

    List<Location> searchAll();        // Lee todos los Locations
}