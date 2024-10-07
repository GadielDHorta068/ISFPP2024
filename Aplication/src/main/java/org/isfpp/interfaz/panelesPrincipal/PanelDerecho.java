package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.IconUtil;
import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;

public class PanelDerecho {
    private JTextArea propiedades;
    private JLabel logo ;
    private  JPanel panel = new JPanel() {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250, getHeight());
        }
    };

    public JPanel crearPanelDerecho() {
        StylusUI.aplicarEstiloPanel(panel);
        panel.setLayout(new BorderLayout());

        //Set inicial; cambiar esto a un signo de pregunta y que al seleccionar un tipo x usar setIcon
        setIcon("help");
        propiedades = new JTextArea("Seleccione un objeto para ver sus propiedades");
        StylusUI.styleTextArea(propiedades);
        panel.add(propiedades, BorderLayout.CENTER);

        return panel;
    }

    public void updateProperties(String text, String equipmentType) {
        propiedades.setText(text);
        setIcon(equipmentType);
    }


    private ImageIcon resizeImage(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public void setIcon(String iconName) {
        // Logo en la parte superior
        ImageIcon logoIcon = IconUtil.getIcon(iconName);
        assert logoIcon != null;
        logoIcon = resizeImage(logoIcon, 100, 100);

        if (logo == null) {
            logo = new JLabel(logoIcon);
            StylusUI.aplicarEstiloEtiqueta(logo);
            panel.add(logo, BorderLayout.NORTH);
        } else {
            logo.setIcon(logoIcon);
        }

        // Refrescar el panel
        panel.revalidate();
        panel.repaint();
    }

}
