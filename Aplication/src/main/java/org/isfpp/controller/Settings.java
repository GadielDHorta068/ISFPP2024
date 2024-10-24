package org.isfpp.controller;

import org.isfpp.datos.Cargar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Settings {

        private Coordinator coordinador;
        private ResourceBundle resourceBundle;


        public Settings() {
            Properties prop = new Properties();
            InputStream input;
            try {
                input = Settings.class.getClassLoader().getResourceAsStream("config.properties");
                prop.load(input);
           //     Locale.setDefault(new Locale(prop.getProperty("language"), prop.getProperty("country")));
                Locale locale = new Locale("es", "AR");
              //  resourceBundle = ResourceBundle.getBundle(prop.getProperty("labels"));
                resourceBundle = ResourceBundle.getBundle("messages", locale);

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
 }


