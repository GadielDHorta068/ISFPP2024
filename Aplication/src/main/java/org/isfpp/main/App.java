package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Web;

import java.io.IOException;

public class App {

    private Web web = null;
    private Utils utils;

    private MainMenu mainMenu;

    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.inicio();
        app.luanch();
        app.minitest();

    }

    private void inicio() {
        web = Web.getWeb();
        coordinator = new Coordinator();
        utils = new Utils();
        mainMenu = new MainMenu();
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

    private void luanch() {
        mainMenu.components(coordinator.getWeb());

    }

    private void minitest() {
    }
}

