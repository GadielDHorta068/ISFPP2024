package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.IconUtil;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;

import javax.swing.*;
import java.awt.*;

public class PanelDerecho {
    private JTextArea propiedades;
    private JLabel logo ;
    private final JPanel panel = new JPanel() {
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
            propiedades.setText("Codigo: " + lo.getCode() + "\n" + "Descripcion: " + lo.getDescription());
        } else if (e instanceof Connection con) {
            setIcon(con.getWire().getCode());
            propiedades.setText("Equipo origen: "+ con.getPort1().getEquipment().getCode()+"\n"+ "Puerto: "+con.getPort1().getPortType().getCode() + "\n" +"MAC: " + con.getPort1().getMACAddress() +"\n"+"\n"+"Equipo Destino: " +con.getPort2().getEquipment().getCode() +"\n"+"Puerto: "+con.getPort2().getPortType().getCode()+ "\n" + "MAC: " + con.getPort2().getMACAddress());
        }
    }


    private ImageIcon resizeImage(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public void setIcon(String iconName) {
        // Logo en la parte superior
        ImageIcon logoIcon = IconUtil.getIcon(iconName);
        assert logoIcon != null;
        logoIcon = resizeImage(logoIcon);

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
