package org.isfpp.data;

import org.isfpp.dao.*;
import org.isfpp.dao.Secuencial.*;
import org.isfpp.dao.posgresql.*;
import org.isfpp.modelo.*;

public class FileTextToBD {
    public static void FileTextToBD(){
        PortTypeDAO portTypeSequentialDAO = new PortTypeSequentialDAO();
        PortTypeDAO portTypePosgresqlDAO = new PortTypePosgresqlDAO();
        for (PortType portType: portTypeSequentialDAO.searchAll().values())
            portTypePosgresqlDAO.insert(portType);

        WireTypeDAO wireTypeSequentialDAO= new WireTypeSequentialDAO();
        WireTypeDAO wireTypePosgresqlDAO= new WireTypePosgresqlDAO();
        for (WireType wireType: wireTypeSequentialDAO.searchAll().values())
            wireTypePosgresqlDAO.insert(wireType);

        EquipmentTypeDAO equipmentTypeSequentialDAO= new EquipmentTypeSequentialDAO();
        EquipmentTypeDAO equipmentTypePosgresqlDAO= new EquipmentTypePosgresqlDAO();
        for (EquipmentType equipmentType: equipmentTypeSequentialDAO.searchAll().values())
            equipmentTypePosgresqlDAO.insert(equipmentType);

        LocationDAO locationSequentialDAO = new LocationSequentialDAO();
        LocationDAO locationPosgresqlDAO= new LocationPosgresqlDAO();
        for (Location location: locationSequentialDAO.searchAll().values())
            locationPosgresqlDAO.insert(location);

        EquipmentDAO equipmentSequentialDAO = new EquipmentSequentialDAO();
        EquipmentDAO equipmentPosgresqlDAO= new EquipmentPosgresqlDAO();
        for (Equipment equipment: equipmentSequentialDAO.searchAll().values())
            equipmentPosgresqlDAO.insert(equipment);

        ConnectionDAO connectionSequentialDAO = new ConnectionSequentialDAO();
        ConnectionDAO connectionPosgresqlDAO= new ConnectionPosgresqlDAO();
        for (Connection connection: connectionSequentialDAO.searchAll())
            connectionPosgresqlDAO.insert(connection);
    }

}
