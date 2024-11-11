package org.isfpp.Service;

import org.isfpp.connection.Factory;
import org.isfpp.dao.LocationDAO;
import org.isfpp.modelo.Location;

import java.util.Hashtable;

public class LocationServiceImpl implements LocationService{
    private LocationDAO locationDAO;

    public LocationServiceImpl() {
        locationDAO = (LocationDAO) Factory.getInstancia("LOCATION");
    }


    public void insert(Location location) {
        locationDAO.insert(location);
    }


    public void update(Location location) {
        locationDAO.update(location);

    }

    @Override
    public void erase(Location location) {
        locationDAO.erase(location);

    }

    @Override
    public void insertAllIn(String directory) {
        locationDAO.searchAllIn(directory);
    }

    @Override
    public Hashtable<String,Location> searchAll() {
        return locationDAO.searchAll();
    }

    @Override
    public Hashtable<String, Location> searchAllIn(String directory) {
        return locationDAO.searchAllIn(directory);
    }


}


