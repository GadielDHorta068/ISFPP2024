package org.isfpp.dao;
import org.isfpp.modelo.PortType;
import java.util.List;

public interface PortTypeDAO {
    void insert(PortType portType);

    void update(PortType portType);

    void erase(PortType portType);

    List<PortType> searchAll();

}
