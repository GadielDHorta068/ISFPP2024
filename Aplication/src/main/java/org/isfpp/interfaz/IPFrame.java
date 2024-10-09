package org.isfpp.interfaz;

import org.isfpp.controller.Coordinator;
import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


public class IPFrame {
    private JFrame frame;
    private JTextArea textArea;
    private JButton scanButton;
    private List<String> direcciones;
    private Coordinator coordinator;


    public IPFrame() {
    }

    public void scanIp() {
        direcciones = new ArrayList<>();
        frame = new JFrame("IP Scanner");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        JTextField ipInicial = new JTextField("IP a escanear");
        String defecto = getIPSeleccionada();
        System.out.println(defecto);
        if (!Objects.equals(defecto, "0")){
            ipInicial.setText(defecto);
        }

        StylusUI.aplicarEstiloCampoTexto(ipInicial);

        ipInicial.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ipInicial.getText().equals("IP a escanear")){
                    ipInicial.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(ipInicial.getText().isEmpty()){
                    ipInicial.setText("IP a escanear");
                }
            }
        });

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scanButton = new JButton("Scan IPs");
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direcciones.clear();
                try {
                    direcciones = coordinator.scanIP(ipInicial.getText());
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

        private void updateTextArea () {
        textArea.setText("");
        System.out.println(direcciones.size());

            for (String direccion : direcciones) {
                textArea.append(direccion + "\n");
            }

    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
    private String getIPSeleccionada(){
        if(coordinator.getSelectedItem() != null){
            if(coordinator.getSelectedItem() instanceof Equipment eq){
                String ip = eq.getIpAdresses().getFirst();
                String[] partes = ip.split("\\.") ;
                partes[2] = "0";
                partes[3] = "0";
                return String.join(".", partes);
            }
        }
        return "0";
    }
}