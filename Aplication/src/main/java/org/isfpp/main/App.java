package org.isfpp.main;

import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.modelo.LAN;

import java.io.IOException;

public class App {
    // l�gica
    private LAN LAN = null;
    private CalculoGraph calculoGraph;

    //vista
    private MainMenu mainMenu;

    // controlador
    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        ResourceExtractor.extractResourcesToExecutionDir();
        String directorio = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
        System.out.println("Ruta absoluta: " + directorio);

        App app = new App();
        CargarParametros.parametros();
        app.inicio();
        app.luanch();
        app.minitest();

    }

    private void inicio(){
        LAN =new LAN();
        coordinator = new Coordinator();
        calculoGraph =new CalculoGraph();
        mainMenu= new MainMenu();
        /* Se establecen las relaciones entre clases */
        LAN.setCoordinator(coordinator);
        calculoGraph.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);


        /* Se establecen relaciones con la clase coordinador */
        coordinator.setWeb(LAN);
        coordinator.setUtils(calculoGraph);
        coordinator.setMainMenu(mainMenu);
        coordinator.LoadData(coordinator.getWeb());
    }

    private void luanch() {
        mainMenu.components(coordinator.getWeb());

    }

    private void minitest() {
    }
}

