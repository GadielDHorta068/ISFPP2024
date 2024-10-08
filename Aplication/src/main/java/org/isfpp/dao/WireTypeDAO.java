package org.isfpp.dao;
import org.isfpp.modelo.WireType;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface WireTypeDAO {
    void insert(WireType wireType);  // Inserta un nuevo WireType

    void update(WireType wireType);  // Actualiza un WireType existente

    void erase(WireType wireType);             // Elimina un WireType por su ID

    Hashtable<String,WireType> searchAll();        // Lee todos los WireTypes
}