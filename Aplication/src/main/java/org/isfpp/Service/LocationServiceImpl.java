package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.Secuencial.LocationSequentialDAO;
import org.isfpp.modelo.Location;

import java.util.Hashtable;
import java.util.List;

public class LocationServiceImpl implements LocationService{
    private LocationDAO LocationDAO;

    public LocationServiceImpl() {
        LocationDAO = new LocationSequentialDAO();
    }


    public void insert(Location location) {
        LocationDAO.insert(location);
    }


    public void update(Location location) {
        LocationDAO.update(location);

    }

    @Override
    public void erase(Location location) {
        LocationDAO.erase(location);

    }

    @Override
    public Hashtable<String,Location> searchAll() {
        return LocationDAO.searchAll();
    }
}


