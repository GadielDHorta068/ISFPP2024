package org.isfpp.interfaz;

import org.isfpp.datos.Cargar;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.logica.Utils;
import org.isfpp.modelo.Web;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class IPFrame {
        private JFrame frame;
        private JTextArea textArea;
        private JButton scanButton;
        private List<String> direcciones;

    public IPFrame(){
        direcciones = new ArrayList<>();
        frame = new JFrame("IP Scanner");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        try {
            new Utils(Cargar.cargarRedDesdePropiedades("config.properties"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, STR."Error al cargar el archivo de configuración: \{e.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTextField ipInicial = new JTextField("Ip a comenzar escaneo");
        StylusUI.aplicarEstiloCampoTexto(ipInicial);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        scanButton = new JButton("Scan IPs");
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scanIP(ipInicial.getText());
                updateTextArea();
            }
        });

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(scanButton, BorderLayout.SOUTH);
        frame.getContentPane().add(ipInicial,BorderLayout.NORTH);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(scanButton,true);
        frame.setVisible(true);
    }

        public void scanIP (String ip){
        direcciones.clear();
        String[] parts = ip.split("\\.");
        int start = Integer.parseInt(parts[3]);
        IntStream.range(start, 256).forEach(i -> {
            String nuevaIP = parts[0] + "." + parts[1] + "." + parts[2] + "." + i;
            System.out.println(nuevaIP);
            if (Utils.ping(nuevaIP)) {
                System.out.println("encontro");
                direcciones.add(nuevaIP);
            }
        });
    }
        private void updateTextArea () {
        textArea.setText("");
        for (String direccion : direcciones) {
            textArea.append(direccion + "\n");
        }
    }

        public static void main (String[]args){
            SwingUtilities.invokeLater(IPFrame::new);
    }
}