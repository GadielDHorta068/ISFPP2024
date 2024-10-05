package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.modelo.EquipmentType;

import java.util.List;

public class EquipmentTypeServiceImpl implements EquipmentTypeService {
    private GenericDAO EquipmentTypeDAO;

    public EquipmentTypeServiceImpl() {
        EquipmentTypeDAO = new EquipmentTypeSecuencialDAO();
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
