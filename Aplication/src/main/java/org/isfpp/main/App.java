package org.isfpp.main;

import org.isfpp.controller.Coordinator;
import org.isfpp.controller.Settings;
import org.isfpp.dao.Secuencial.EquipmentSequentialDAO;
import org.isfpp.data.FileTextToBD;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.logica.CalculoGraph;
import org.isfpp.logica.Subject;
import org.isfpp.modelo.Equipment;
import org.isfpp.logica.Lan;

import java.io.IOException;

public class App {
    // l�gica
    private Lan lan = null;
    private CalculoGraph calculoGraph;
    private Subject subject;
    // vista
    private MainMenu mainMenu;


    // controlador
    private Coordinator coordinator;
    private Settings settings;

    public static void main(String[] args) throws IOException {
        App app = new App();
        app.inicio();
        app.launch();
        app.minitest();

    }

    private void inicio(){
        lan = Lan.getLan();
        subject = new Subject();
        settings=new Settings();
        coordinator = new Coordinator();
        calculoGraph =new CalculoGraph();
        mainMenu= new MainMenu();

        /* Se establecen las relaciones entre clases */
        settings.setCoordinador(coordinator);
        lan.setCoordinator(coordinator);
        calculoGraph.setCoordinator(coordinator);
        mainMenu.SetCoordinator(coordinator);


        /* Se establecen relaciones con la clase coordinador */
        coordinator.setSettings(settings);
        coordinator.setWeb(lan);
        coordinator.setUtils(calculoGraph);
        coordinator.setMainMenu(mainMenu);
        lan.init(subject);
        calculoGraph.init(subject);
        coordinator.LoadData(coordinator.getWeb());
    }

    private void launch(){
        mainMenu.components(coordinator.getWeb());
    }

    private void minitest(){
        EquipmentSequentialDAO dad = new EquipmentSequentialDAO();
        for (Equipment equipment: dad.searchAll().values())
            System.out.println(equipment.getCode());
    }

    private void fileTextToDB(){
        FileTextToBD.FileTextToBD();
    }
}