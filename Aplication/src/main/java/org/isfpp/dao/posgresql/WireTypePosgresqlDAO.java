package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.WireType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class WireTypePosgresqlDAO implements WireTypeDAO {
    public WireTypePosgresqlDAO(){

    }

    @Override
    public void insert(WireType wireType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO public.wire_type (code_wire_type," +
                    " description_wire_type," +
                    " speed_wire_type)";
            sql+="VALUES(?, ?, ?) ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1,wireType.getCode());
            pstm.setString(2,wireType.getDescription());
            pstm.setInt(3,wireType.getSpeed());
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
    public void update(WireType wireType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE public.wire_type ";
            sql += "SET code_wire_type = ?, " +
                    "description_wire_type = ?, " +
                    "speed_wire_type = ? ";
            sql += "WHERE code_wire_type = ? ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, wireType.getDescription());
            pstm.setString(2, wireType.getDescription());
            pstm.setInt(3,wireType.getSpeed());
            pstm.setString(4,wireType.getCode());

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
    public void erase(WireType wireType) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM public.wire_type " +
                    "WHERE code_wire_type = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, wireType.getCode());
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
    public Hashtable<String, WireType> searchAll() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConnection.getConnection();
            String sql = "SELECT code_wire_type, " +
                    "description_wire_type, " +
                    "speed_wire_type ";
            sql += "FROM public.wire_ype ";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            Hashtable<String, WireType> ret = new Hashtable<>();
            while (rs.next()) {
                WireType wireType = new WireType();
                wireType.setCode(rs.getString("code_wire_type"));
                wireType.setDescription(rs.getString("description_wire_type"));
                wireType.setSpeed(rs.getInt("speed_wire_type"));
                ret.put(wireType.getCode(), wireType);
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
    public Hashtable<String, WireType> searchAllIn(String directory) {
        return null;
    }
}
