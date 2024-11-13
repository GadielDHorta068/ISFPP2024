package org.isfpp.dao.Secuencial;

import org.isfpp.dao.abstractDao.AbstractWireTypeDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.WireType;

import java.util.*;

public class WireTypeSequentialDAO extends AbstractWireTypeDAO implements WireTypeDAO {
    private static Hashtable<String, WireType> map;
    private static boolean update  = true;

    public WireTypeSequentialDAO() {
        super();
    }

    @Override
    public void insert(WireType wireType) {
        map.put(wireType.getCode(),wireType);
        appendToFile(wireType,fileName);
        update = true;
    }

    @Override
    public void update(WireType wireType) {
        map.replace(wireType.getCode(),wireType);
        update = true;
    }

    @Override
    public void erase(WireType wireType) {
        map.remove(wireType.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String,WireType> searchAll() {
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
    public Hashtable<String, WireType> searchAllIn(String directory) {
        for(WireType wireType: super.searchAllIn(directory).values())
            if (!map.contains(wireType))
                map.put(wireType.getCode(),wireType);
        return map;
    }

}
