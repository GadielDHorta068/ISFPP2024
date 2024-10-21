package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.PortType;

import java.util.Hashtable;

public class PortTypeServiceImpl implements PortTypeService{
    private PortTypeDAO portTypeDAO;
    public PortTypeServiceImpl() {
        portTypeDAO = (PortTypeDAO) Factory.getInstancia("PORTTYPE");
    }

    @Override
    public void insert(PortType portType) {
        portTypeDAO.insert(portType);
    }

    @Override
    public void update(PortType portType) {
        portTypeDAO.update(portType);
    }

    @Override
    public void erase(PortType portType) {
        portTypeDAO.erase(portType);
    }

    @Override
    public void insertAllIn(String directory) {
        portTypeDAO.insertAllIn(directory);
    }

    @Override
    public Hashtable<String, PortType> searchAll() {
        return portTypeDAO.searchAll();
    }

    @Override
    public Hashtable<String, PortType> searchAllIn(String directory) {
        return searchAllIn(directory);
    }

}
