package org.isfpp.connection;

import java.util.Hashtable;
import java.util.ResourceBundle;

public class Factory {
    private static Hashtable<String, Object> instances = new Hashtable<String, Object>();

    public static Object getInstancia(String objName) {
        try {
            // verifico si existe un objeto relacionado a objName
            // en la hashtable
            Object obj = instances.get(objName);
            // si no existe entonces lo instancio y lo agrego
            if (obj == null) {
                ResourceBundle rb = ResourceBundle.getBundle("factory");
                String Classname = rb.getString(objName);
                obj = Class.forName(Classname).newInstance();
                // agrego el objeto a la hashtable
                instances.put(objName, obj);
            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
