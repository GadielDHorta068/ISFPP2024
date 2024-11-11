package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.PortType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class PortTypePosgresqlDAO implements PortTypeDAO {
    @Override
    public void insert(PortType portType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO poo2024.RCG_port_Type (" +
                    "   code," +
                    "   description," +
                    "   speed" +
                    ")";
            sql+="VALUES(?, ?, ?) ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1,portType.getCode());
            pstm.setString(2,portType.getDescription());
            pstm.setInt(3,portType.getSpeed());
            pstm.executeUpdate();
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
    public void update(PortType portType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.RCG_port_Type ";
            sql += "SET code = ?, " +
                    "   description = ?, " +
                    "   speed = ? ";
            sql += "WHERE code = ? ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, portType.getCode());
            pstm.setString(2, portType.getDescription());
            pstm.setInt(3,portType.getSpeed());
            pstm.setString(4,portType.getCode());

            pstm.execute();
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
    public void erase(PortType portType) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.RCG_port_Type " +
                    "WHERE code = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, portType.getCode());
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
    public Hashtable<String, PortType> searchAll() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConnection.getConnection();
            String sql = "SELECT code, " +
                    "   description, " +
                    "   speed ";
            sql += "FROM poo2024.RCG_port_Type ";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            Hashtable<String, PortType> ret = new Hashtable<>();
            while (rs.next()) {
                PortType portType = new PortType();
                portType.setCode(rs.getString("code"));
                portType.setDescription(rs.getString("description"));
                portType.setSpeed(rs.getInt("speed"));
                ret.put(portType.getCode(), portType);
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
    public Hashtable<String, PortType> searchAllIn(String directory) {
        return null;
    }
}
