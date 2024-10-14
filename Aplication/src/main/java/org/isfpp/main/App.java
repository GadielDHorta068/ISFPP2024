package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.CargarParametros;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.PortType;
import org.isfpp.modelo.Web;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class App {
    // l�gica
    private Web web = null;
    private Utils utils;

    // vista
    private MainMenu mainMenu;

    // controlador
    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        App app = new App();
        CargarParametros.parametros();
        app.inicio();
        app.luanch();
        app.minitest2();
    }

    private List<Connection> la(){
    return         web.getConnections();
    }

    private void inicio(){
        web = Web.getWeb();
        coordinator = new Coordinator();
        utils=new Utils();
        mainMenu= new MainMenu();
        /* Se establecen las relaciones entre clases */
        web.setCoordinator(coordinator);
        utils.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);

        /* Se establecen relaciones con la clase coordinador */
        coordinator.setWeb(web);
        coordinator.setUtils(utils);
        coordinator.setMainMenu(mainMenu);
        coordinator.LoadData(coordinator.getWeb());
    }

    private void luanch(){
        mainMenu.components(coordinator.getWeb());
    }

    private String minitest(){
        String lala = "";
        try{
            File file = new File("test1.txt");

            if (file.createNewFile()){
                System.out.println("Archivo creado: "+ file.getName());
                System.out.println("Ruta: "+ file.getPath());
                lala = file.getPath();
                if (file.setWritable(true))
                    System.out.println("Permisos de escritura establecido");
                else
                    System.out.println("No se pudiero establecer permisos de escritura");
            }
            else {
                System.out.println("El archivo ya existe.");
            }
        } catch (IOException e) {
            System.err.println("Ocurrió un error al crear el archivo");
            e.printStackTrace();
        }
        return lala;
    }

    public void minitest2() {
    }
}
