package org.isfpp.Service;

import org.isfpp.modelo.EquipmentType;
import java.util.Hashtable;

public interface EquipmentTypeService{
    void insert(EquipmentType equipmentType);

    void update(EquipmentType equipmentType);

    void erase(EquipmentType equipmentType);

    /**
     * Inserta todos los tipos de equipos existentes dentro del directorio dado
     * @param directory directorio donde se guardaran todos los tipos de equipos
     */
    void insertAllIn(String directory);

    Hashtable<String,EquipmentType> searchAll();

    /**
     * Busca todos los tipos de equipos que se encuentren dentro del directorio dado
     * @param directory directorio que contiene un archivo .txt donde se guardaran los tipos de equipos
     * @return Lista con todas los tipos de equipos encontrados
     */
    Hashtable<String,EquipmentType> searchAllIn(String directory);
}
