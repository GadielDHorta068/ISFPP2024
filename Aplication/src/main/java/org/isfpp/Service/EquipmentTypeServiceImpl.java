package org.isfpp.Service;

import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.modelo.EquipmentType;
import org.isfpp.dao.Secuencial.EquipmentTypeSequentialDAO;

import java.util.List;

public class EquipmentTypeServiceImpl implements EquipmentTypeService {
    private EquipmentTypeDAO EquipmentTypeDAO;

    public EquipmentTypeServiceImpl() {
        EquipmentTypeDAO = new EquipmentTypeSequentialDAO();
    }


    public void insert(EquipmentType equipmentType) {
        EquipmentTypeDAO.insert(equipmentType);
    }


    public void update(EquipmentType equipmentType) {
        EquipmentTypeDAO.update(equipmentType);

    }

    public void erase(EquipmentType equipmentType) {
        EquipmentTypeDAO.erase(equipmentType);

    }

    @Override
    public List<EquipmentType> searchAll() {
        return EquipmentTypeDAO.searchAll();
    }





}
