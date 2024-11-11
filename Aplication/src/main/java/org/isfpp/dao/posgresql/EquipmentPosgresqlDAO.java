package org.isfpp.dao.posgresql;

import org.isfpp.connection.BDConnection;
import org.isfpp.dao.*;
import org.isfpp.modelo.*;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class EquipmentPosgresqlDAO implements EquipmentDAO {
    private static boolean update = true;
    private static Hashtable<String, Equipment> map = new Hashtable<>();

    private PortTypeDAO portTypeDAO;
    private EquipmentTypeDAO equipmentTypeDAO;
    private LocationDAO locationDAO;

    private Hashtable<String, PortType> portTypeTable;
    private Hashtable<String, EquipmentType> equipmentTypeTable;
    private Hashtable<String, Location> locationTable;

    public EquipmentPosgresqlDAO() {
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

    private Hashtable<String, Equipment> readFromFile(String fileName) {
        Hashtable<String, Equipment> map = new Hashtable<>();
        Scanner inFile = null;
        try {
            // Cambia a FileInputStream para leer del sistema de archivos
            inFile = new Scanner(new FileInputStream(fileName));
            inFile.useDelimiter("\\s*;\\s*");
            String[] minireader;
            while (inFile.hasNext()) {
                Equipment equipment = new Equipment();
                equipment.setCode(inFile.next());
                equipment.setDescription(inFile.next());
                equipment.setMake(inFile.next());
                equipment.setModel(inFile.next());
                equipment.setEquipmentType(equipmentTypeTable.get(inFile.next()));
                equipment.setLocation(locationTable.get(inFile.next()));
                minireader = inFile.next().split(",");
                for (int i = 0; i < minireader.length; i += 2)
                    for (int cap = 0; cap < Integer.parseInt(minireader[i + 1]); cap++)
                        equipment.addPort(portTypeTable.get(minireader[i]));

                minireader = inFile.next().split(",");
                for (String ip : minireader)
                    equipment.addIp(ip);

                equipment.setStatus(inFile.nextBoolean());
                System.out.println("SAS EN TODA LA BOCA"+equipment.getCode());
                map.put(equipment.getCode(), equipment);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error al abrir el archivo: " + fileName);
            fileNotFoundException.printStackTrace();
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error en la estructura del registro del archivo.");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error al leer desde el archivo.");
            illegalStateException.printStackTrace();
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String, Equipment> equipmentMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            HashMap<PortType, Integer> portMap;
            for (Equipment equipment : equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                writer.write(String.format("%s;%s;%s;%s;%s;%s;",
                        equipment.getCode(),
                        equipment.getDescription(),
                        equipment.getMake(),
                        equipment.getModel(),
                        equipment.getEquipmentType().getCode(),
                        equipment.getLocation().getCode()));
                int portIndex = 0;
                for (PortType portType : portMap.keySet()) {
                    writer.write(String.format("%s,%s%s", portType.getCode(), portMap.get(portType),
                            (portIndex < portMap.size() - 1 ? "," : ";")));
                    portIndex++;
                }
                for (int i = 0; i < equipment.getIpAdresses().size(); i++)
                    writer.write(String.format("%s%s", equipment.getIpAdresses().get(i),
                            (i < equipment.getIpAdresses().size() - 1 ? "," : ";")));

                writer.write(String.format("%s;", equipment.isStatus() ? "true" : "false"));
                writer.newLine();  // Escribir salto de línea después de cada línea
            }
            System.out.println("Archivo reescrito exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();  // Manejo de excepciones
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error al escribir en el archivo.");
        }
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
                    .collect(Collectors.joining("; "));  // Separa cada `INSERT` con `;` para ejecutar múltiples instrucciones

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
            System.out.println("LALLAL");
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
        map.replace(equipment.getCode(),equipment);
        update = true;
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
    public void insertAllIn(String directory) {
        boolean check = true;
        // Validación: el directorio no debe ser nulo ni vacío
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            check = false;
        }

        // Validación: Verificar si el directorio existe y es un directorio válido
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            check = false;
        }

        // Crear la ruta completa al archivo
        File file = new File(directory, "data"+File.pathSeparator+"equipo.txt");

        // Validación: Verificar si el archivo existe y es un archivo regular
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            check = false;
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            check = false;
        }

        // Validación: Verificar si el archivo es legible
        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            check = false;
        }


        if (check)
            writeToFile(map, file.getAbsolutePath());
    }

    @Override
    public Hashtable<String, Equipment> searchAll() {
        if (update) {
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
                Hashtable<String, Equipment> ret = new Hashtable<>();
                equipmentTypeTable = loadEquipmentTypes();
                portTypeTable = loadPortTypes();
                locationTable = loadLocations();
                while (rs.next()) {
                    String code = rs.getString("equipment_code");
                    Equipment equipment = ret.get(code);
                    if (equipment == null) {
                        equipment = new Equipment();
                        equipment.setCode(code);
                        equipment.setDescription(rs.getString("description"));
                        equipment.setMake(rs.getString("marca"));
                        equipment.setModel(rs.getString("modelo"));
                        equipment.setEquipmentType(equipmentTypeTable.get(rs.getString("code_equipment_type")));
                        equipment.setLocation(locationTable.get(rs.getString("code_location")));
                        equipment.setStatus(rs.getBoolean("status"));
                        ret.put(code, equipment);
                    }

                    // Agrega los puertos, si están presentes
                    String portTypeCode = rs.getString("code_port_type");
                    int portCapacity = rs.getInt("cantidad");
                    if (portTypeCode != null && portCapacity > 0) {
                        for (int i = 0; i < portCapacity; i++) {
                            equipment.addPort(portTypeTable.get(portTypeCode));
                        }
                    }
                    String ip = rs.getString("ip");
                    if (ip != null)
                        equipment.addIp(ip);

                }
                rs.close();
                pstm.close();
                map = ret;
            } catch (Exception ex) {
                ex.printStackTrace();
              //  throw new RuntimeException(ex);
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
            update = false;
        }
        return map;
    }


    @Override
    public Hashtable<String, Equipment> searchAllIn(String directory) {
        // Validación: el directorio no debe ser nulo ni vacío
        if (directory == null || directory.trim().isEmpty()) {
            System.err.println("El directorio proporcionado es nulo o está vacío.");
            return new Hashtable<>();
        }

        // Validación: Verificar si el directorio existe y es un directorio válido
        File dir = new File(directory);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("El directorio no existe o no es válido: " + dir.getAbsolutePath());
            return new Hashtable<>();

        }
        // Crear la ruta completa al archivo
        File file = new File(directory, "data"+File.pathSeparator+"equipo.txt");

        // Validación: Verificar si el archivo existe y es un archivo regular
        if (!file.exists()) {
            System.err.println("El archivo no existe: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        if (!file.isFile()) {
            System.err.println("La ruta no es un archivo válido: " + file.getAbsolutePath());
            return new Hashtable<>();
        }

        // Validación: Verificar si el archivo es legible
        if (!file.canRead()) {
            System.err.println("El archivo no tiene permisos de lectura: " + file.getAbsolutePath());
            return new Hashtable<>();
        }
        Hashtable<String,Equipment> equipmentHashtable =  readFromFile(file.getAbsolutePath());
        // Intentar leer el archivo y manejar posibles excepciones
        // Leer el archivo y devolver la lista de conexiones
        update = false;
        return equipmentHashtable;
    }
}

