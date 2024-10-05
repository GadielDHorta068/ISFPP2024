package org.isfpp.Service;

import org.isfpp.modelo.Equipment;

import java.util.List;

public interface EquipmentService {

    void insert(Equipment equipment);

    void update(Equipment equipment);

    void erase(Equipment equipment);

    List<Equipment> searchAll();
}
