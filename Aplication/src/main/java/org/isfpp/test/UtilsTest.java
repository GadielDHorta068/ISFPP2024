package org.isfpp.test;

import org.isfpp.datos.Cargar;
import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.isfpp.logica.Utils;


import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
    private Web w1;
    private Graph<Equipment, Connection> graph;
    private Equipment equipment1;
    private Equipment equipment2;
    private Equipment equipment3;
    private Equipment equipment4;

    private Connection connection1;
    private Connection connection2;
    private Connection connection3;
    private Connection connection4;
    Utils u1;

    @BeforeEach
    public void setUp() {
        w1=new Web("testWeb");

        // Creación de objetos PortType
        PortType p1000 =  w1.addPort("MIDp", "Ethernet Port", 1000); // 1 Gbps
        PortType p10000 = w1.addPort("HIGHp", "Fiber Optic Port", 10000); // 10 Gbps
        PortType p300 = w1.addPort("LOWp", "Wi-Fi Port", 300); // 300 Mbps
        // Crear un nuevo tipo de equipo
        EquipmentType equipmentType1 = w1.addEquipmentType("ET1", "Router");
        EquipmentType equipmentType2 = w1.addEquipmentType("ET2", "Switch");
        EquipmentType equipmentType3 = w1.addEquipmentType("ET3", "Access Point");


        // localizaciones

        Location location1 = w1.addLocation("1","Sala de Servidores");
        Location location2 = w1.addLocation("2","Sala de Conferencias");
        Location location3 = w1.addLocation("3","Auditorio");


        equipment1 = w1.addEquipment("001", "Router Principal", "Cisco", "RV340", p300, 5,
                equipmentType1, location1, true);
        equipment1.addPort(p10000);

        equipment2 = w1.addEquipment("002", "PC de Reuniones", "HP", "Pavilion", p300, 10,
                equipmentType2, location2, true);
        equipment2.addPort(p1000);

        equipment3 = w1.addEquipment("003", "Proyector de Presentaciones", "Epson", "PowerLite", p10000, 2,
                equipmentType3, location3, true);
        equipment3.addPort(p1000);

        equipment4 = w1.addEquipment("003", "Proyector de Presentaciones", "Epson", "PowerLite", p10000, 2,
                equipmentType3, location3, true);

        WireType w10000 = w1.addWire("HIGHw", "Cable de fibra óptica", 10000); // Velocidad en Mbps
        WireType w10 = w1.addWire("MIDw", "Cable de cobre oxidado", 10); // Velocidad en Mbps

        // Crear conexiones
        connection1 = w1.addConnection(equipment1.checkPort(p300),equipment2.checkPort(p300),w10000);  // Activa
        connection2 = w1.addConnection(equipment2.checkPort(p1000),equipment3.checkPort(p1000), w10);  // Entre equipo activo e inactivo

        connection3 = w1.addConnection(equipment1.checkPort(p300),equipment4.checkPort(p300),w10000);  // Activa
        connection4 = w1.addConnection(equipment4.checkPort(p10000),equipment3.checkPort(p10000), w10000);  // Entre equipo activo e inactivo

        u1=new Utils();
        u1.LoadData(w1);




    }
    @Test
    void testTraceroute_ThrowsException_ForSameEquipment() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {u1.traceroute(equipment1, equipment1);});  // Testeando con equipos iguales

        assertEquals("Equipo duplicado", exception.getMessage());
    }

    @Test
    void testTraceroute_ThrowsException_ForInactiveEquipment() {
        equipment1.setStatus(false); //simula que el primer equipo esta inactivo

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {u1.traceroute(equipment1, equipment2);});  // Testeando con un equipo inactivo

        assertTrue(exception.getMessage().contains("Uno de los equipos no está activo"));
    }

    @Test
    void testTracerouteFindsShortestPath() {
        int minSpeed;
        List<DefaultWeightedEdge> connectionList = u1.traceroute(equipment1,equipment3);
        assertEquals(connectionList.size(),2);
        //velocidad minima de la conexion
        minSpeed = Math.min(connection3.getPort1().getPortType().getSpeed(),connection3.getPort2().getPortType().getSpeed());
        minSpeed = Math.min(minSpeed, connection3.getWire().getSpeed());
        assertEquals(connectionList.get(1), minSpeed);
        //velocidad minima de la conexion
        minSpeed = Math.min(connection3.getPort1().getPortType().getSpeed(),connection3.getPort2().getPortType().getSpeed());
        minSpeed = Math.min(minSpeed, connection3.getWire().getSpeed());
        assertEquals(connectionList.get(2),minSpeed);

        equipment4.setStatus(false);
        connectionList = u1.traceroute(equipment1,equipment3);
        assertEquals(connectionList.size(),2);

        minSpeed = Math.min(connection1.getPort1().getPortType().getSpeed(),connection1.getPort2().getPortType().getSpeed());
        minSpeed = Math.min(minSpeed, connection1.getWire().getSpeed());
        assertEquals(connectionList.get(1),minSpeed);

        minSpeed = Math.min(connection2.getPort1().getPortType().getSpeed(),connection2.getPort2().getPortType().getSpeed());
        minSpeed = Math.min(minSpeed, connection2.getWire().getSpeed());
        assertEquals(connectionList.get(2),minSpeed);
    }

    @Test
    void testTraceroute_NotFoundPath() {
        equipment4.setStatus(false);
        equipment2.setStatus(false);
        List<DefaultWeightedEdge> connectionList = u1.traceroute(equipment1,equipment3);
        assertNull(connectionList);
    }
}


