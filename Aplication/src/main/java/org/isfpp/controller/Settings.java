package org.isfpp.controller;

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
                input = new FileInputStream("config.properties");
                prop.load(input);
                Locale.setDefault(new Locale(prop.getProperty("language"), prop.getProperty("country")));
                resourceBundle = ResourceBundle.getBundle(prop.getProperty("labels"));

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


