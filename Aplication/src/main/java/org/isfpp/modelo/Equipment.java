package org.isfpp.modelo;

import org.isfpp.exceptions.AlreadyExistException;
import org.isfpp.exceptions.NotFoundException;

import java.util.*;

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

    public Equipment(){
        this.ipAdresses = new ArrayList<String>();
        this.ports = new ArrayList<Port>();}

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

		this.ipAdresses = new ArrayList<String>();
		this.ports = new ArrayList<Port>();

        for (int i = 0; i<portCapacity; i++)
            this.addPort(portType);
	}

	public static String generarMAC() {
		Random random = new Random();
		byte[] macAddr = new byte[6];
		random.nextBytes(macAddr);

        StringBuilder macAddress = new StringBuilder(18);
        for (byte b : macAddr) {
            if (!macAddress.isEmpty()) {
                macAddress.append(":");
            }
            macAddress.append(String.format("%02x", b));
        }
        return macAddress.toString().toUpperCase();
    }


    public void addIp(String ip) {
        // llamo al metodo check de ip
        if (ipAdresses.contains(ip))
            throw new AlreadyExistException("la ip ya se encuentra");
        ipAdresses.add(ip);

    }

    public void deleteIp(String ip) {
        if (!ipAdresses.contains(ip))
            throw new NotFoundException("la ip no se encuentra");
        ipAdresses.remove(ip);

    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void addPort(PortType portType) {
        Port p = new Port(portType, this);
        this.ports.add(p);
    }

    public void deletePort(PortType port) {
        if (!ports.contains(port))
            throw new AlreadyExistException("ese puerto ya esta en la lista de puertos del equipo");
        ports.remove(port);

    }

    public String getCode () {
        return code;
    }

    public void setCode (String code){
        this.code = code;
    }

    public String getDescription () {
        return description;
    }

    public void setDescription (String description){
        this.description = description;
    }

    public String getMake () {
        return make;
    }

    public void setMake (String make){
        this.make = make;
    }

    public String getModel () {
        return model;
    }

    public void setModel (String model){
        this.model = model;
    }

    public List<String> getIpAdresses () {
        return ipAdresses;
    }

    public void setIpAdresses (List < String > ipAdresses) {
        this.ipAdresses = ipAdresses;
    }

    public List<Port> getPorts () {
        return ports;
    }

    public void setPorts (List < Port > ports) {
        this.ports = ports;
    }

    public EquipmentType getEquipmentType () {
        return equipmentType;
    }

    public void setEquipmentType (EquipmentType equipmentType){
        this.equipmentType = equipmentType;
    }

    public Location getLocation () {
        return location;
    }

    public void setLocation (Location location){
        this.location = location;
    }

    /**
     * Retorna un mapa con donde se hace un recuento de la cantidad de puertos que hay, segun su tipo.
     * @return Retorna un mapa donde la Clave es el tipo de puerto y el Valor es la cantidad estos que tiene el equipo.
     */
    public HashMap<PortType, Integer> getAllPortsTypes() {
        HashMap<PortType, Integer> portTypes = new HashMap<>();
        for (Port port : getPorts())
            if (portTypes.containsKey(port.getPortType()))
                portTypes.put(port.getPortType(), countPort(port.getPortType()));

        return portTypes;
    }

    /**
     * Hace un conteo de los puertos que sean del tipo dado.
     * @param portType tipo de puerto requerido.
     * @return retorna la cantidad de los puertos que sean de el mismo tipo del que se busca.
     */
    public int countPort (PortType portType){
        int count = 0;
        for (Port port : getPorts())
            if (port.getPortType().getCode().equals(portType.getCode()))
                count++;
        return count;
    }

    /**
     * Checkea si el existe un puerto del tipo dado disponible.
     * @param portType tipo de puerto requerido
     * @return retorna el primer puerto disponible encontrado y que sea del tipo requerido;
     */
    public Port checkPort(PortType portType) {
        Port portCatch = null;
        for (int i = 0; portCatch == null && i < getPorts().size();i++)
            if (!getPorts().get(i).isInUse() && getPorts().get(i).getPortType().equals(portType))
                portCatch = getPorts().get(i);

        return portCatch;
    }


    @Override
    public int hashCode () {
        return Objects.hash(code);
    }

    @Override
    public boolean equals (Object obj){
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
    public String toString () {
        return code + "\n"
                + description + "\n"
                + make + "\n"
                +  model + "\n"
                + ipAdresses + "\n"
                +  location.getDescription();

    }
}
