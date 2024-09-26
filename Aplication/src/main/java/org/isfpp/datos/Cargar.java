package org.isfpp.datos;

import org.isfpp.modelo.Web;

public class Cargar {
    public static Web cargarRed(String nombre){
        Web red = new Web(nombre);

        //Varios io stream por archivo en orden para no generar una conexion a la nada si no estan los nodos




        return red;
    }
}
