package org.isfpp.Service;

import org.isfpp.modelo.WireType;

import java.util.Hashtable;
import java.util.List;

public interface WireTypeService {
    void insert(WireType wireType);

    void update(WireType wireType);

    void erase(WireType wireType);

    Hashtable<String,WireType> searchAll();

}
