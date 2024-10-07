package org.isfpp.Service;

import org.isfpp.modelo.EquipmentType;

import java.util.List;

public interface EquipmentTypeService{
    void insert(EquipmentType equipmentType);

    void update(EquipmentType equipmentType);

    void erase(EquipmentType equipmentType);

    List<EquipmentType> searchAll();
}
