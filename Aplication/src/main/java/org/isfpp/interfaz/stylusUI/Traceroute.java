package org.isfpp.interfaz.stylusUI;

import org.isfpp.controller.Coordinator;
import org.isfpp.modelo.Equipment;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Traceroute {
    private JFrame frame;
    private JTextArea textArea;
    private JButton okButton;
    private Coordinator coordinator;
    private List<DefaultWeightedEdge> direcciones;
   private JTextField textE1;
    private JTextField textE2;

    public Traceroute() {

    }
    public void trace (){
            direcciones = new ArrayList<>();
        frame = new JFrame("TraceRouter");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        textE1= new JTextField("codigo equipo 1");
        textE2= new JTextField("codigo equipo 2");
        StylusUI.aplicarEstiloCampoTexto(textE1);
        StylusUI.aplicarEstiloCampoTexto(textE2);
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        okButton = new JButton("Tracerouter");
        for(String e: coordinator.getWeb().getHardware().keySet()) {
            System.out.println( coordinator.getWeb().getHardware().get(e));
        }
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                direcciones.clear();
                try {
                    Equipment e1=coordinator.getHardware().get(textE1.getText().toUpperCase());
                    Equipment e2=coordinator.getHardware().get(textE2.getText().toUpperCase());
                    direcciones = coordinator.traceroute(e1,e2);
                    updateTextArea();
                } catch (Exception exception) {
                    textE1.setText("");
                    textE2.setText("");
                    textArea.setText("");
                    JOptionPane.showMessageDialog(frame, exception.getMessage());

                }
            }
        });
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2)); // Distribuir los dos componentes verticalmente
        northPanel.add(textE1);
        northPanel.add(textE2);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(okButton, BorderLayout.SOUTH);
        frame.getContentPane().add(northPanel, BorderLayout.NORTH); // Añadir el panel con los dos JTextField
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(okButton, true);
        frame.setVisible(true);

    }
    private void updateTextArea () {
        textE1.setText("");
        textE2.setText("");
        for (DefaultWeightedEdge direccion : direcciones) {
            textArea.append(direccion + "\n");
        }
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
