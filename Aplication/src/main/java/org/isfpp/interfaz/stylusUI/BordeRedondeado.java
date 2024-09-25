package org.deneb.stylusUI;

import javax.swing.border.Border;
import java.awt.*;

public class BordeRedondeado implements Border {

    private final int radio;

    public BordeRedondeado(int radio) {
        this.radio = radio;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radio + 1, this.radio + 1, this.radio + 1, this.radio + 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(c.getForeground()); //Cambiar el color al contorno del boton
        g.drawRoundRect(x, y, width - 1, height - 1, radio, radio);
    }
}