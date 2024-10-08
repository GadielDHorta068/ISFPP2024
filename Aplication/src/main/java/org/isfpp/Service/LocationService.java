package org.isfpp.Service;

import org.isfpp.modelo.Location;

import java.util.Hashtable;
import java.util.List;

public interface LocationService {

    void insert(Location location);

    void update(Location location);

    void erase(Location location);

    Hashtable<String,Location> searchAll();
}
