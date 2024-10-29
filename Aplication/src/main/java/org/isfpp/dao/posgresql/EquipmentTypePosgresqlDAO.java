package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.EquipmentType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class EquipmentTypePosgresqlDAO implements EquipmentTypeDAO {

    public EquipmentTypePosgresqlDAO(){

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
    public Hashtable<String, EquipmentType> searchAll() {
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
            Hashtable<String, EquipmentType> ret= new Hashtable<>();
            while (rs.next()) {
                EquipmentType equipmentType = new EquipmentType();
                equipmentType.setCode(rs.getString("code"));
                equipmentType.setDescription(rs.getString("description"));
                ret.put(equipmentType.getCode(), equipmentType);
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
    public Hashtable<String, EquipmentType> searchAllIn(String directory) {
        return null;
    }
}
