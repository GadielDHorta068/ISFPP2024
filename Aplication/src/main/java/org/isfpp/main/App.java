package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.controller.Settings;
import org.isfpp.datos.CargarParametros;
import org.isfpp.datos.ResourceExtractor;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.interfaz.panelesEditadores.EditPortsFromEquipment;
import org.isfpp.interfaz.panelesPrincipal.LanguageSelectorPanel;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.modelo.LAN;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class App {
    // l�gica
    private LAN LAN = null;
    private CalculoGraph calculoGraph;

    // vista
    private MainMenu mainMenu;


    // controlador
    private Coordinator coordinator;
    private Settings settings;

    public static void main(String[] args) throws IOException, InterruptedException {
        ResourceExtractor.extractResourcesToExecutionDir();
        String directorio = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        System.out.println("Ruta absoluta: " + directorio);

        App app = new App();
        CargarParametros.parametros();
        app.inicio();
        app.launch();
        app.minitest();

    }

    private void inicio() throws InterruptedException {

        LAN =new LAN();
        settings=new Settings();
        coordinator = new Coordinator();
        calculoGraph =new CalculoGraph();
        mainMenu= new MainMenu();

        /* Se establecen las relaciones entre clases */
        settings.setCoordinador(coordinator);
        LAN.setCoordinator(coordinator);
        calculoGraph.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);


        /* Se establecen relaciones con la clase coordinador */
        coordinator.setSettings(settings);
        coordinator.setWeb(LAN);
        coordinator.setUtils(calculoGraph);
        coordinator.setMainMenu(mainMenu);
        coordinator.LoadData(coordinator.getWeb());
    }

    private void launch() throws InterruptedException {
        mainMenu.components(coordinator.getWeb());
    }

    private void minitest(){

    }
}