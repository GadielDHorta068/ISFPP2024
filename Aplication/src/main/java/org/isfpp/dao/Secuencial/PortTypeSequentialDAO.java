package org.isfpp.dao.Secuencial;


import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.abstractDao.AbstractPortTypeDAO;
import org.isfpp.modelo.PortType;

import java.util.*;

public class PortTypeSequentialDAO extends AbstractPortTypeDAO implements PortTypeDAO {
    private static Hashtable<String, PortType> map;
    private static boolean update  = true;

    public PortTypeSequentialDAO() {
        super();
    }

    @Override
    public void insert(PortType type) {
        map.put(type.getCode(), type);
        appendToFile(type, fileName);
        update = true;
    }

    @Override
    public void update(PortType portType) {
        map.replace(portType.getCode(), portType);
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public void erase(PortType portType) {
        map.remove(portType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String, PortType> searchAll() {
        if (update) {
            System.out.println("SAS EN TODA LA BOCA");
            map = readFromFile(fileName);
            update = false;
        }
        return map;
    }

    @Override
    public void insertAllIn(String directory) {
        super.insertAllIn(directory, map);
    }

    @Override
    public Hashtable<String, PortType> searchAllIn(String directory) {
        for(PortType portType: super.searchAllIn(directory).values())
            if (!map.contains(portType))
                map.put(portType.getCode(),portType);
        return map;
    }

}
