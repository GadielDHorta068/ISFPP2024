package org.isfpp.dao.Secuencial;

import org.isfpp.dao.EquipmentDAO;
import org.isfpp.dao.EquipmentTypeDAO;
import org.isfpp.dao.LocationDAO;
import org.isfpp.dao.PortTypeDAO;
import org.isfpp.modelo.*;

import java.io.*;
import java.util.*;

public class EquipmentSequentialDAO implements EquipmentDAO{
    private Hashtable<String, Equipment> map;
    private String fileName;
    private boolean update;
    private Hashtable<String, PortType> portTypes;
    private Hashtable<String,EquipmentType> equipmentTypes;
    private Hashtable<String,Location> locations;

    public EquipmentSequentialDAO() {
        equipmentTypes = readEquipmentTypes();
        locations = readLocations();
        portTypes = readPortTypes();
        ResourceBundle rb = ResourceBundle.getBundle("config");
        fileName = rb.getString("rs.connection");
        update = true;
    }

    private Hashtable<String, Equipment> readFromFile(String fileName) {
        Hashtable<String, Equipment> map = new Hashtable<>();
        Scanner inFile = null;

        try {
            inFile = new Scanner(new File(fileName));
            String[] minireader;
            while (inFile.hasNext()) while (inFile.hasNext()) {
                String line = inFile.nextLine(); // Leer toda la línea para inspección
                System.out.println("Línea leída: " + line);
                String[] fields = line.split(";");

                if (fields.length < 9) {  // Asegúrate de que haya suficientes campos en la línea
                    System.err.println("Error: La línea no tiene suficientes campos.");
                    continue;
                }

                Equipment equipment = new Equipment();
                equipment.setCode(fields[0]);
                equipment.setDescription(fields[1]);
                equipment.setMake(fields[2]);
                equipment.setModel(fields[3]);
                equipment.setEquipmentType(equipmentTypes.get(fields[4]));
                equipment.setLocation(locations.get(fields[5]));

                // Ahora trata el campo problemático
                String portInfo = fields[6];  // "1G,4,SFP+,1"
                System.out.println("Información de puertos: " + portInfo);
                minireader = portInfo.split(",");

                for (int i = 0; i < minireader.length; i += 2) {
                    String portType = minireader[i];  // Ejemplo: "1G"
                    int portCount = Integer.parseInt(minireader[i + 1]);  // Ejemplo: 4
                    System.out.println("Tipo de puerto: " + portType + ", Cantidad: " + portCount);

                    for (int cap = 0; cap < portCount; cap++) {
                        equipment.addPort(portTypes.get(portType));
                    }
                }

                // Procesar las direcciones IP
                String[] ips = fields[7].split(",");
                for (String ip : ips) {
                    equipment.addIp(ip);
                }

                equipment.setStatus(Boolean.parseBoolean(fields[8]));

                map.put(equipment.getCode(), equipment);
            }

        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error opening file.");
            fileNotFoundException.printStackTrace();
        } catch (NoSuchElementException noSuchElementException) {
            System.err.println("Error in file record structure");
            noSuchElementException.printStackTrace();
        } catch (IllegalStateException illegalStateException) {
            System.err.println("Error reading from file.");
            illegalStateException.printStackTrace();
        } finally {
            if (inFile != null)
                inFile.close();
        }
        return map;
    }

    private void writeToFile(Hashtable<String,Equipment> equipmentMap, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + fileName, false))) {
            HashMap<PortType, Integer> portMap;
            for (Equipment equipment: equipmentMap.values()) {
                portMap = equipment.getAllPortsTypes();
                writer.write(String.format("%s;%s;%s;%s;%s;%s;",equipment.getCode(),
                        equipment.getDescription(),equipment.getMake(),equipment.getModel(),
                        equipment.getEquipmentType().getCode(),equipment.getLocation().getCode()));

                for (PortType portType: portMap.keySet())
                    writer.write(String.format("%s,%s",portType.getCode(),portMap.get(portType)));
                writer.write(";");

                for (int i = 1; i<equipment.getIpAdresses().size();i++)
                    if (!(i == equipment.getIpAdresses().size()))
                        writer.write(String.format("%s,",equipment.getIpAdresses().get(i)));
                    else
                        writer.write(String.format("%s;",equipment.getIpAdresses().get(i)));

                if (equipment.isStatus())
                    writer.write(String.format("true;"));
                else
                    writer.write(String.format("false;"));

                writer.newLine();  // Escribir salto de línea después de cada línea
            }
            System.out.println("Archivo reescrito exitosamente.");
        }catch (IOException e) {
            e.printStackTrace();  // Manejo de excepciones
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file.");
        }
    }

    @Override
    public void insert(Equipment equipment) {
        map.put(equipment.getCode(),equipment);
        writeToFile(map,fileName);
        update = true;
    }

    @Override
    public void update(Equipment equipment) {
        map.replace(equipment.getCode(),equipment);
        update = true;
    }

    @Override
    public void erase(Equipment equipment) {
        map.remove(equipment.getCode());
        writeToFile(map, fileName);
        update = true;
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
        File file = new File(directory, fileName);

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
    public Hashtable<String,Equipment> searchAll() {
        if (update) {
            map = readFromFile(fileName);
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
            File file = new File(directory, fileName);

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
        return equipmentHashtable; // Retornar la lista de conexiones leídas
    }

    private Hashtable<String, EquipmentType> readEquipmentTypes() {
        EquipmentTypeDAO equipmentTypeDAO = new EquipmentTypeSequentialDAO();
       return equipmentTypeDAO.searchAll();
    }
    private Hashtable<String,Location> readLocations(){
        LocationDAO locationDAO = new LocationSequentialDAO();
        return  locationDAO.searchAll();
    }

    private Hashtable<String, PortType> readPortTypes() {
        PortTypeDAO portTypeDAO = new PortTypeSequentialDAO();
        return portTypeDAO.searchAll();
    }
}
