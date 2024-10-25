package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.data.FileTextToBD;
import org.isfpp.datos.ResourceExtractor;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.modelo.Lan;

import java.io.File;
import java.io.IOException;

public class App {
    // l�gica
    private Lan lan = null;
    private CalculoGraph calculoGraph;

    //vista
    private MainMenu mainMenu;

    // controlador
    private Coordinator coordinator;

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.inicio();
        app.luanch();
        app.minitest();

    }

    private void inicio(){

        lan =new Lan();
        coordinator = new Coordinator();
        calculoGraph =new CalculoGraph();
        mainMenu= new MainMenu();
        /* Se establecen las relaciones entre clases */
        lan.setCoordinator(coordinator);
        calculoGraph.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);


        /* Se establecen relaciones con la clase coordinador */
        coordinator.setWeb(lan);
        coordinator.setUtils(calculoGraph);
        coordinator.setMainMenu(mainMenu);
        coordinator.LoadData(coordinator.getWeb());
    }

    private void luanch() {
        mainMenu.components(coordinator.getWeb());

    }

    private void minitest() {
    }

    private void fileTextToDB(){
        FileTextToBD.FileTextToBD();
    }
}

