package org.isfpp.controller;


import org.isfpp.interfaz.panelesPrincipal.LanguageSelectorPanel;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Configuracion del usuario
 */
public class Settings {

        private Coordinator coordinador;
        private ResourceBundle resourceBundle;
        private LanguageSelectorPanel ls;
        private String name;


    /**
     * Constructor por defecto de la clase
     *
     */
    public Settings() {
            Properties prop = new Properties();
            InputStream input;
            try {
                input = Settings.class.getClassLoader().getResourceAsStream("config.properties");
                prop.load(input);

                CountDownLatch latch = new CountDownLatch(1);
                ls = new LanguageSelectorPanel(latch);
                latch.await();
                Locale locale = LanguageSelectorPanel.getLoc();
                resourceBundle = ResourceBundle.getBundle("messages", locale);
                name = ls.getName();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        public ResourceBundle getResourceBundle() {
            return resourceBundle;
        }


    public void setCoordinador(Coordinator coordinador) {
            this.coordinador = coordinador;
        }
        public String getName(){return name;}
 }


