package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.IconUtil;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(equipmentType);
        setIcon(equipmentType);
    }

    public void updateProperties(Object e) {
        if (e instanceof Equipment eq) {
            propiedades.setText(eq.toString());
            setIcon(eq.getEquipmentType().getCode());
        } else if (e instanceof Location lo) {
            setIcon("LOC");
            propiedades.setText(STR."""
Codigo:\{lo.getCode()}

Descripcion:
\{lo.getDescription()}""");
        } else if (e instanceof Connection con) {
            setIcon(con.getWire().getCode());
            propiedades.setText(STR."""
Equipo origen: \{con.getPort1().getEquipment().getCode()}
Puerto: \{con.getPort1().getPortType().getCode()}

Equipo Destino: \{con.getPort2().getEquipment().getCode()}
Puerto: \{con.getPort2().getPortType().getCode()}
""");
        }
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
