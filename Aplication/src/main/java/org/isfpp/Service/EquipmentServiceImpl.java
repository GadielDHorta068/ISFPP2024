package org.isfpp.Service;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.Secuencial.EquipmentSequentialDAO;
import org.isfpp.modelo.Equipment;

import java.util.List;

public class EquipmentServiceImpl implements EquipmentService {
    private EquipmentDAO equipmentDAO;

    public EquipmentServiceImpl() {
        equipmentDAO = new EquipmentSequentialDAO();
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
