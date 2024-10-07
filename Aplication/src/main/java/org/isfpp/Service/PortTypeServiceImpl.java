package org.isfpp.Service;

import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.Secuencial.PortTypeSequentialDAO;
import org.isfpp.modelo.PortType;

import java.util.List;

public class PortTypeServiceImpl implements PortTypeService{
    private PortTypeDAO portTypeDAO;
    public PortTypeServiceImpl() {
        portTypeDAO = new PortTypeSequentialDAO();
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
    public List<PortType> searchAll() {
        return portTypeDAO.searchAll();
    }
}
