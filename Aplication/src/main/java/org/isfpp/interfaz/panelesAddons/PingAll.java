package org.isfpp.interfaz.panelesAddons;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Equipment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class PingAll {
    private JFrame frame;
    private JTextArea textArea;
    private List<String> direcciones;
    private Coordinator coordinator;
    private ResourceBundle rb;


    public PingAll() {
    }

    public void scanIp() {
        this.rb=coordinator.getResourceBundle();
        direcciones = new ArrayList<>();
        frame = new JFrame(rb.getString("ip_Scanner"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        JTextField ipInicial = new JTextField(rb.getString("ip_escanear"));
        String defecto = getIPSeleccionada();
        JTextField ipFinal = new JTextField(rb.getString("ip_escanear"));
        System.out.println(defecto);
        if (!Objects.equals(defecto, "0")) {
            ipInicial.setText(defecto);
        }

        StylusUI.aplicarEstiloCampoTexto(ipInicial);
        ipFinal.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ipFinal.getText().equals(rb.getString("ip_escanear"))) {
                    ipFinal.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ipFinal.getText().isEmpty()) {
                    ipFinal.setText(rb.getString("ip_escanear"));
                }
            }
        });

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
        JScrollPane scrollPane = new JScrollPane(textArea);
        JButton scanButton = new JButton(rb.getString("scan_IPs"));
        scanButton.addActionListener(e -> {
            direcciones.clear();
            try {
                direcciones = coordinator.scanIP(ipInicial.getText(),ipFinal.getText());
                updateTextArea();
            } catch (Exception exception) {
                textArea.setText("");
                ipInicial.setText("");
                ipFinal.setText("");
                JOptionPane.showMessageDialog(frame, exception.getMessage());
            }
            frame.pack();
        });

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2));
        northPanel.add(ipInicial);
        northPanel.add(ipFinal);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(scanButton, BorderLayout.SOUTH);
        frame.getContentPane().add(northPanel, BorderLayout.NORTH);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(scanButton, true);
        frame.setVisible(true);
        frame.requestFocus();
    }

    private void updateTextArea() {
        textArea.setText("");
        System.out.println(direcciones.size());

        for (String direccion : direcciones) {
            textArea.append(direccion + "\n" + "\n");
        }

    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    private String getIPSeleccionada() {
        if (coordinator.getSelectedItem() != null) {
            if (coordinator.getSelectedItem() instanceof Equipment eq) {
                String ip = eq.getIpAdresses().getFirst();
                String[] partes = ip.split("\\.");
                partes[2] = "0";
                partes[3] = "0";
                return String.join(".", partes);
            }
        }
        return "0";
    }
}