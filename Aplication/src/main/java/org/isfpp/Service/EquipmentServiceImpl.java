package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.modelo.Equipment;

import java.util.Hashtable;

public class EquipmentServiceImpl implements EquipmentService {
    private EquipmentDAO equipmentDAO;

    public EquipmentServiceImpl() {
        equipmentDAO = (EquipmentDAO) Factory.getInstancia("EQUIPMENT");
    }


    public void insert(Equipment equipment) {
        equipmentDAO.insert(equipment);
    }


    public void update(Equipment equipment) {
        equipmentDAO.update(equipment);

    }

    @Override
    public void erase(Equipment equipment) {
        equipmentDAO.erase(equipment);

    }

    @Override
    public void insertAllIn(String directory) {
        equipmentDAO.insertAllIn(directory);
    }

    @Override
    public Hashtable<String, Equipment> searchAll() {
        return equipmentDAO.searchAll();
    }

    @Override
    public Hashtable<String, Equipment> searchAllIn(String directory) {
        return equipmentDAO.searchAllIn(directory);
    }

    public void renameFilePath(String newFileName){

    }
}
