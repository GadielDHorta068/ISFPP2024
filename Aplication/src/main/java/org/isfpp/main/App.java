package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.CargarParametros;
import org.isfpp.datos.ResourceExtractor;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Web;

import java.io.IOException;

public class App {
    // l�gica
    private Web web = null;
    private Utils utils;

    // vista
    private MainMenu mainMenu;

    // controlador
    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        ResourceExtractor.extractResourcesToExecutionDir();
        App app = new App();
        CargarParametros.parametros();
        app.inicio();
        app.launch();
        app.minitest();

    }

    private void inicio(){
        web =new Web();
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

    private void launch(){
        mainMenu.components(coordinator.getWeb());
    }

    private void minitest(){
    }
}