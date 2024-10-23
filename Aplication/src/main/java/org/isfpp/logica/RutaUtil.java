package org.isfpp.logica;
import java.io.File;
import java.util.Properties;

public class RutaUtil {

    // Método que acomoda las barras de la ruta al formato adecuado
    public static String corregirRuta(String directorio, String rutaArchivo) {
        // Asegura que ambas partes de la ruta utilicen el separador correcto
        // Elimina cualquier barra al final del directorio, si existe
        if (directorio.endsWith(File.separator)) {
            directorio = directorio.substring(0, directorio.length() - 1);
        }

        // Reemplaza las barras incorrectas en la ruta del archivo
        rutaArchivo = rutaArchivo.replace("/", File.separator).replace("\\", File.separator);

        // Combina el directorio y la ruta del archivo usando el separador correcto
        return directorio + File.separator + rutaArchivo;
    }

    public static void main(String[] args) {
        // Supongamos que el archivo properties tiene esta entrada:
        Properties prop = new Properties();
        prop.setProperty("rs.equipment", "data/equipo.txt");

        // Ejemplo de uso del método corregirRuta
        String directorio = "C:\\Users\\gadie\\Documents\\ISFPP2024";
        String rutaArchivo = prop.getProperty("rs.equipment");

        // Obtenemos la ruta corregida
        String rutaCorregida = corregirRuta(directorio, rutaArchivo);

        // Mostramos la ruta corregida
        System.out.println("Ruta corregida: " + rutaCorregida);

        // Verificamos si el archivo existe
        File archivo = new File(rutaCorregida);
        if (archivo.exists()) {
            System.out.println("El archivo fue encontrado.");
        } else {
            System.out.println("Archivo no encontrado: " + archivo.getAbsolutePath());
        }
    }
}
