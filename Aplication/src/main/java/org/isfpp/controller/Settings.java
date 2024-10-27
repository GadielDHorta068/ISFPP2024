package org.isfpp.controller;

import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.panelesPrincipal.LanguageSelectorPanel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Settings {

        private Coordinator coordinador;
        private ResourceBundle resourceBundle;
        private LanguageSelectorPanel ls;
        private String name;


        public Settings() {
            Properties prop = new Properties();
            InputStream input;
            try {
                input = Settings.class.getClassLoader().getResourceAsStream("config.properties");
                prop.load(input);

                CountDownLatch latch = new CountDownLatch(1);
                ls = new LanguageSelectorPanel(latch);
                latch.await();
           //     Locale.setDefault(new Locale(prop.getProperty("language"), prop.getProperty("country")));
                    Locale locale = LanguageSelectorPanel.getLoc();
              //  resourceBundle = ResourceBundle.getBundle(prop.getProperty("labels"));
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

    public static class ObtenerIdiomaSistema {
        public static void main(String[] args) {
            Locale locale = Locale.getDefault();
            String idioma = locale.getLanguage();
            String pais = locale.getCountry();

            System.out.println("Idioma del sistema: " + idioma);
            System.out.println("País del sistema: " + pais);
        }
    }


    public void setCoordinador(Coordinator coordinador) {
            this.coordinador = coordinador;
        }
        public String getName(){return name;}
 }


