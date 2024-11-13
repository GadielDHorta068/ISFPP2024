package org.isfpp.dao.Secuencial;

import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.abstractDao.AbstractLocationDAO;
import org.isfpp.modelo.Location;

import java.util.*;

public class LocationSequentialDAO extends AbstractLocationDAO implements LocationDAO {
    private static Hashtable<String, Location> map;
    private static boolean update  = true;

    public LocationSequentialDAO() {
        super();
    }

    @Override
    public void insert(Location location) {
        map.put(location.getCode(), location);
        appendToFile(location, fileName);
        update = true;
    }

    @Override
    public void update(Location location) {
        map.replace(location.getCode(), location);
        update = true;
    }

    @Override
    public void erase(Location location) {
        map.remove(location.getCode());
        writeToFile(map, fileName);
        update = true;
    }

    @Override
    public Hashtable<String, Location> searchAll() {
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
    public Hashtable<String, Location> searchAllIn(String directory) {
        for(Location location: super.searchAllIn(directory).values())
            if (!map.contains(location))
                map.put(location.getCode(),location);
        return map;
    }

}
