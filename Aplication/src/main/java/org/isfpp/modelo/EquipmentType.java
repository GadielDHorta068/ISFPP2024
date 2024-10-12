package org.isfpp.modelo;

import java.util.Objects;

public class EquipmentType {
	private String code;
	private String description;

	public EquipmentType(){}

	public EquipmentType(String code, String description) {
		super();
		setCode(code);
		setDescription(description);
	}
	
	public String getCode() {return code;}
	
	public void setCode(String code) {this.code = code;}
	
	public String getDescription() {return description;}
	
	public void setDescription(String description) {this.description = description;}

	@Override
	public int hashCode() {return Objects.hash(code);}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof EquipmentType))
			return false;
		EquipmentType other = (EquipmentType) obj;
		return Objects.equals(code, other.code);
	}

	 @Override
    public String toString() {
        return "TipoEquipo{" +
                "codigo='" + code + '\'' +
                ", descripcion='" + description + '\'' +
                '}';
    }
}
