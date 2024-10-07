package org.isfpp.dao;
import org.isfpp.modelo.EquipmentType;
import java.util.List;

public interface EquipmentTypeDAO {
    void insert(EquipmentType equipmentType);  // Inserta un nuevo EquipmentType

    void update(EquipmentType equipmentType);  // Actualiza un EquipmentType existente

    void erase(EquipmentType equipmentType);                       // Elimina un EquipmentType por su ID

    List<EquipmentType> searchAll();             // Lee todos los EquipmentTypes
}