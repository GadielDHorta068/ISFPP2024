package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.dao.Secuencial.EquipmentSecuencialDAO;
import org.isfpp.modelo.Equipment;

import java.util.List;

public class EquipmentServiceImpl implements EquipmentService {
    private GenericDAO equipmentDAO;

    public EquipmentServiceImpl() {
        equipmentDAO = new EquipmentSecuencialDAO();
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
    public List<Equipment> searchAll() {
        return equipmentDAO.searchAll();
    }
}
