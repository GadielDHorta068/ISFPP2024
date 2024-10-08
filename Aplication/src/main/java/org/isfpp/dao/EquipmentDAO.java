package org.isfpp.dao;
import org.isfpp.modelo.Equipment;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface EquipmentDAO {
    void insert(Equipment equipment);  // Inserta un nuevo Equipment

    void update(Equipment equipment);  // Actualiza un Equipment existente

    void erase(Equipment equipment);   // Elimina un Equipment por su ID

    Hashtable<String,Equipment> searchAll();       // Lee todos los Equipments
}