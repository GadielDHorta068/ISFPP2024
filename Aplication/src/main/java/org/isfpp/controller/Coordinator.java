package org.isfpp.controller;

import org.isfpp.logica.Utils;
import org.isfpp.modelo.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Coordinator{
    private Web web;
    private Utils utils;


    public Utils getUtils() {
        return utils;
    }

    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    public Web getWeb() {
        return web;
    }

    public void setWeb(Web web) {
        this.web = web;
    }
    //web
    public HashMap<String, Equipment> getHardware() {
        return web.getHardware();
    }

    public void setHardware(HashMap<String, Equipment> hardware) {
        web.setHardware(hardware);    }

    public ArrayList<Connection> getConnections() {
        return web.getConnections();
    }

    public void setConnections(ArrayList<org.isfpp.modelo.Connection> conections) {
        this.web.setConnections(conections);
    }

    public HashMap<String, Location> getLocations() {
        return web.getLocations();
    }

    public void setLocations(HashMap<String, Location> locations) {
        this.web.setLocations(locations);
    }

    public String getNombre() {
        return web.getName();
    }

    public void setNombre(String nombre) {
        web.setName(nombre);
    }
    public Location addLocation(String code, String description) {return web.addLocation(code,description);
    }

    public void eraseLocation(Location l) { web.eraseLocation(l);}


    public PortType addPort(String code,String description,int speed){return web.addPort(code, description, speed);
    }
    public WireType addWire(String code,String description,int speed){return web.addWire(code, description, speed);
    }
    public EquipmentType addEquipmentType(String code,String description){ return  web.addEquipmentType(code, description);
    }
    public Equipment addEquipment(String code, String description, String marca, String model, PortType portType,int cantidad,
                                  EquipmentType equipmentType, Location location,Boolean status)  {
        return web.addEquipment(code, description, marca, model, portType, cantidad, equipmentType, location, status);
    }

    public void eraseEquipment(Equipment e) {web.eraseEquipment(e);
    }
    public void eraseWire(WireType w){web.eraseWire(w);


    }
    public void erasePort(PortType portType){web.erasePort(portType);
    }
    // Agregar una conexión entre dos equipos
    public Connection addConnection(Port port1, Port port2, WireType wire) {
        return web.addConnection(port1, port2, wire);
    }


    // Eliminar una conexión
    public void eraseConnection(org.isfpp.modelo.Connection connection) {web.eraseConnection(connection);}
    //Utils
    public HashMap<Equipment, Boolean> ping(){return utils.ping();}
    public boolean ping(Equipment e1){return utils.ping(e1);
    }
    public List<Equipment> detectConnectivityIssues(Equipment startNode) {
        return utils.detectConnectivityIssues(startNode);}
    public List<DefaultWeightedEdge> traceroute(Equipment e1, Equipment e2){return utils.traceroute(e1, e2);}


    public Graph<Equipment, Connection> getGraph() {
        return utils.getGraph();
    }

    public void setGraph(Graph<Equipment, Connection> graph) {
        this.utils.setGraph(graph);
    }
}
