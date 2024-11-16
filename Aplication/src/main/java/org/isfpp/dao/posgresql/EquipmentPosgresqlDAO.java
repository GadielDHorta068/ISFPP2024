package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.*;
import org.isfpp.dao.abstractDao.AbstractEquipmentDAO;
import org.isfpp.modelo.*;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class EquipmentPosgresqlDAO extends AbstractEquipmentDAO implements EquipmentDAO {
    private static boolean update = true;
    private static Hashtable<String, Equipment> map = new Hashtable<>();


    public EquipmentPosgresqlDAO() {
        super();
    }

    @Override
    public void insert(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;

        try {
            con = BDConnection.getConnection();

            System.out.println(con.getCatalog());
            System.out.println(equipment.getCode());
            // 1. Inserta el equipo y retorna el código generado
            String sqlInsertEquipment = "INSERT INTO poo2024.RCG_equipment " +
                    "(code, description, marca, modelo, code_equipment_type, code_location, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    ";";

            pstm = con.prepareStatement(sqlInsertEquipment);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getDescription());
            pstm.setString(3, equipment.getMake());
            pstm.setString(4, equipment.getModel());
            pstm.setString(5, equipment.getEquipmentType().getCode());
            pstm.setString(6, equipment.getLocation().getCode());
            pstm.setBoolean(7, equipment.isStatus());
            pstm.executeUpdate();
            System.out.println();
            //pstm.close();

            // 2. Inserta los puertos usando el código generado
            String insertsPortFormat = equipment.getAllPortsTypes().entrySet().stream()
                    .map(entry -> String.format("INSERT INTO poo2024.RCG_equipment_port (cantidad, code_port_type, code_equipment) " +
                                    "VALUES (%d, '%s', '%s')",
                            entry.getValue(), entry.getKey().getCode(), equipment.getCode()))
                    .collect(Collectors.joining("; "));

            Statement stmtPorts = con.createStatement();
            stmtPorts.executeUpdate(insertsPortFormat);  // Ejecuta todos los inserts de puertos
            stmtPorts.close();
            // 3. Inserta las IPs usando el código generado
            String insertsIpFormat = equipment.getIpAdresses().stream()
                    .map(ip -> String.format("INSERT INTO poo2024.RCG_equipment_ips (code_equipment, ip) " +
                                    "VALUES ('%s', '%s'::INET)",
                            equipment.getCode(), ip))
                    .collect(Collectors.joining("; "));  // Separa cada `INSERT` con `;` para múltiples instrucciones

            Statement stmtIps = con.createStatement();
            stmtIps.executeUpdate(insertsIpFormat);  // Ejecuta todos los inserts de IPs
            stmtIps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map.put(equipment.getCode(),equipment);
            update = true;
            try {
                if (pstm != null) pstm.close();
            } catch (SQLException e) {
                System.err.println("Error a la hora de cerrar la consulta");
                throw new RuntimeException(e);
            }
        }
        map.put(equipment.getCode(), equipment);
        update = true;

    }

    @Override
    public void update(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = BDConnection.getConnection();;

            String sql = "DELETE FROM poo2024.RCG_equipment_port WHERE code_equipment = ?;" +
                    "DELETE FROM poo2024.RCG_equipment_ips WHERE code_equipment = ?;" +

                    "UPDATE poo2024.RCG_equipment " +
                    "SET code = ?," +
                    "   description = ?," +
                    "   marca = ?," +
                    "   modelo = ?," +
                    "   code_equipment_type = ?," +
                    "   code_location = ?," +
                    "   status = ? " +
                    "WHERE code = ?;";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getCode());
            pstm.setString(3, equipment.getCode());
            pstm.setString(4, equipment.getDescription());
            pstm.setString(5, equipment.getMake());
            pstm.setString(6, equipment.getModel());
            pstm.setString(7, equipment.getEquipmentType().getCode());
            pstm.setString(8, equipment.getLocation().getCode());
            pstm.setBoolean(9, equipment.isStatus());
            pstm.setString(10, equipment.getCode());
            pstm.executeUpdate();
            pstm.close();

            // 2. Inserta los puertos usando el código generado
            HashMap<PortType, Integer> ports = equipment.getAllPortsTypes();
            if (!ports.isEmpty()) {
                String insertsPortFormat = ports.entrySet().stream()
                        .map(entry -> String.format("INSERT INTO poo2024.RCG_equipment_port (cantidad, code_port_type, code_equipment) " +
                                        "VALUES (%d, '%s', '%s')",
                                entry.getValue(), entry.getKey().getCode(), equipment.getCode()))
                        .collect(Collectors.joining("; "));  // Separa cada `INSERT` con `;` para ejecutar múltiples instrucciones


                Statement stmtPorts = con.createStatement();
                stmtPorts.executeUpdate(insertsPortFormat);  // Ejecuta todos los inserts de puertos
                stmtPorts.close();
            }
            // 3. Inserta las IPs usando el código generado
            if (equipment.getIpAdresses().size() > 1) {
                String insertsIpFormat = equipment.getIpAdresses().stream()
                        .map(ip -> String.format("INSERT INTO poo2024.RCG_equipment_ips (code_equipment, ip) " +
                                        "VALUES ('%s', '%s'::INET)",
                                equipment.getCode(), ip))
                        .collect(Collectors.joining("; "));  // Separa cada `INSERT` con `;` para múltiples instrucciones

                Statement stmtIps = con.createStatement();
                stmtIps.executeUpdate(insertsIpFormat);  // Ejecuta todos los inserts de IPs
                stmtIps.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            map.replace(equipment.getCode(), equipment);
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
    public void erase(Equipment equipment) {
        java.sql.Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
             con = BDConnection.getConnection();
            String sql = "";
            sql += "DELETE FROM poo2024.RCG_equipment_ips WHERE code_equipment = ?; " +
                    "DELETE FROM poo2024.RCG_equipment_port WHERE code_equipment = ?; " +
                    "DELETE FROM poo2024.RCG_equipment WHERE code = ?";

            pstm = con.prepareStatement(sql);
            pstm.setString(1, equipment.getCode());
            pstm.setString(2, equipment.getCode());
            pstm.setString(3, equipment.getCode());
            pstm.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            map.remove(equipment.getCode());
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
        map.remove(equipment.getCode());
    }

    @Override
    public Hashtable<String, Equipment> searchAll() {
        if (update) {
            Hashtable<String, Equipment> ret = new Hashtable<>();
            java.sql.Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = BDConnection.getConnection();
                String sql = "SELECT " +
                        "   e.code AS equipment_code," +
                        "   e.description," +
                        "   e.marca," +
                        "   e.modelo," +
                        "   e.code_equipment_type," +
                        "   e.code_location," +
                        "   e.status," +
                        "   p.cantidad, " +
                        "   p.code_port_type, " +
                        "   i.ip" +
                        " " +
                        "FROM poo2024.rcg_equipment e left join poo2024.rcg_equipment_port p ON (e.code = p.code_equipment)" +
                        " LEFT JOIN poo2024.rcg_equipment_ips i on (e.code = i.code_equipment)" +
                        ";" ;
                pstm = con.prepareStatement(sql);
                rs = pstm.executeQuery();
                equipmentTypes = readEquipmentTypes();
                portTypes = readPortTypes();
                locations = readLocations();
                while (rs.next()) {
                    String code = rs.getString("equipment_code");
                    Equipment equipment = ret.get(code);
                    if (equipment == null) {
                        equipment = new Equipment();
                        equipment.setCode(code);
                        equipment.setDescription(rs.getString("description"));
                        equipment.setMake(rs.getString("marca"));
                        equipment.setModel(rs.getString("modelo"));
                        equipment.setEquipmentType(equipmentTypes.get(rs.getString("code_equipment_type")));
                        equipment.setLocation(locations.get(rs.getString("code_location")));
                        equipment.setStatus(rs.getBoolean("status"));
                        ret.put(code, equipment);
                    }

                    // Agrega los puertos, si están presentes
                    String portTypeCode = rs.getString("code_port_type");
                    int portCapacity = rs.getInt("cantidad");
                    if (portTypeCode != null && portCapacity > 0) {
                        for (int i = 0; i < portCapacity; i++) {
                            equipment.addPort(portTypes.get(portTypeCode));
                        }
                    }
                    String ip = rs.getString("ip");
                    if (ip != null && !equipment.getIpAdresses().contains(ip)) {
                        equipment.addIp(ip);
                    }
                }
                rs.close();
                pstm.close();
            } catch (Exception ex) {
                ex.printStackTrace();
              //  throw new RuntimeException(ex);
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
        super.insertAllIn(directory,map);
    }

    @Override
    public Hashtable<String, Equipment> searchAllIn(String directory) {
        for (Equipment equipment: super.searchAllIn(directory).values())
            if (!map.contains(equipment))
                map.put(equipment.getCode(), equipment);
        return map;
    }
}

