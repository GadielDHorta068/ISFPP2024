package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.WireType;

import java.util.Hashtable;


public class WireTypeServiceImpl implements WireTypeService {
    private WireTypeDAO wireTypeDAO;

    public WireTypeServiceImpl() {
        wireTypeDAO = (WireTypeDAO) Factory.getInstancia("WIRETYPE");
    }


    public void insert(WireType wireType) {
        wireTypeDAO.insert(wireType);
    }


    public void update(WireType wireType) {
        wireTypeDAO.update(wireType);

    }

    @Override
    public void erase(WireType wireType) {
        wireTypeDAO.erase(wireType);

    }

    @Override
    public void insertAllIn(String directory) {
        wireTypeDAO.insertAllIn(directory);
    }

    @Override
    public Hashtable<String,WireType> searchAll() {
        return wireTypeDAO.searchAll();
    }

    @Override
    public Hashtable<String, WireType> searchAllIn(String directory) {
        return wireTypeDAO.searchAllIn(directory);
    }
}



