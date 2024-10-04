package org.isfpp.test;

import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.isfpp.logica.Utils;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    private Graph<Equipment, Connection> graph;
    private Equipment equipment1;
    private Equipment equipment2;
    private Equipment equipment3;
    private Connection connection1;
    private Connection connection2;
    Utils u1;

    @BeforeEach
    public void setUp() {
        Web w1=new Web("testWeb");

        // Creación de objetos PortType
        PortType portType1 =  w1.addPort("P1", "Ethernet Port", 1000); // 1 Gbps
        PortType portType2 = w1.addPort("P2", "Fiber Optic Port", 10000); // 10 Gbps
        PortType portType3 = w1.addPort("P3", "Wi-Fi Port", 300); // 300 Mbps
        // Crear un nuevo tipo de equipo
        EquipmentType equipmentType1 = w1.addEquipmentType("ET1", "Router");
        EquipmentType equipmentType2 = w1.addEquipmentType("ET2", "Switch");
        EquipmentType equipmentType3 = w1.addEquipmentType("ET3", "Access Point");


        // localizaciones

        Location location1 = w1.addLocation("1","Sala de Servidores");
        Location location2 = w1.addLocation("2","Sala de Conferencias");
        Location location3 = w1.addLocation("3","Auditorio");


        Equipment equipment1 = w1.addEquipment("001", "Router Principal", "Cisco", "RV340", portType1, 5,
                equipmentType1, location1, true);




        Equipment equipment2 = w1.addEquipment("002", "PC de Reuniones", "HP", "Pavilion", portType2, 10,
                equipmentType2, location2, true);





        Equipment equipment3 = w1.addEquipment("003", "Proyector de Presentaciones", "Epson", "PowerLite", portType3, 2,
                equipmentType3, location3, false);
        WireType wireType1 = w1.addWire("WT1", "Cable de fibra óptica", 10000); // Velocidad en Mbps
        WireType wireType2 = w1.addWire("WT2", "Cable de cobre", 1000); // Velocidad en Mbps


        // Crear conexiones
        connection1 = w1.addConnection(equipment2,equipment1, wireType1);  // Activa
        connection2 = w1.addConnection(equipment3,equipment2, wireType2);  // Entre equipo activo e inactivo
        u1=new Utils();




    }
    @Test
    public void testDetectConnectivityIssues_AllActive() {
        // Activar todos los equipos para esta prueba
        // Ahora todos los equipos están activos

        // Verificar que se puede acceder a todos los nodes
        List<Equipment> reachable = u1.detectConnectivityIssues(equipment1);
        for(Equipment e:reachable){
            System.out.println(e);
        }
        assertEquals(3, reachable.size());
        assertTrue(reachable.contains(equipment1));
        assertTrue(reachable.contains(equipment2));
        assertTrue(reachable.contains(equipment3));
    }

    @Test
    public void testDetectConnectivityIssues_ConnectionIssue() {
        // Dejar equipment3 inactivo (caso por defecto)
        equipment3.setStatus(false);

        // Verificar que solo se puede acceder a equipment1 y equipment2
        List<Equipment> reachable = u1.detectConnectivityIssues(equipment1);
        for(Equipment e:reachable){
            System.out.println(e);
        }
        assertEquals(2, reachable.size());
        assertTrue(reachable.contains(equipment1));
        assertTrue(reachable.contains(equipment2));
        assertTrue(reachable.contains(equipment3));
    }

    @Test
    public void testDetectConnectivityIssues_SingleNode() {
        // Verificar que desde un solo equipo activo se pueda acceder solo a él mismo
        List<Equipment> reachable = u1.detectConnectivityIssues(equipment1);
        assertTrue(reachable.contains(equipment1));
        assertEquals(2, reachable.size()); // Porque también equipment2 está accesible
    }
}


