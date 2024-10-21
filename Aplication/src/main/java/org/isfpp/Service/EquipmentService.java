package org.isfpp.Service;

import org.isfpp.modelo.Equipment;
import java.util.Hashtable;

public interface EquipmentService {

    void insert(Equipment equipment);

    void update(Equipment equipment);

    void erase(Equipment equipment);

    /**
     * Inserta todos los equipos existentes dentro del directorio dado
     * @param directory directorio donde se guardaran todos los equipos
     */
    void insertAllIn(String directory);

    Hashtable<String,Equipment> searchAll();

    /**
     * Busca todos los equipos que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran los equipos
     * @return Lista con todos los equipo encontrados
     */
    Hashtable<String,Equipment> searchAllIn(String directory);

}
