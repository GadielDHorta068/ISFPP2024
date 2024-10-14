package org.isfpp.dao;
import org.isfpp.modelo.EquipmentType;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface EquipmentTypeDAO {
    void insert(EquipmentType equipmentType);  // Inserta un nuevo EquipmentType

    void update(EquipmentType equipmentType);  // Actualiza un EquipmentType existente

    void erase(EquipmentType equipmentType);                       // Elimina un EquipmentType por su ID

    Hashtable<String,EquipmentType> searchAll();             // Lee todos los EquipmentTypes
}