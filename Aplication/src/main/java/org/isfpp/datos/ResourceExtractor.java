package org.isfpp.datos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ResourceExtractor {
    /**
     * Extrae los archivos de recursos al directorio de ejecucion.
     */
    public static void extractResourcesToExecutionDir() {
        // Obtiene el directorio desde donde se estÃ¡ ejecutando el jar
        String executionDir = new File("").getAbsolutePath();

        // Lista de archivos a extraer
        String[] filesToExtract = { "data/equipo.txt", "data/conexion.txt", "data/tipoCable.txt", "data/tipoEquipo.txt", "data/tipoPuerto.txt", "data/ubicacion.txt" };

        // Extrae cada archivo
        for (String file : filesToExtract) {
            try (InputStream in = Objects.requireNonNull(ResourceExtractor.class.getClassLoader().getResourceAsStream(file))) {
                // AsegÃºrate de que el directorio de destino existe
                File targetFile = new File(executionDir, file);
                targetFile.getParentFile().mkdirs();

                // Copia el archivo al directorio de ejecuciÃ³n

                Files.copy(in, Paths.get(executionDir, file), StandardCopyOption.REPLACE_EXISTING); //, StandardCopyOption.REPLACE_EXISTING
                System.out.println("Archivo extraido: " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}