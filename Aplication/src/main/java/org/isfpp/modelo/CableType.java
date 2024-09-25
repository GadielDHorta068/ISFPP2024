package org.isfpp.modelo;

import java.util.Objects;

public class CableType {
    private String codigo;
    private String descripcion;
    private int velocidad;

    public CableType(String codigo, String descripcion, int velocidad) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.velocidad = velocidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    @Override
    public String toString() {
        return "CableType{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", velocidad=" + velocidad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CableType cableType = (CableType) o;
        return velocidad == cableType.velocidad && Objects.equals(codigo, cableType.codigo) && Objects.equals(descripcion, cableType.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, descripcion, velocidad);
    }
}
