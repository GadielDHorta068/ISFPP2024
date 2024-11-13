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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.isfpp.logica.CalculoGraph.ipToLong;

/**
 * Clase que mostrara la ip entre dos rangos
 */
public class PingAll {
    private JFrame frame;
    private JTextArea textArea;
    private Coordinator coordinator;
    private ResourceBundle rb;
    private JProgressBar progressBar;
    /**
     * Constructor por defecto
     */
    public PingAll() {}

    /**
     * Metodo que dibujara y contiene todos los elementos graficos
     */
    public void scanIp() {
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        StylusUI.aplicarEstiloProgressBar(progressBar, false);
        this.rb = coordinator.getResourceBundle();
        frame = new JFrame(rb.getString("ip_Scanner"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        JTextField ipInicial = new JTextField(rb.getString("ip_escanear"));
        String defecto = getIPSeleccionada();
        JTextField ipFinal = new JTextField(rb.getString("ip_escanear"));
        if (!Objects.equals(defecto, "0")) {
            ipInicial.setText(defecto);
        }

        StylusUI.aplicarEstiloCampoTexto(ipInicial);
        StylusUI.aplicarEstiloCampoTexto(ipFinal);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton scanButton = new JButton(rb.getString("scan_IPs"));
        scanButton.addActionListener(e -> {
            progressBar.setValue(0);
            textArea.setText("");
            String ip1 = ipInicial.getText();
            String ip2 = ipFinal.getText();
            new IPScannerWorker(ip1, ip2).execute();
        });

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1));
        southPanel.add(progressBar, BorderLayout.NORTH);
        southPanel.add(scanButton, BorderLayout.NORTH);
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2));
        northPanel.add(ipInicial);
        northPanel.add(ipFinal);
        frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(northPanel, BorderLayout.NORTH);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(scanButton, true);
        frame.setVisible(true);
        frame.requestFocus();
    }

    /**
     * SwingWorker para escanear IPs en segundo plano
     */
    private class IPScannerWorker extends SwingWorker<Void, String> {
        private final String ipStart;
        private final String ipEnd;
        private final int progress;
        public IPScannerWorker(String ipStart, String ipEnd) {
            this.ipStart = ipStart;
            this.ipEnd = ipEnd;
            this.progress = 0;
        }
        public void updateProgress(int progress){
            progressBar.setValue(progress);
        }
        @Override
        protected Void doInBackground() {

            ExecutorService executor = Executors.newFixedThreadPool(1);

            try {
                long ipInicio = ipToLong(ipStart);
                long ipFinal = ipToLong(ipEnd);


                List<String> direcciones = coordinator.scanIP(ipStart, ipEnd, textArea, progressBar);
                //int cont=0;
                //for (String direccion : direcciones) {
                  //  cont++;
                //    publish(direccion);// Publica cada IP encontrada para actualizar en la interfaz
               //     updateProgress((int) ((cont * 100) / direcciones.size()));

               // }///


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }

            return null;
        }

        @Override
        protected void process(List<String> direcciones) {
            // Actualizar el área de texto con cada IP
            for (String direccion : direcciones) {
                textArea.append(direccion + "\n");
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
     * Obtener la ip clickeada en la tabla
     * @return String
     */
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
