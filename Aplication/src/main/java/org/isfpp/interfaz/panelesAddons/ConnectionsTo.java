package org.isfpp.interfaz.panelesAddons;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Panel que mostrara el alcanze del equipo elegido
 */
public class ConnectionsTo {
    private JFrame frame;
    private JTextArea textArea;
    private List<Equipment> direcciones;
    private Coordinator coordinator;
    private ResourceBundle rb;

    /**
     * Constructor por defecto
     */
    public ConnectionsTo() {
    }

    /**
     * Metodo que dibujara el area de texto con el alcanze del equipo a analizar
     */
    public void scanIp() {
        this.rb=coordinator.getResourceBundle();
        direcciones = new ArrayList<>();
        frame = new JFrame(rb.getString("alcanze_equipo"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        JTextField ipInicial = new JTextField(rb.getString("ip_escanear"));
        String defecto = getIPSeleccionada();
        System.out.println(defecto);
        if (!Objects.equals(defecto, "0")) {
            ipInicial.setText(defecto);
        }

        StylusUI.aplicarEstiloCampoTexto(ipInicial);

        ipInicial.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ipInicial.getText().equals(rb.getString("ip_escanear"))) {
                    ipInicial.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ipInicial.getText().isEmpty()) {
                    ipInicial.setText(rb.getString("ip_escanear"));
                }
            }
        });

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton scanButton = new JButton(rb.getString("conexiones"));
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direcciones.clear();
                try {
                    textArea.setText("");
                    String equipo = ipInicial.getText().toUpperCase();
                    direcciones = coordinator.detectConnectivityIssues(coordinator.getHardware().get(equipo));
                    updateTextArea();
                } catch (Exception exception) {
                    textArea.setText("");
                    ipInicial.setText("");
                    JOptionPane.showMessageDialog(frame, exception.getMessage());
                }
            }
        });


        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(scanButton, BorderLayout.SOUTH);
        frame.getContentPane().add(ipInicial, BorderLayout.NORTH);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(scanButton, true);
        frame.setVisible(true);
        frame.requestFocus();
    }

    /**
     * Actualizar area de texto
     */
    private void updateTextArea() {
        textArea.setText("");
        if (direcciones.size() == 1) {
            textArea.append(rb.getString("no_conectado"));
        } else {
            for (Equipment direccion : direcciones) {
                textArea.append(direccion.getCode() + "\n");
            }
        }
    }

    /**
     * Setear coordinador de la red
     * @param coordinator coordinador
     */
    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Get ip del equipo
     * @return String
     */
    private String getIPSeleccionada() {
        if (coordinator.getSelectedItem() != null) {
            if (coordinator.getSelectedItem() instanceof Equipment eq) {

                return eq.getCode();
            }
        }
        return "0";
    }
}

