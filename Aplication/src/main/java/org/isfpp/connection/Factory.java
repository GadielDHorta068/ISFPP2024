package org.isfpp.connection;

import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Clase factory
 */
public class Factory {
    private static final Hashtable<String, Object> instances = new Hashtable<String, Object>();

    /**
     * Instanciar un objeto depende de su nombre
     * @param objName Nombre del item a instanciar
     * @return Object
     */
    public static Object getInstancia(String objName) {
        try {
            Object obj = instances.get(objName);
            if (obj == null) {
                ResourceBundle rb = ResourceBundle.getBundle("factory");
                String Classname = rb.getString(objName);
                obj = Class.forName(Classname).newInstance();
                instances.put(objName, obj);
            }
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
