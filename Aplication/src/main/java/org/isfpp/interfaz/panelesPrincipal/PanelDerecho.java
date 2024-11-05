package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.IconUtil;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.modelo.Connection;
import org.isfpp.modelo.Equipment;
import org.isfpp.modelo.Location;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * PanelDerecho: Esta clase representa un panel en la interfaz de usuario que muestra propiedades
 * y un icono relacionado con un objeto seleccionado.
 */
public class PanelDerecho {
    private JTextArea propiedades;
    private JLabel logo;
    private ResourceBundle rb;

    private final JPanel panel = new JPanel() {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250, getHeight());
        }
    };


    /**
     * crearPanelDerecho: Crea y aplica el estilo al panel derecho.
     * @return un JPanel estilizado.
     */
    public JPanel crearPanelDerecho(ResourceBundle resourceBundle) {
        this.rb=resourceBundle;
        StylusUI.aplicarEstiloPanel(panel);
        panel.setLayout(new BorderLayout());

        setIcon("help");
        propiedades = new JTextArea( rb.getString("seleccione_objeto"));
        StylusUI.styleTextArea(propiedades);
        panel.add(propiedades, BorderLayout.CENTER);

        return panel;
    }

    /**
     * updateProperties: Actualiza las propiedades mostradas y el icono del panel.
     * @param text El texto a mostrar.
     * @param equipmentType El tipo de equipamiento para el cual se mostrará el icono.
     */
    public void updateProperties(String text, String equipmentType) {
        propiedades.setText(text);
        System.out.println(equipmentType);
        setIcon(equipmentType);
    }

    /**
     * updateProperties: Actualiza las propiedades basadas en el objeto proporcionado.
     * @param e El objeto cuya información se presentará.
     */
    public void updateProperties(Object e) {
        if (e instanceof Equipment eq) {
            propiedades.setText(eq.toString());
            setIcon(eq.getEquipmentType().getCode());
        } else if (e instanceof Location lo) {
            setIcon("LOC");
            propiedades.setText( rb.getString("codigo") + lo.getCode() + "\n" +  rb.getString("descripcion") + lo.getDescription());
        } else if (e instanceof Connection con) {
            setIcon(con.getWire().getCode());
            propiedades.setText( rb.getString("equipo_origen") + con.getPort1().getEquipment().getCode() + "\n" + rb.getString( "puerto" )+ con.getPort1().getPortType().getCode() + "\n" +  rb.getString("mac") + con.getPort1().getMACAddress() + "\n" + "\n" +  rb.getString("equipo_destino" )+ con.getPort2().getEquipment().getCode() + "\n" +  rb.getString("puerto" )+ con.getPort2().getPortType().getCode() + "\n" +  rb.getString("mac") + con.getPort2().getMACAddress());
        }
    }

    /**
     * resizeImage: Redimensiona una imagen al tamaño especificado.
     * @param imageIcon El icono de imagen a redimensionar.
     * @return Un nuevo ImageIcon redimensionado.
     */
    private ImageIcon resizeImage(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /**
     * setIcon: Establece el icono en la parte superior del panel.
     * @param iconName El nombre del icono a establecer.
     */
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