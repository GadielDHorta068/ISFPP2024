package org.isfpp.modelo;

import java.util.Objects;

/**
 * Clase que representa una conexión entre dos puertos mediante un tipo de cable.
 */
public class Connection {
    private WireType wire;
    private Port port1;
    private Port port2;

    /**
     * Constructor por defecto de Connection.
     */
	public Connection(){}

    /**
     * Constructor de Connection que inicializa los puertos y el tipo de cable.
     *
     * @param port1 el primer puerto en la conexión
     * @param port2 el segundo puerto en la conexión
     * @param wire el tipo de cable que conecta los puertos
     * @throws IllegalArgumentException si algún puerto está en uso o ambos puertos pertenecen al mismo equipo
     */
	public Connection(Port port1, Port port2, WireType wire) {
		super();

        // Verificamos que ambos puertos no estén en uso
		if (port1.isInUse()) {
			throw new IllegalArgumentException("El puerto 1 ya está en uso");
		}
		if (port2.isInUse()) {
			throw new IllegalArgumentException("El puerto 2 ya está en uso");
		}

        // Verificamos que los puertos no pertenezcan al mismo equipo
		if (port1.getEquipment().equals(port2.getEquipment())) {
			throw new IllegalArgumentException("Ambos puertos pertenecen al mismo equipo");
		}

        // Configuramos los puertos y marcamos como en uso
		setPort1(port1);
		port1.setInUse(true);
		setPort2(port2);
		port2.setInUse(true);



        // Configuramos el tipo de cable
		setWire(wire);
	}

    /**
     * Obtiene el tipo de cable utilizado en la conexión.
     *
     * @return el tipo de cable
     */
	public WireType getWire() {return wire;}

    /**
     * Configura el tipo de cable utilizado en la conexión.
     *
     * @param wire el nuevo tipo de cable
     */
	public void setWire(WireType wire) {this.wire = wire;}

    /**
     * Obtiene el primer puerto en la conexión.
     *
     * @return el primer puerto
     */
	public Port getPort1() {return port1;}
	
    /**
     * Configura el primer puerto en la conexión.
     *
     * @param port1 el nuevo primer puerto
     */
	public void setPort1(Port port1) {this.port1 = port1;}

    /**
     * Obtiene el segundo puerto en la conexión.
     *
     * @return el segundo puerto
     */
	public Port getPort2() {return port2;}
	
    /**
     * Configura el segundo puerto en la conexión.
     *
     * @param port2 el nuevo segundo puerto
     */
	public void setPort2(Port port2) {this.port2 = port2;}

    /**
     * Calcula el código hash de la conexión basado en los puertos.
     *
     * @return el código hash
     */
	@Override
	public int hashCode() {
		return Objects.hash(port1, port2);
	}

    /**
     * Compara esta conexión con otro objeto para verificar si son iguales.
     *
     * @param obj el objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Connection other))
			return false;
        return (port1.equals(other.port1) && port2.equals(other.port2))
                || (port1.equals(other.port2) && port2.equals(port1));
    }

    /**
     * Retorna una representación en cadena de la conexión.
     *
     * @return una cadena que representa la conexión
     */
	@Override
	public String toString() {
		return wire +"\n"+port1.toString()+"\n"+port2.toString();
	}
}