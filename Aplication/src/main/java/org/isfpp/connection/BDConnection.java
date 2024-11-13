package org.isfpp.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *  Clase que se encargara de establecer la conexion con la base de datos
 */
public class BDConnection {
    private static java.sql.Connection con = null;

    /**
     * Clase que devuelve los atributos de la conexion
     * @return Connection
     */
    public static Connection getConnection(){
        try {
            if (con == null){
                Runtime.getRuntime().addShutdownHook(new MishDwnHook());
                ResourceBundle rb = ResourceBundle.getBundle("jdbc");
                String driver = rb.getString("driver");
                String url = rb.getString("url");
                String user = rb.getString("user");
                String password = rb.getString("password");
                Class.forName(driver);
                con = DriverManager.getConnection(url,user,password);
            }
            return con;
        } catch (ClassNotFoundException e) {
            System.err.println("Error al crear la connexion");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("connexion con la base de datos no establecida");
            throw new RuntimeException(e);
        }
    }

    private static class MishDwnHook extends Thread {
        public void run() {
            try {
                Connection con = BDConnection.getConnection();
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }
}
