package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.abstractDao.AbstractLocationDAO;
import org.isfpp.modelo.Location;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

public class LocationPosgresqlDAO extends AbstractLocationDAO implements LocationDAO {
    private static Hashtable<String, Location> map;
    private static boolean update  = true;

    public LocationPosgresqlDAO(){
        super();
    }

    @Override
    public void insert(Location location) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO poo2024.RCG_Location (" +
                    "   code," +
                    "   description" +
                    ") " +
                    "VALUES(?,?) ";
            pstm = con.prepareStatement(sql);
            pstm.setString(1,location.getCode());
            pstm.setString(2,location.getDescription());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            map.put(location.getCode(),location);
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
    public void update(Location location) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.RCG_Location " +
                    "SET code = ?, " +
                    "   description = ? " +
                    "WHERE code = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, location.getCode());
            pstm.setString(2, location.getDescription());
            pstm.setString(3,location.getCode());
            pstm.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map.replace(location.getCode(),location);
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
    public void erase(Location location) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.RCG_Location " +
                    "WHERE code = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, location.getCode());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            map.remove(location.getCode());
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
    public Hashtable<String, Location> searchAll() {
        if (update) {
            Hashtable<String, Location> ret = new Hashtable<String, Location>();
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = BDConnection.getConnection();
                String sql = "SELECT code," +
                        "   description " +
                        "FROM poo2024.RCG_Location";
                pstm = con.prepareStatement(sql);
                rs = pstm.executeQuery();
                while (rs.next()) {
                    Location location = new Location();
                    location.setCode(rs.getString("code"));
                    location.setDescription(rs.getString("description"));
                    ret.put(location.getCode(), location);
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
    public Hashtable<String, Location> searchAllIn(String directory) {
        for(Location location: super.searchAllIn(directory).values())
            if (!map.contains(location))
                map.put(location.getCode(),location);
        return map;
    }
}
