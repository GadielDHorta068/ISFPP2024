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



        // Crear equipos

        Location location1 = new Location("1","Sala de Servidores"); // Ejemplo de creación de un objeto Location

        Equipment equipment1 = new Equipment("001", "Router Principal", "Cisco", "RV340", portType1, 5,
                equipmentType1, location1, true);


        Location location2 = new Location("2","Sala de Conferencias");

        Equipment equipment2 = new Equipment("002", "PC de Reuniones", "HP", "Pavilion", portType2, 10,
                equipmentType2, location2, true);



        Location location3 = new Location("2","Auditorio");

        Equipment equipment3 = new Equipment("003", "Proyector de Presentaciones", "Epson", "PowerLite", portType3, 2,
                equipmentType3, location3, false);
        WireType wireType1 = new WireType("WT1", "Cable de fibra óptica", 10000); // Velocidad en Mbps
        WireType wireType2 = new WireType("WT2", "Cable de cobre", 1000); // Velocidad en Mbps


        // Crear conexiones
        connection1 = w1.addConnection(equipment2,equipment1, wireType1);  // Activa
        connection2 = w1.addConnection(equipment3,equipment2, wireType2);  // Entre equipo activo e inactivo

        // Agregar aristas al grafo

    }

    @Test
    public void testDetectConnectivityIssues_AllActive() {
        // Activar todos los equipos para esta prueba
        equipment3.setStatus(true);  // Ahora todos los equipos están activos

        // Verificar que se puede acceder a todos los nodos
        List<Equipment> reachable = Utils.detectConnectivityIssues(graph, equipment1);
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
        List<Equipment> reachable = Utils.detectConnectivityIssues(graph, equipment1);
        assertEquals(2, reachable.size());
        assertTrue(reachable.contains(equipment1));
        assertTrue(reachable.contains(equipment2));
        assertFalse(reachable.contains(equipment3));
    }

    @Test
    public void testDetectConnectivityIssues_SingleNode() {
        // Verificar que desde un solo equipo activo se pueda acceder solo a él mismo
        List<Equipment> reachable = Utils.detectConnectivityIssues(graph, equipment1);
        assertTrue(reachable.contains(equipment1));
        assertEquals(2, reachable.size()); // Porque también equipment2 está accesible
    }

    @Test
    public void testDetectConnectivityIssues_InactiveNode() {
        // Verificar que no se puede acceder a ningún nodo si el nodo inicial está inactivo
        equipment1.setStatus(false);

        List<Equipment> reachable = Utils.detectConnectivityIssues(graph, equipment1);
        assertTrue(reachable.contains(equipment1));  // Solo el nodo inicial porque está inactivo
        assertEquals(1, reachable.size());
    }
}


