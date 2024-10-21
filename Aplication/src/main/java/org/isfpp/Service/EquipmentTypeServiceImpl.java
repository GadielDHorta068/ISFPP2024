package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.modelo.EquipmentType;

import java.util.Hashtable;

public class EquipmentTypeServiceImpl implements EquipmentTypeService {
    private EquipmentTypeDAO equipmentTypeDAO;

    public EquipmentTypeServiceImpl() {
        equipmentTypeDAO = (EquipmentTypeDAO) Factory.getInstancia("EQUIPMENTTYPE");
    }


    public void insert(EquipmentType equipmentType) {
        equipmentTypeDAO.insert(equipmentType);
    }


    public void update(EquipmentType equipmentType) {
        equipmentTypeDAO.update(equipmentType);

    }

    public void erase(EquipmentType equipmentType) {
        equipmentTypeDAO.erase(equipmentType);

    }

    @Override
    public void insertAllIn(String directory) {
        equipmentTypeDAO.insertAllIn(directory);
    }

    @Override
    public Hashtable<String,EquipmentType> searchAll() {
        return equipmentTypeDAO.searchAll();
    }

    @Override
    public Hashtable<String, EquipmentType> searchAllIn(String directory) {
        return equipmentTypeDAO.searchAllIn(directory);
    }

}
