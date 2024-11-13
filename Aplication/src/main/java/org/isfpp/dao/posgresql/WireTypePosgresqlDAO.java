package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.dao.abstractDao.AbstractWireTypeDAO;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.WireType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public class WireTypePosgresqlDAO extends AbstractWireTypeDAO implements WireTypeDAO {
    private static Hashtable<String, WireType> map;
    private static boolean update  = true;

    public WireTypePosgresqlDAO(){
        super();
    }

    @Override
    public void insert(WireType wireType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO poo2024.RCG_Wire_type (" +
                    "   code," +
                    "   description," +
                    "   speed)";
            sql+="VALUES(?, ?, ?) ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1,wireType.getCode());
            pstm.setString(2,wireType.getDescription());
            pstm.setInt(3,wireType.getSpeed());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            map.put(wireType.getCode(),wireType);
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
    public void update(WireType wireType) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.RCG_Wire_type ";
            sql += "SET code = ?, " +
                    "   description = ?, " +
                    "   speed = ? ";
            sql += "WHERE code = ? ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, wireType.getCode());
            pstm.setString(2, wireType.getDescription());
            pstm.setInt(3,wireType.getSpeed());
            pstm.setString(4,wireType.getCode());

            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map.replace(wireType.getCode(),wireType);
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
    public void erase(WireType wireType) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.RCG_Wire_type " +
                    "WHERE getCode() = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, wireType.getCode());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            map.remove(wireType.getCode());
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
    public Hashtable<String, WireType> searchAll() {
        if (update) {
            Hashtable<String, WireType> ret = new Hashtable<>();
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = BDConnection.getConnection();
                String sql = "SELECT code, " +
                        "   description, " +
                        "   speed ";
                sql += "FROM poo2024.RCG_Wire_type ";
                pstm = con.prepareStatement(sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    WireType wireType = new WireType();
                    wireType.setCode(rs.getString("code"));
                    wireType.setDescription(rs.getString("description"));
                    wireType.setSpeed(rs.getInt("speed"));
                    ret.put(wireType.getCode(), wireType);
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
    public Hashtable<String, WireType> searchAllIn(String directory) {
        for(WireType wireType: super.searchAllIn(directory).values())
            if (!map.contains(wireType))
                map.put(wireType.getCode(),wireType);
        return map;
    }}
