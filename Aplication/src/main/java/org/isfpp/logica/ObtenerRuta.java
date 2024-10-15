package org.isfpp.logica;

import java.io.File;
import java.io.IOException;

public class ObtenerRuta {
    public static String obtenerRutaBase() {
        try {
            // Obtiene la ruta desde donde se ejecuta el archivo Java
            String rutaEjecucion = new File(System.getProperty("user.dir")).getCanonicalPath();

            // Verifica si la ruta contiene una subcarpeta "target"
            if (rutaEjecucion.contains("target")) {
                // Elimina "target" de la ruta
                rutaEjecucion = rutaEjecucion.substring(0, rutaEjecucion.indexOf("target"));
            }

            // Asegura que la ruta termine con una barra '/'
            if (!rutaEjecucion.endsWith(File.separator)) {
                rutaEjecucion += File.separator;
            }

            return rutaEjecucion;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void main(String[] args) {
        // Obtiene la ruta base corregida
        String rutaBase = obtenerRutaBase();

        if (rutaBase != null) {
            // Ejemplo de uso con un archivo de texto
            String rutaArchivo = rutaBase + "/data/equipo.txt";

            System.out.println("Ruta absoluta del archivo: " + rutaArchivo);
        } else {
            System.out.println("No se pudo obtener la ruta.");
        }
    }
}
