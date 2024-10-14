package org.isfpp.main;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.panelesCreadores.MainMenu;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Web;

import java.io.IOException;

public class App implements NativeMouseInputListener{

    public static void main(String[] args) {
        Web web;
        try {
            web = Cargar.cargarRedDesdePropiedades("config.properties");
            GlobalScreen.registerNativeHook();
        } catch (IOException | NativeHookException e) {
            throw new RuntimeException(e);
        }


        GlobalScreen.addNativeMouseListener(new App());
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
