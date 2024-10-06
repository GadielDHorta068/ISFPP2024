package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.modelo.WireType;

import java.util.List;


public class WireTypeServiceImpl implements WireTypeService {
    private GenericDAO WireTypeDAO;

    public WireTypeServiceImpl() {
        WireTypeDAO = new WireTypeSecuencialDAO();
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
    public List<WireType> searchAll() {
        return WireTypeDAO.searchAll();
    }
}



