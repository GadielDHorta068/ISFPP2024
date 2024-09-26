package org.isfpp.interfaz;

import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BarraMenu {
    public JMenuBar crearBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        StylusUI.styleMenuBar(menuBar);

        JMenu archivoMenu = new JMenu("Archivo");
        StylusUI.styleMenu(archivoMenu);
        JMenuItem cargarItem = new JMenuItem("Cargar");
        StylusUI.styleMenuItem(cargarItem);
        JMenuItem guardarItem = new JMenuItem("Guardar");
        StylusUI.styleMenuItem(guardarItem);
        JMenuItem salirItem = new JMenuItem("Salir");
        StylusUI.styleMenuItem(salirItem);
        // Añadir ActionListeners a los elementos del menú
        cargarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para cargar
                System.out.println("Cargar seleccionado");
            }
        });

        guardarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para guardar
                System.out.println("Guardar seleccionado");
            }
        });

        salirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para salir
                System.exit(0);
            }
        });

        archivoMenu.add(cargarItem);
        archivoMenu.add(guardarItem);
        archivoMenu.add(salirItem);

        menuBar.add(archivoMenu);

        JMenu editarMenu = new JMenu("Editar");
        StylusUI.styleMenu(editarMenu);
        JMenu ayudaMenu = new JMenu("Ayuda");
        StylusUI.styleMenu(ayudaMenu);
        menuBar.add(editarMenu);
        menuBar.add(ayudaMenu);

        return menuBar;
    }
}