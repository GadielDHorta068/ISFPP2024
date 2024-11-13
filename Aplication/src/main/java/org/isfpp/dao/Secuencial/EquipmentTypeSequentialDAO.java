package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.abstractDao.AbstractEquimentTypeDAO;
import org.isfpp.modelo.EquipmentType;

import java.util.*;

public class EquipmentTypeSequentialDAO extends AbstractEquimentTypeDAO implements EquipmentTypeDAO {
    private static Hashtable<String, EquipmentType> map;
    private static boolean update  = true;

    public EquipmentTypeSequentialDAO() {
        super();
    }

    @Override
    public void insert(EquipmentType equipmentType) {
        map.put(equipmentType.getCode(), equipmentType);
        appendToFile(equipmentType, fileName);
        update = true;
    }

    @Override
    public void update(EquipmentType equipmentType) {
        map.replace(equipmentType.getCode(), equipmentType);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void erase(EquipmentType equipmentType) {
        map.remove(equipmentType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String, EquipmentType> searchAll() {
        if (update) {
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }

    @Override
    public void insertAllIn(String directory) {
        super.insertAllIn(directory, map);
    }

    @Override
    public Hashtable<String, EquipmentType> searchAllIn(String directory) {
        for(EquipmentType equimentType: super.searchAllIn(directory).values())
            if (!map.contains(equimentType))
                map.put(equimentType.getCode(),equimentType);
        return map;
    }
}
