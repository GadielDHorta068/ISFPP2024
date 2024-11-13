package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.abstractDao.AbstractEquimentTypeDAO;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.EquipmentType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class EquipmentTypePosgresqlDAO extends AbstractEquimentTypeDAO implements EquipmentTypeDAO {
    private static Hashtable<String, EquipmentType> map;
    private static boolean update  = true;

    public EquipmentTypePosgresqlDAO(){
        super();
    }

    @Override
    public void insert(EquipmentType equipmentType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO poo2024.rcg_equipment_type (" +
                    "   code," +
                    "   description" +
                    ") " +
                    "VALUES(?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipmentType.getCode());
            pstm.setString(2, equipmentType.getDescription());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            map.put(equipmentType.getCode(),equipmentType);
            update = true;
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
    public void update(EquipmentType equipmentType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.rcg_equipment_type ";
            sql += "SET code = ?," +
                    " description = ? ";
            sql += "WHERE code = ?";
            pstm = con.prepareStatement(sql);
            //SET
            pstm.setString(1, equipmentType.getCode());
            pstm.setString(2, equipmentType.getDescription());
            //WHERE
            pstm.setString(3, equipmentType.getCode());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map.replace(equipmentType.getCode(), equipmentType);
            update = true;
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
    public void erase(EquipmentType equipmentType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.rcg_equipment_type WHERE code = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipmentType.getCode());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            map.remove(equipmentType.getCode());
            update = true;
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
    public Hashtable<String, EquipmentType> searchAll() {
        if (update) {
            Hashtable<String, EquipmentType> ret = new Hashtable<>();
            java.sql.Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = BDConnection.getConnection();
                String sql = "SELECT " +
                        "   code," +
                        "   description " +
                        "FROM poo2024.rcg_equipment_type ";
                pstm = con.prepareStatement(sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    EquipmentType equipmentType = new EquipmentType();
                    equipmentType.setCode(rs.getString("code"));
                    equipmentType.setDescription(rs.getString("description"));
                    ret.put(equipmentType.getCode(), equipmentType);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                map = ret;
                update = false;
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
        return map;
    }

    @Override
    public void insertAllIn(String directory) {
        super.insertAllIn(directory, map);
    }

    @Override
    public Hashtable<String, EquipmentType> searchAllIn(String directory) {
        for(EquipmentType equimentType: super.searchAllIn(directory).values())
            if (!map.contains(equimentType))
                map.put(equimentType.getCode(),equimentType);
        return map;
    }
}
