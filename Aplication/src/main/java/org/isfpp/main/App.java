package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Web;

import java.io.IOException;

public class App {

    // l�gica
    private Web web;
    private Utils utils;

    // vista

    private MainMenu mainMenu;
    // controlador
    private Coordinator coordinator;

    public static void main(String[] args)  {
        Web web=null;
        try {
            web = Cargar.cargarRedDesdePropiedades("config.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Coordinator coordinator= new Coordinator();
        Utils utils=new Utils();
        MainMenu mainMenu= new MainMenu();
        /* Se establecen las relaciones entre clases */
        web.setCoordinator(coordinator);
        utils.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);


        /* Se establecen relaciones con la clase coordinador */
        coordinator.setWeb(web);
        coordinator.setUtils(utils);
        coordinator.setMainMenu(mainMenu);
        coordinator.LoadData(coordinator.getWeb());
        mainMenu.components(coordinator.getWeb());


    }

}
