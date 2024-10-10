package org.isfpp.modelo;

import java.util.Objects;

public class PortType {
	private String code;
	private String description;
	private int speed;

	public PortType(){}

	public PortType(String code, String description, int speed) {
		super();
		setCode(code);
		setDescription(description);
		setSpeed(speed);
	}
	
	public String getCode() {return code;}
	
	public void setCode(String code) {this.code = code;}
	
	public String getDescription() {return description;}
	
	public void setDescription(String description) {this.description = description;}
	
	public int getSpeed() {return speed;}
	
	public void setSpeed(int speed) {this.speed = speed;}

	@Override
	public int hashCode() {return Objects.hash(code);}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PortType))
			return false;
		PortType other = (PortType) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return String.format("%s \n%s \n%s",
				code,
				description,
				speed
		);
	}

}
