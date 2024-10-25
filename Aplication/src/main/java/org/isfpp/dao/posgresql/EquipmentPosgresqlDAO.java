package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.*;
import org.isfpp.modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

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
            sql+="INSERT INTO public.equipment (code_equipment," +
                    " description_equipment," +
                    " make_equipment," +
                    " model_equipment," +
                    " equipment_type," +
                    " location," +
                    " ports," +
                    " ipAddresses, " +
                    " status)";
            sql+="VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getDescription());
            pstm.setString(3, equipment.getMake());
            pstm.setString(4, equipment.getModel());
            pstm.setString(5, equipment.getEquipmentType().getCode());
            pstm.setString(6, equipment.getLocation().getCode());
            pstm.setString(7, portFormatOf(equipment));
            pstm.setString(8, ipFormatOf(equipment));
            pstm.setBoolean(9, equipment.isStatus());

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
            String sql = "UPDATE public.equipment ";
            sql += "SET code_equipment = ?," +
                    " description_equipment = ?," +
                    " make_equipment = ?," +
                    " model_equipment = ?," +
                    " equipment_type = ?," +
                    " location = ?," +
                    " ports = ?," +
                    " ipAddresses = ?" +
                    " status = ?";
            sql += "WHERE code_equipment = ?";
            pstm = con.prepareStatement(sql);


            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getDescription());
            pstm.setString(3, equipment.getMake());
            pstm.setString(4, equipment.getModel());
            pstm.setString(5, equipment.getEquipmentType().getCode());
            pstm.setString(6, equipment.getLocation().getCode());
            pstm.setString(7, portFormatOf(equipment));
            pstm.setString(8, ipFormatOf(equipment));
            pstm.setBoolean(9, equipment.isStatus());
            pstm.setString(10, equipment.getCode());

            pstm.executeUpdate();
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
            sql += "DELETE FROM public.equipment " +
                    "WHERE code_equipment = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
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
            String sql = "SELECT code_equipment," +
                    " description_equipment," +
                    " make_equipment," +
                    " model_equipment," +
                    " equipment_type," +
                    " location," +
                    " ports," +
                    " ipAddresses, " +
                    " status";
            sql += " FROM public.equipment ";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            Hashtable<String, Equipment> ret = new Hashtable<>();
            equipmentTypeTable = loadEquipmentTypes();
            portTypeTable = loadPortTypes();
            locationTable = loadLocations();
            String[] miniReader;
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setCode(rs.getString("code_equipment"));
                equipment.setDescription(rs.getString("description_equipment"));
                equipment.setMake(rs.getString("make_equipment"));
                equipment.setModel(rs.getString("model_equipment"));
                equipment.setEquipmentType(equipmentTypeTable.get(rs.getString("equipment_type")));
                equipment.setLocation(locationTable.get(rs.getString("location")));

                miniReader = rs.getString("ports").split(",");
                for (int i = 0; i < miniReader.length; i += 2)
                    for (int cap = 0; cap < Integer.parseInt(miniReader[i + 1]); cap++)
                        equipment.addPort(portTypeTable.get(miniReader[i]));

                miniReader = rs.getString("ipAddresses").split(",");
                for (String ip : miniReader)
                    equipment.addIp(ip);

                equipment.setStatus(rs.getBoolean("status"));
                ret.put(equipment.getCode(), equipment);
            }
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

