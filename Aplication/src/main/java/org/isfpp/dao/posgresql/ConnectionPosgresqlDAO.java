package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.ConnectionDAO;
import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.dao.Secuencial.ConnectionSequentialDAO;
import org.isfpp.dao.WireTypeDAO;
import org.isfpp.dao.abstractDao.AbstractConnectionDAO;
import org.isfpp.modelo.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ConnectionPosgresqlDAO extends AbstractConnectionDAO implements ConnectionDAO{
    private static boolean update;
    private static List<Connection> list;

    public ConnectionPosgresqlDAO(){
        super();
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
            pstm.setString(1, connection.getPort1().getPortType().getCode());
            pstm.setString(2, connection.getPort1().getEquipment().getCode());
            pstm.setString(3, connection.getPort2().getPortType().getCode());
            pstm.setString(4, connection.getPort2().getEquipment().getCode());
            pstm.setString(5, connection.getWire().getCode());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            list.add(connection);
            update = true;
            try {
                if(rs != null)
                    rs.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                System.err.println("Error a la hora de cerrar la consulta");
                e.printStackTrace();
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
            pstm.setString(1, connection.getPort1().getPortType().getCode());
            pstm.setString(2, connection.getPort1().getEquipment().getCode());
            pstm.setString(3, connection.getPort2().getPortType().getCode());
            pstm.setString(4, connection.getPort2().getEquipment().getCode());
            pstm.setString(5, connection.getWire().getCode());
            //WHERE
            pstm.setString(6, connection.getPort1().getEquipment().getCode());
            pstm.setString(7, connection.getPort2().getEquipment().getCode());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            int pos = list.indexOf(connection);
            list.set(pos, connection);
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
            list.remove(connection);
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
    public List<Connection> searchAll() {
        if(update) {
            List<Connection> ret = new ArrayList<>();
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
                equipments = super.readEquipments();
                portTypes = super.readPortTypes();
                wireTypes = super.readWireTypes();
                while (rs.next()) {
                    Connection connection = new Connection();

                    connection.setPort1(equipments.get(rs.getString("code_equipment1")).
                            checkPort(portTypes.get(rs.getString("code_port_type1"))));
                    connection.setPort2(equipments.get(rs.getString("code_equipment2")).
                            checkPort(portTypes.get(rs.getString("code_port_type2"))));
                    connection.setWire(wireTypes.get(rs.getString("code_wire_type")));

                    ret.add(connection);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                list = ret;
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
        return list;
    }

    @Override
    public void insertAllIn(String directory) {
        super.insertAllIn(directory, list);
    }

    @Override
    public List<Connection> searchAllIn(String directory) {
        for (Connection connection: super.searchAllIn(directory)){
            insert(connection);
            list.add(connection);
        }
        return list;
    }
}
