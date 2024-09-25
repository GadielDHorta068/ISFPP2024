package org.deneb.stylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SlidingDialog extends JDialog {
    private static final int ANIMATION_DURATION = 75; // Duración de la animación en milisegundos
    private static final int STEP_SIZE = 10; // Tamaño del paso en píxeles

    public SlidingDialog(JFrame parentFrame, String title, String message) {
        super(parentFrame, title, true);
        setLayout(new BorderLayout());
        // Aplicar el estilo de StylusUI
        JPanel panel = new JPanel(new BorderLayout());
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setUndecorated(true);
        MacOSWindowButtons macButtons = new MacOSWindowButtons(null);
        add(macButtons, BorderLayout.NORTH);

        panel.setBackground(StylusUI.COLOR_PRIMARIO.darker());
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        StylusUI.aplicarEstiloEtiqueta(messageLabel);
        panel.add(messageLabel, BorderLayout.CENTER);



        // Crear un panel para el botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(StylusUI.COLOR_PRIMARIO.darker());

        JButton btnAceptar = new JButton("Aceptar");
        StylusUI.aplicarEstiloBoton(btnAceptar, true);
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slideOut();
            }
        });

        buttonPanel.add(btnAceptar);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        StylusUI.aplicarEstiloPanel((JPanel) getContentPane()); // Aplicar estilo al panel del diálogo

        setSize(320, 120);
        setLocationRelativeTo(parentFrame);

        // Inicializar posición de la ventana fuera de la pantalla
        setLocation(parentFrame.getX() + (parentFrame.getWidth() - getWidth()) / 2, parentFrame.getY() + parentFrame.getHeight());

        // Crear un temporizador para animar el deslizamiento
        Timer timer = new Timer(10, new ActionListener() {
            private final int totalSteps = ANIMATION_DURATION / 10;
            private int step = 0;
            private final int targetY = parentFrame.getY() + (parentFrame.getHeight() - getHeight()) / 2;
            private final int startY = parentFrame.getY() + parentFrame.getHeight();

            @Override
            public void actionPerformed(ActionEvent e) {
                int yPosition = startY + (step * (targetY - startY)) / totalSteps;
                setLocation(getX(), yPosition);

                step++;
                if (step > totalSteps) {
                    setLocation(getX(), targetY); // Ajustar la posición final para el centro
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();


    }

    private void slideOut() {
        // Crear un temporizador para animar el deslizamiento hacia abajo
        Timer timer = new Timer(10, new ActionListener() {
            private final int totalSteps = ANIMATION_DURATION / 10;
            private int step = 0;
            private final int startY = getY();
            private final int endY = getOwner().getY() + getOwner().getHeight();

            @Override
            public void actionPerformed(ActionEvent e) {
                int yPosition = startY + (step * (endY - startY)) / totalSteps;
                setLocation(getX(), yPosition);

                step++;
                if (step > totalSteps) {
                    setLocation(getX(), endY); // Ajustar la posición final para la parte inferior
                    ((Timer) e.getSource()).stop();
                    dispose();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}