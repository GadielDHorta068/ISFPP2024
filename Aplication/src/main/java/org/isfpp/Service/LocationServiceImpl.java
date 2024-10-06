package org.isfpp.Service;

import org.isfpp.dao.GenericDAO;
import org.isfpp.modelo.Location;

import java.util.List;

public class LocationServiceImpl implements LocationService{
    private final GenericDAO LocationDAO;

    public LocationServiceImpl() {
        LocationDAO = new LocationSecuencialDAO();
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
    public List<Location> searchAll() {
        return LocationDAO.searchAll();
    }
}


