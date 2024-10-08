package org.isfpp.Service;

import org.isfpp.modelo.Equipment;

import java.util.Hashtable;
import java.util.List;

public interface EquipmentService {

    void insert(Equipment equipment);

    void update(Equipment equipment);

    void erase(Equipment equipment);

    Hashtable<String,Equipment> searchAll();
}
