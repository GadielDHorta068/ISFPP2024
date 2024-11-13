package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.abstractDao.AbstractEquipmentDAO;
import org.isfpp.modelo.*;

import java.util.*;

public class EquipmentSequentialDAO extends AbstractEquipmentDAO implements EquipmentDAO {
    private static boolean update  = true;
    private static Hashtable<String, Equipment> map;

    public EquipmentSequentialDAO() {
        super();
    }

    @Override
    public void insert(Equipment equipment) {
        map.put(equipment.getCode(),equipment);
        appendToFile(equipment, fileName); // Descomenta esta línea si deseas guardar automáticamente al insertar
        update = true;
    }

    @Override
    public void update(Equipment equipment) {
        map.replace(equipment.getCode(), equipment);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void erase(Equipment equipment) {
        map.remove(equipment.getCode());
        writeToFile(map, fileName);

        update = true;
    }

    @Override
    public Hashtable<String, Equipment> searchAll() {
        if (update) {
            equipmentTypes = readEquipmentTypes();
            portTypes = readPortTypes();
            locations = readLocations();

            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }

    @Override
    public void insertAllIn(String directory) {
        super.insertAllIn(directory,map);
    }

    @Override
    public Hashtable<String, Equipment> searchAllIn(String directory) {
            for (Equipment equipment: super.searchAllIn(directory).values())
                if (!map.contains(equipment))
                    map.put(equipment.getCode(), equipment);
    return map;
    }
}
