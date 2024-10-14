package org.isfpp.interfaz.panelesAddons;

import org.isfpp.controller.Coordinator;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Equipment;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Traceroute {
    private JFrame frame;
    private JTextArea textArea;
    private Coordinator coordinator;
    private GraphPath<Equipment, DefaultWeightedEdge> direcciones;
   private JTextField textE1;
    private JTextField textE2;

    public Traceroute() {

    }
    public void trace (){
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
        JButton okButton = new JButton("Tracerouter");
        okButton.addActionListener(e -> {
            try {
                textArea.setText("");
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
        });

        textE1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textE1.getText().equals("codigo equipo 1")){
                    textE1.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(textE1.getText().isEmpty()){
                    textE1.setText("codigo equipo 1");
                }
            }
        });

        textE2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textE2.getText().equals("codigo equipo 2")){
                    textE2.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(textE2.getText().isEmpty()){
                    textE2.setText("codigo equipo 2");
                }
            }
        });

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 2));
        northPanel.add(textE1);
        northPanel.add(textE2);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(okButton, BorderLayout.SOUTH);
        frame.getContentPane().add(northPanel, BorderLayout.NORTH);
        StylusUI.aplicarEstiloScrollPane(scrollPane);
        StylusUI.styleTextArea(textArea);
        StylusUI.aplicarEstiloBoton(okButton, true);
        frame.setVisible(true);
        frame.requestFocus();

    }
    private void updateTextArea () {
        textE1.setText("");
        textE2.setText("");

        for(Equipment e:direcciones.getVertexList()){
            textArea.append(e.getCode() + "\n");
        }

    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
