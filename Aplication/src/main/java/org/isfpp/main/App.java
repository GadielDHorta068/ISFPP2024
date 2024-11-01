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
import org.isfpp.modelo.PortType;

import java.io.IOException;
import java.util.stream.Collectors;

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
        Equipment equipment = new Equipment();
        PortType p1 = new PortType("KKK", "JAJAJA", 100);
        PortType p2 = new PortType("LLL", "JIJIJI", 1000);
        equipment.addPort(p1);
        equipment.addPort(p2);
        equipment.addIp("100.11.1.1");
        equipment.addIp("103.11.1.1");
        equipment.addIp("124.11.1.1");

        equipment.setCode("FAFA");

        String insertsPortFormat = equipment.getAllPortsTypes().entrySet().stream()
                .map(entry -> String.format("INSERT INTO poo2024.RCG_equipment_port (cantidad, code_port_type, code_equipment) VALUES %d, '%s', '%s'",
                        entry.getValue(), entry.getKey().getCode(),equipment.getCode()))
                .collect(Collectors.joining(";"));

        String insertsIpFormat = equipment.getIpAdresses().stream()
                .map(ip -> String.format("INSERT INTO poo2024.RCG_equipment_ips (code_equipment, ip) VALUES '%s', '%s'::INET", equipment.getCode(), ip))
                .collect(Collectors.joining(";"));


        String sql = "" +
                "  INSERT INTO poo2024.RCG_equipment (code," +
                "   description, " +
                "   marca, " +
                "   modelo, " +
                "   code_equipment_type, " +
                "   code_location, " +
                "   status" +
                ") " +
                "  VALUES (?, ?, ?, ?, ?, ?, ?) " +
                ";" +
                //parte de la consulta sobre los puertos, hace una UNION ALL sobre todos los elementos del puerto
                insertsPortFormat+
                //parte de la consulta de las ips, hace lo mismo que con los puertos, pero usando la lista de puertos
                insertsIpFormat;

        System.out.println("codigo equipo: "+equipment.getCode()+"\n"+sql);
    }

    private void fileTextToDB(){
        FileTextToBD.fileTextToBD();
    }
}