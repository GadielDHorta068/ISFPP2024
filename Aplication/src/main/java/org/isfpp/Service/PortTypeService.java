package org.isfpp.Service;

import org.isfpp.modelo.PortType;
import java.util.Hashtable;

public interface PortTypeService {

    void insert(PortType portType);

    void update(PortType portType);

    void erase(PortType portType);

    /**
     * Inserta todos los tipos de puerto existentes dentro del directorio dado.
     * @param directory directorio donde se guardaran todos los tipos de puerto.
     */
    void insertAllIn(String directory);

    Hashtable<String,PortType> searchAll();

    /**
     * Busca todas las ubicaciones que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran todos los tipos de puerto
     * @return Lista con todos los tipos de puerto encontrados.
     */
    Hashtable<String,PortType> searchAllIn(String directory);
}

