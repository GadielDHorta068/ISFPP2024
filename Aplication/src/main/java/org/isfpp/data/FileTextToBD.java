package org.isfpp.data;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.*;
import org.isfpp.dao.Secuencial.*;
import org.isfpp.dao.posgresql.*;
import org.isfpp.modelo.*;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FileTextToBD {

    public static void fileTextToBD(){
        try {
              PortTypeDAO portTypeSequentialDAO = new PortTypeSequentialDAO();
              PortTypeDAO portTypePosgresqlDAO = new PortTypePosgresqlDAO();
              for (PortType portType : portTypeSequentialDAO.searchAll().values())
                  portTypePosgresqlDAO.insert(portType);

              WireTypeDAO wireTypeSequentialDAO = new WireTypeSequentialDAO();
              WireTypeDAO wireTypePosgresqlDAO = new WireTypePosgresqlDAO();
              for (WireType wireType : wireTypeSequentialDAO.searchAll().values())
                  wireTypePosgresqlDAO.insert(wireType);

              EquipmentTypeDAO equipmentTypeSequentialDAO = new EquipmentTypeSequentialDAO();
              EquipmentTypeDAO equipmentTypePosgresqlDAO = new EquipmentTypePosgresqlDAO();
              for (EquipmentType equipmentType : equipmentTypeSequentialDAO.searchAll().values())
                  equipmentTypePosgresqlDAO.insert(equipmentType);

              LocationDAO locationSequentialDAO = new LocationSequentialDAO();
              LocationDAO locationPosgresqlDAO = new LocationPosgresqlDAO();
              for (Location location : locationSequentialDAO.searchAll().values())
                  locationPosgresqlDAO.insert(location);

              EquipmentDAO equipmentSequentialDAO = new EquipmentSequentialDAO();
              EquipmentDAO equipmentPosgresqlDAO = new EquipmentPosgresqlDAO();
              for (Equipment equipment : equipmentSequentialDAO.searchAll().values())
                  equipmentPosgresqlDAO.insert(equipment);

              ConnectionDAO connectionSequentialDAO = new ConnectionSequentialDAO();
              ConnectionDAO connectionPosgresqlDAO = new ConnectionPosgresqlDAO();
              for (Connection connection : connectionSequentialDAO.searchAll())
                  connectionPosgresqlDAO.insert(connection);
          } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAllBD(){
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try{
            con = BDConnection.getConnection();
            String[] sqlStatements = {
                    "DELETE FROM poo2024.rcg_connection;",
                    "DELETE FROM poo2024.RCG_equipment_port;",
                    "DELETE FROM poo2024.RCG_equipment_ips;",
                    "DELETE FROM poo2024.RCG_equipment;",
                    "DELETE FROM poo2024.rcg_equipment_type;",
                    "DELETE FROM poo2024.RCG_Location;",
                    "DELETE FROM poo2024.RCG_port_Type;",
                    "DELETE FROM poo2024.RCG_Wire_type;"
            };

            // Ejecutar cada declaración por separado
            for (String sql : sqlStatements) {
                pstm = con.prepareStatement(sql);
                pstm.executeUpdate();
            }
            pstm.close();
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error a la hora de cerrar la consulta");
                throw new RuntimeException(e);
            }
        }
    }

    public static void FileTextToFileNoEquipmentAndConnection(){
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
    }

    public static void main(String[] args){
        //FileTextToBD.fileTextToBD();
        //FileTextToBD.deleteAllBD();
        EquipmentDAO equipmentSequentialDAO = new EquipmentSequentialDAO();
        EquipmentDAO equipmentPosgresqlDAO = new EquipmentPosgresqlDAO();
        for (Equipment equipment : equipmentSequentialDAO.searchAll().values()) {
            System.out.println(equipment);
            equipmentPosgresqlDAO.insert(equipment);
        }

    }
}
