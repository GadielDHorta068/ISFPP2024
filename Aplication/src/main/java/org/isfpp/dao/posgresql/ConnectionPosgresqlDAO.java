package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.Secuencial.ConnectionSequentialDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ConnectionPosgresqlDAO implements ConnectionDAO{
    private static boolean update;
    private static List<Connection> list;

    private final PortTypeDAO portTypeDAO;
    private final EquipmentDAO equipmentDAO;
    private final WireTypeDAO wireTypeDAO;

    private Hashtable<String, PortType> portTypeTable;
    private Hashtable<String, Equipment> equipmentTable;
    private Hashtable<String, WireType> wireTypeTable;

    public ConnectionPosgresqlDAO(){
        portTypeDAO = new PortTypePosgresqlDAO();
        wireTypeDAO = new WireTypePosgresqlDAO();
        equipmentDAO = new EquipmentPosgresqlDAO();
    }

    private Hashtable<String, PortType> loadPortTypes(){
        return portTypeDAO.searchAll();
    }

    private Hashtable<String, Equipment> loadEquipments(){
        return equipmentDAO.searchAll();
    }

    private Hashtable<String, WireType> loadWireType(){
        return wireTypeDAO.searchAll();
    }

    @Override
    public void insert(Connection connection) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql ="";
            sql+="INSERT INTO poo2024.rcg_connection (" +
                    "   code_port_type1," +
                    "   code_equipment1," +
                    "   code_port_type2," +
                    "   code_equipment2," +
                    "   code_wire_type" +
                    ")";
            sql+="VALUES(?, ?, ?, ?, ?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(2, connection.getPort1().getPortType().getCode());
            pstm.setString(1, connection.getPort1().getEquipment().getCode());
            pstm.setString(4, connection.getPort2().getPortType().getCode());
            pstm.setString(3, connection.getPort2().getEquipment().getCode());
            pstm.setString(5, connection.getWire().getCode());
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
    public void update(Connection connection) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();;
            String sql = "UPDATE poo2024.rcg_connection ";
            sql += "SET code_port_type1 = ?," +
                    "   code_equipment1 = ?," +
                    "   code_port_type2 = ?," +
                    "   code_equipment2 = ?," +
                    "   code_wire_type = ?";
            sql += "WHERE code_equipment1 = ? AND code_equipment2 = ?";
            pstm = con.prepareStatement(sql);
            //SET
            pstm.setString(2, connection.getPort1().getPortType().getCode());
            pstm.setString(1, connection.getPort1().getEquipment().getCode());
            pstm.setString(4, connection.getPort2().getPortType().getCode());
            pstm.setString(3, connection.getPort2().getEquipment().getCode());
            pstm.setString(5, connection.getWire().getCode());
            //WHERE
            pstm.setString(6, connection.getPort1().getEquipment().getCode());
            pstm.setString(7, connection.getPort2().getEquipment().getCode());
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
    public void erase(Connection connection) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.rcg_connection " +
                    "WHERE code_equipment1 = ? AND code_equipment2 = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, connection.getPort1().getEquipment().getCode());
            pstm.setString(2, connection.getPort2().getEquipment().getCode());
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
    public List<Connection> searchAll() {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConnection.getConnection();
            String sql = "SELECT code_port_type1," +
                    "   code_equipment1," +
                    "   code_port_type2," +
                    "   code_equipment2," +
                    "   code_wire_type";
            sql += " FROM poo2024.rcg_connection ";
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            List<Connection> ret = new ArrayList<>();
            equipmentTable = loadEquipments();
            portTypeTable = loadPortTypes();
            wireTypeTable = loadWireType();
            while (rs.next()) {
                Connection connection = new Connection();
                connection.setPort1(equipmentTable.get(rs.getString("code_equipment1")).
                        checkPort(portTypeTable.get(rs.getString("code_port_type1"))));
                connection.setPort2(equipmentTable.get(rs.getString("code_equipment2")).
                        checkPort(portTypeTable.get(rs.getString("code_port_type2"))));
                connection.setWire(wireTypeTable.get(rs.getString("code_wire_type")));

                ret.add(connection);
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
    public List<Connection> searchAllIn(String directory) {
        return List.of();
    }

}
