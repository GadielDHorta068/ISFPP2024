package org.isfpp.Service;

import org.isfpp.modelo.WireType;

import java.util.Hashtable;

public interface WireTypeService {
    void insert(WireType wireType);

    void update(WireType wireType);

    void erase(WireType wireType);

    /**
     * Inserta todos los tipos de cable existentes dentro del directorio dado
     * @param directory directorio donde se guardaran todos los tipos de cable
     */
    void insertAllIn(String directory);

    Hashtable<String,WireType> searchAll();

    /**
     * Busca todos los tipos de cable que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran todos los tipos de cable
     * @return Lista con todos los tipos de cable encontrados.
     */
    Hashtable<String,WireType> searchAllIn(String directory);

}
