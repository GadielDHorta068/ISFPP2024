package org.isfpp.Service;

import org.isfpp.modelo.PortType;

import java.util.List;

public interface PortTypeService {

    void insert(PortType portType);

    void update(PortType portType);

    void erase(PortType portType);

    List<PortType> searchAll();
}

