package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.dao.Secuencial.WireTypeSequentialDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.WireType;

import java.util.Hashtable;
import java.util.List;


public class WireTypeServiceImpl implements WireTypeService {
    private WireTypeDAO WireTypeDAO;

    public WireTypeServiceImpl() {
        WireTypeDAO = new WireTypeSequentialDAO();
    }


    public void insert(WireType wireType) {
        WireTypeDAO.insert(wireType);
    }


    public void update(WireType wireType) {
        WireTypeDAO.update(wireType);

    }

    @Override
    public void erase(WireType wireType) {
        WireTypeDAO.erase(wireType);

    }

    @Override
    public Hashtable<String,WireType> searchAll() {
        return WireTypeDAO.searchAll();
    }
}



