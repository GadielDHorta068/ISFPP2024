package org.isfpp.main;

import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.datos.CargarParametros;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Web;

import java.io.IOException;

public class App implements NativeMouseInputListener {

    private Web web = null;
    private Utils utils;

    private MainMenu mainMenu;

    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        App app = new App();
        CargarParametros.parametros();
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

