package org.isfpp.modelo;

import java.util.Objects;

public class Location {
	private String code;
	private String description;

	public Location(){}

	public Location(String code, String description) {
		super();
		setCode(code);
		setDescription(description);
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
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}
	@Override
	public String toString() {
		return code + " \n" +
				 description;
	}
	
	
	

}
