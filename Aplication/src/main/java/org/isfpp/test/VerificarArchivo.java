package org.isfpp.test;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class VerificarArchivo {

    public static void main(String[] args) {
        String rutaArchivo = "C:\\Users\\gadie\\Documents\\ISFPP2024\\data\\equipo.txt";

        File archivo = new File(rutaArchivo);

        try {
            if (archivo.exists()) {
                System.out.println("El archivo fue encontrado.");
                // Verifica si se puede leer
                if (archivo.canRead()) {
                    System.out.println("El archivo se puede leer correctamente.");
                } else {
                    System.out.println("No tienes permisos para leer el archivo.");
                }

                // Intenta abrir el archivo para asegurarse de que Java puede acceder
                FileReader reader = new FileReader(archivo);
                System.out.println("El archivo se ha abierto correctamente.");
                reader.close();
            } else {
                System.out.println("Archivo no encontrado: " + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("Error al intentar abrir el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

