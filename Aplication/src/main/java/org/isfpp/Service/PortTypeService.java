package org.isfpp.Service;

import org.isfpp.modelo.PortType;

import java.util.Hashtable;
import java.util.List;

public interface PortTypeService {

    void insert(PortType portType);

    void update(PortType portType);

    void erase(PortType portType);

    Hashtable<String,PortType> searchAll();
}

