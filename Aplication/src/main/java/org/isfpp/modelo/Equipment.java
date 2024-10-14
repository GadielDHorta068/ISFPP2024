package org.isfpp.modelo;

import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Esta clase representa un equipo en el sistema.
 * Cada equipo tiene un código, descripción, marca, modelo, direcciones IP, puertos, tipo de equipo, ubicación y estado.
 */
public class Equipment {
    private String code;
    private String description;
    private String make;
    private String model;
    private List<String> ipAdresses;
    private List<Port> ports;
    private EquipmentType equipmentType;
    private Location location;
    private boolean status;

    public Equipment() {
        this.ipAdresses = new ArrayList<>();
        this.ports = new ArrayList<>();
    }

    /**
     * Constructor con parámetros.
     *
     * @param code          Código del equipo.
     * @param description   Descripción del equipo.
     * @param make          Marca del equipo.
     * @param model         Modelo del equipo.
     * @param portType      Tipo de puerto.
     * @param portCapacity  Capacidad del puerto.
     * @param equipmentType Tipo de equipo.
     * @param location      Ubicación del equipo.
     * @param status        Estado del equipo.
     */
    public Equipment(String code, String description, String make, String model, PortType portType, int portCapacity,
                     EquipmentType equipmentType, Location location, boolean status) {
        super();
        setCode(code);
        setDescription(description);
        setMake(make);
        setModel(model);
        setEquipmentType(equipmentType);
        setLocation(location);
        setStatus(status);

        this.ipAdresses = new ArrayList<>();
        this.ports = new ArrayList<>();

        for (int i = 0; i < portCapacity; i++)
            this.addPort(portType);
    }

    /**
     * Agregar una dirección IP al equipo.
     *
     * @param ip Dirección IP a agregar.
     */
    public void addIp(String ip) {
        if (ipAdresses.contains(ip))
            throw new AlreadyExistException("la ip ya se encuentra");
        ipAdresses.add(ip);
    }

    /**
     * Eliminar una dirección IP del equipo.
     *
     * @param ip Dirección IP a eliminar.
     */
    public void deleteIp(String ip) {
        if (!ipAdresses.contains(ip))
            throw new NotFoundException("la ip no se encuentra");
        ipAdresses.remove(ip);
    }

    /**
     * Establecer el estado del equipo.
     *
     * @param status Estado del equipo.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Obtener el estado del equipo.
     *
     * @return Estado del equipo.
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Agregar un puerto al equipo.
     *
     * @param portType Tipo de puerto.
     */
    public void addPort(PortType portType) {
        Port p = new Port(portType, this);
        ports.add(p);
    }

    /**
     * Eliminar un puerto del equipo.
     *
     * @param port Puerto a eliminar.
     */
    public void deletePort(Port port) {
        if (!ports.contains(port))
            throw new NotFoundException("El puerto no se encuentra en la lista de puertos del equipo");
        ports.remove(port);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getIpAdresses() {
        return ipAdresses;
    }

    public void setIpAdresses(List<String> ipAdresses) {
        this.ipAdresses = ipAdresses;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Retorna un mapa con donde se hace un recuento de la cantidad de puertos que hay, según su tipo.
     *
     * @return Retorna un mapa donde la clave es el tipo de puerto y el valor es la cantidad estos que tiene el equipo.
     */
    public HashMap<PortType, Integer> getAllPortsTypes() {
        HashMap<PortType, Integer> portTypes = new HashMap<>();
        for (Port port : getPorts()) {
            portTypes.put(port.getPortType(), portTypes.getOrDefault(port.getPortType(), 0) + 1);
        }
        return portTypes;
    }

    /**
     * Hace un conteo de los puertos que sean del tipo dado.
     *
     * @param portType Tipo de puerto requerido.
     * @return Retorna la cantidad de puertos que sean del mismo tipo del que se busca.
     */
    public int countPort(PortType portType) {
        int count = 0;
        for (Port port : getPorts())
            if (port.getPortType().getCode().equals(portType.getCode()))
                count++;
        return count;
    }

    /**
     * Checkea si existe un puerto del tipo dado disponible.
     *
     * @param portType Tipo de puerto requerido.
     * @return Retorna el primer puerto disponible encontrado que sea del tipo requerido.
     */
    public Port checkPort(PortType portType) {
        Port portCatch = null;
        for (int i = 0; portCatch == null && i < getPorts().size(); i++)
            if (!getPorts().get(i).isInUse() && getPorts().get(i).getPortType().equals(portType))
                portCatch = getPorts().get(i);

        return portCatch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Equipment other = (Equipment) obj;
        return Objects.equals(code, other.code);
    }

    @Override
    public String toString() {
        return String.format(
                "Código: %s\n\n Descripción: %s\n\n Marca: %s\n\n Modelo: %s\n\n Direcciones IP: %s\n\n Tipo de Equipo: %s\n\n Ubicación: %s\n\n Estado: %s\n\n Puertos: %d",
                code,
                description,
                make,
                model,
                String.join(", ", ipAdresses),
                equipmentType.getDescription(),
                location.getDescription(),
                status ? "Activo" : "Inactivo",
                ports.size()
        );
    }

    /**
     * Obtiene una lista de puertos que no están en uso.
     *
     * @return Retorna una lista de puertos no están en uso.
     */
    public List<Port> getPortsNotInUse() {
        return getPorts().stream()
                .filter(port -> !port.isInUse())
                .collect(Collectors.toList());
    }

}