package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.*;
import org.isfpp.modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EquipmentPosgresqlDAO implements EquipmentDAO {
    private PortTypeDAO portTypeDAO;
    private EquipmentTypeDAO equipmentTypeDAO;
    private LocationDAO locationDAO;

    private Hashtable<String, PortType> portTypeTable;
    private Hashtable<String,EquipmentType> equipmentTypeTable;
    private Hashtable<String, Location> locationTable;

    public EquipmentPosgresqlDAO(){
        portTypeDAO = new PortTypePosgresqlDAO();
        portTypeTable = loadPortTypes();
        equipmentTypeDAO = new EquipmentTypePosgresqlDAO();
        equipmentTypeTable = loadEquipmentTypes();
        locationDAO = new LocationPosgresqlDAO();
        locationTable = loadLocations();
    }

    private Hashtable<String, Location> loadLocations() {
        return locationDAO.searchAll();
    }

    private Hashtable<String, EquipmentType> loadEquipmentTypes() {
        return equipmentTypeDAO.searchAll();
    }

    private Hashtable<String, PortType> loadPortTypes() {
        return portTypeDAO.searchAll();
    }

    private String portFormatOf(Equipment equipment) {
        String ports = "";
        HashMap<PortType, Integer> portMap = equipment.getAllPortsTypes();
        for (PortType portType: portMap.keySet())
            ports.formatted("%s,%s", portType.getCode(), portMap.get(portType));
        ports += ";";
        return ports;
    }

    private String ipFormatOf(Equipment equipment) {
        String ips = "";
        for (int i = 0; i < equipment.getIpAdresses().size(); i++)
            ips.formatted("%s%s", equipment.getIpAdresses().get(i),
                    (i < equipment.getIpAdresses().size() - 1 ? "," : ";"));
        return ips;
    }

    @Override
    public void insert(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql = "INSERT INTO poo2024.RCG_equipment (" +
                    "   code," +
                    "   description," +
                    "   marca," +
                    "   modelo," +
                    "   code_equipment_type," +
                    "   code_location," +
                    "   status" +
                    ")";
            sql+="VALUES(?, ?, ?, ?, ?, ?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getDescription());
            pstm.setString(3, equipment.getMake());
            pstm.setString(4, equipment.getModel());
            pstm.setString(5, equipment.getEquipmentType().getCode());
            pstm.setString(6, equipment.getLocation().getCode());
            pstm.setBoolean(7, equipment.isStatus());
            pstm.executeUpdate();
            pstm.close();

            //se insertan los puertos a la tabla de puertos de equipo
            HashMap<PortType, Integer> portsTypes = equipment.getAllPortsTypes();
            sql = "INSERT INTO poo2024.RCG_equipment_port(" +
                    "cantidad," +
                    "code_port_type," +
                    "code_equipment" +
                    ") " +
                    "VALUES(?, ?, ?)";
            for (PortType portType: portsTypes.keySet()){
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, portsTypes.get(portType));
                pstm.setString(2, portType.getCode());
                pstm.setString(3, equipment.getCode());
                pstm.executeUpdate();
                pstm.close();
            }

            //se insertan las ips que tiene el equipo dentro de la tabla de ips de equipo
            sql = "INSERT INTO poo2024.RCG_equipment_ips(" +
                    "code_equipment," +
                    "ip" +
                    ") " +
                    "VALUES(?, ?)";
            for (String ip: equipment.getIpAdresses()){
                pstm = con.prepareStatement(sql);
                pstm.setString(1, equipment.getCode());
                pstm.setString(2, ip);
                pstm.executeUpdate();
                pstm.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(rs != null)
                    rs.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                System.err.println("Error a la hora de cerrar la consulta");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.RCG_equipment ";
            sql += "SET code = ?," +
                    " description = ?," +
                    " marca = ?," +
                    " modelo = ?," +
                    " code_equipment_type = ?," +
                    " code_location = ?," +
                    " status = ? ";
            sql += "WHERE code = ?";
            pstm = con.prepareStatement(sql);

            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getDescription());
            pstm.setString(3, equipment.getMake());
            pstm.setString(4, equipment.getModel());
            pstm.setString(5, equipment.getEquipmentType().getCode());
            pstm.setString(6, equipment.getLocation().getCode());
            pstm.setBoolean(7, equipment.isStatus());
            pstm.setString(8, equipment.getCode());
            pstm.executeUpdate();
            pstm.close();

            HashMap<PortType, Integer> portsTypes = equipment.getAllPortsTypes();
            /*borra todos los puertos y ips guardados, para volver a escribirlos, con las posibles
            modificaciones dentro de la base de datos*/
            sql = "DELETE FROM poo2024.RCG_equipment_port WHERE code_equipment = ?;" +
                    "DELETE FROM poo2024.RCG_equipment_ips WHERE code_equipment = ?;";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getCode());
            pstm.executeUpdate();
            pstm.close();

            sql = "SET cantidad = ?," +
                    "   code_port_type = ?," +
                    "   code_equipment = ? " +
                    "WHERE code_equipment = ?";
            for (PortType portType: portsTypes.keySet()){
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, portsTypes.get(portType));
                pstm.setString(2, portType.getCode());
                pstm.setString(3, equipment.getCode());
                pstm.setString(4, equipment.getCode());
                pstm.executeUpdate();
                pstm.close();
            }

            sql = "SET ip = ?," +
                    "   code_equipment = ?," +
                    "WHERE code_equipment = ?";
            for (String ip: equipment.getIpAdresses()){
                pstm = con.prepareStatement(sql);
                pstm.setString(1, ip);
                pstm.setString(2, equipment.getCode());
                pstm.setString(3, equipment.getCode());
                pstm.executeUpdate();
                pstm.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar comunicación con la base de datos");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void erase(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.RCG_equipment_ips WHERE code_equipment = ?" +
                    "DELETE FROM poo2024.RCG_equipment_port WHERE code_equipment = ?" +
                    "DELETE FROM poo2024.RCG_equipment WHERE code = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getCode());
            pstm.setString(3, equipment.getCode());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void insertAllIn(String directory) {

    }

    @Override
    public Hashtable<String, Equipment> searchAll() {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConnection.getConnection();
            String sql = "SELECT " +
                    "    e.code," +
                    "    e.description," +
                    "    e.marca," +
                    "    e.modelo," +
                    "    e.equipment_type" +
                    "    e.location," +
                    "    e.status";
                    sql += "FROM " +
                    "    poo2024.rcg_equipment e;";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            Hashtable<String, Equipment> ret = new Hashtable<>();
            equipmentTypeTable = loadEquipmentTypes();
            portTypeTable = loadPortTypes();
            locationTable = loadLocations();
            Equipment equipment;
            while (rs.next()) {
                String code = rs.getString("code");
                equipment = new Equipment();
                ret.put(code, equipment);
                equipment.setCode(code);
                equipment.setDescription(rs.getString("description"));
                equipment.setMake(rs.getString("marca"));
                equipment.setModel(rs.getString("modelo"));
                equipment.setEquipmentType(equipmentTypeTable.get(rs.getString("equipment_type")));
                equipment.setLocation(locationTable.get(rs.getString("location")));
                equipment.setStatus(rs.getBoolean("status"));
            }
            rs.close();
            pstm.close();

            sql = "SELECT " +
                    "   cantidad," +
                    "   code_portype," +
                    "   code_equipment " +
                    "FROM poo2024.rcg_port";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            String portTypeCode = "";
            int portCapacity = 0;
            while (rs.next()){
                equipment = ret.get(rs.getString("code_equipment"));
                portTypeCode = rs.getString("code_portype");
                portCapacity = rs.getInt("cantidad");
                for (int i = 0; i < portCapacity;i++)
                    equipment.addPort(portTypeTable.get(portTypeCode));
            }
            rs.close();
            pstm.close();

            String sqlIps = "SELECT " +
                    "  code_equipment," +
                    "    ip " +
                    "FROM poo2024.rcg_ips";
            pstm = con.prepareStatement(sqlIps);
            rs = pstm.executeQuery();
            while (rs.next()){
                ret.get(rs.getString("code_equipment")).addIp(rs.getString("ip"));
            }
            rs.close();
            pstm.close();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstm != null)
                    pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public Hashtable<String, Equipment> searchAllIn(String directory) {
        return null;
    }
}

