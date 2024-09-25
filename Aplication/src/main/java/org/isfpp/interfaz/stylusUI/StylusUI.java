package org.deneb.stylusUI;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class StylusUI {
    // Colores oscuros inspirados en Material UI, IOS , Ubuntu
    //Las variables publicas estaran disponibles para que otras clases puedan obtener el color
    public static final Color COLOR_PRIMARIO = new Color(38, 41, 48);  // Pantone 433 C (Gris oscuro)
    public static final Color COLOR_SECUNDARIO = new Color(0, 91, 173); // Pantone 7687 C (Azul profundo)
    public static final Color COLOR_TERCIARIO = new Color(3, 3, 65, 249);//Mejorar este tono
    public static final Color COLOR_FONDO_BOTON = new Color(51, 63, 72); // Pantone 7546 C (Gris azulado)
    public static final Color COLOR_TEXTO = new Color(240,240,240); // Menos blanco par reducir contraste
    public static final Color COLOR_HOVER_BOTON = new Color(132, 146, 156); // Pantone 7544 C (Gris medio)
    public static final Color COLOR_PRESIONADO_BOTON = new Color(60, 75, 83); // Pantone 7545 C (Gris profundo)

    private static final Color DARK_BACKGROUND_COLOR = new Color(55, 54, 54); // Pantone Black 7 C (Negro ahumado)
    private static final Color TEXT_COLOR = new Color(230, 230, 230); // Color del texto
    private static final Color ERROR_COLOR = Color.RED; // Color para errores (sin cambios)
    private static final int PADDING_HORIZONTAL = 2; // Espacio extra en los botones
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(51, 63, 72); // Pantone 7546 C (Gris azulado)
    private static final String NAME_BUTTON_SOUND = "/boton.wav";
    private static final Color BUTTON_HOVER_COLOR = BUTTON_BACKGROUND_COLOR.darker();
    private static final Color BUTTON_PRESSED_COLOR = BUTTON_BACKGROUND_COLOR.brighter();

    // Tipografía personalizada
    public static Font FUENTE_TEXTO;
    public static Font FUENTE_TITULO;

    static {
        try {
            URL fuenteURL = StylusUI.class.getResource("/FiraMonoNerdFont-Medium.otf");
            if (fuenteURL == null) {
                throw new IOException("No se pudo encontrar la fuente en la URL especificada.");
            }

            try (InputStream fuenteStream = fuenteURL.openStream()) {
                FUENTE_TEXTO = Font.createFont(Font.TRUETYPE_FONT, fuenteStream).deriveFont(14f);
                FUENTE_TITULO = FUENTE_TEXTO.deriveFont(Font.ITALIC, 16f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(FUENTE_TEXTO);

                // Aplicar la fuente a todos los componentes globalmente
                UIManager.put("Label.font", FUENTE_TEXTO);
                UIManager.put("Button.font", FUENTE_TEXTO);
                UIManager.put("TextField.font", FUENTE_TEXTO);
                UIManager.put("Table.font", FUENTE_TEXTO);
                UIManager.put("TextArea.font", FUENTE_TEXTO);
                UIManager.put("ComboBox.font", FUENTE_TEXTO);
                UIManager.put("CheckBox.font", FUENTE_TEXTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FUENTE_TEXTO = new Font("SansSerif", Font.PLAIN, 14);
            FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 16);
        }
    }

    /**
     * Esta clase recibe un boton como parametro y le aplica toda la configuracion personalizada que deseemos
     * @param boton boton a ser alterado
     * @param relleno true si el boton sera solo texto y el resto de sus componentes transparente
     */
    public static void aplicarEstiloBoton(JButton boton, Boolean relleno) {
        boton.setBackground(COLOR_FONDO_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(FUENTE_TEXTO.deriveFont(12f));  // Tamaño de fuente reducido
        boton.setFont(boton.getFont().deriveFont(Font.BOLD)); // Bold para mas volumen(?
        boton.setFocusPainted(false);
        boton.setBorder(new BordeRedondeado(10));  // Borde redondeado más pequeño
        // Ajustar márgenes muy pequeños para hacer el botón más compacto
        boton.setMargin(new Insets(2, 5, 2, 5));    //Esto hace que el boton tenga mas "tamaño"
        boton.setPreferredSize(new Dimension(85, 35));  // Tamaño ajustado, me quedaba muy grande
        boton.setContentAreaFilled(relleno);  // Mantener el relleno, caso contrario logramos hacer un boton "transparente" con texto legible
       if (!relleno){
           boton.setBorder(null);
       }
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));    //HandCursor hace que al poner el mouse sobre un boton cambie a "la manito"

        // Ajustar tamaño al contenido del texto del botón
        Dimension preferredSize = boton.getPreferredSize();
        preferredSize.width += PADDING_HORIZONTAL * 2; // Añadir espacio horizontal adicional
        boton.setPreferredSize(preferredSize);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!relleno){
                    boton.setForeground(COLOR_HOVER_BOTON);
                }else{
                    boton.setBackground(COLOR_HOVER_BOTON);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!relleno){
                    boton.setForeground(COLOR_TEXTO);
                }else{
                    boton.setBackground(COLOR_FONDO_BOTON);
                }
            }
        });
    }

    /**
     * Cambia la fuente y el fondo a una etiqueta
     * @param etiqueta etiqueta a cambiar
     */
    public static void aplicarEstiloEtiqueta(JLabel etiqueta) {
        etiqueta.setForeground(COLOR_TEXTO);
        etiqueta.setFont(FUENTE_TEXTO);
    }

    /**
     * Cambia el fondo del panel
     * @param panel panel a ser tratado
     */
    public static void aplicarEstiloPanel(JPanel panel) {
        panel.setBackground(COLOR_PRIMARIO);
    }

    /**
     * Recibe una tabla y se le cambian los aspectos de la misma
     * @param tabla tabla a ser tratada
     * @param reajustar si es verdadero, se ajustara el largo de las columnas al largo del texto del titulo que las contenga
     */
    public static void aplicarEstiloTabla(JTable tabla, boolean reajustar) {
        tabla.setBackground(COLOR_PRIMARIO);  // Fondo de la tabla
        tabla.setForeground(COLOR_TEXTO);     // Color del texto
        tabla.setFont(FUENTE_TEXTO.deriveFont(Font.PLAIN, 12f));  // Fuente del texto sin negrita
        tabla.setFillsViewportHeight(true);   // Llenar el área de la tabla, incluso cuando está vacía

        JTableHeader cabecera = tabla.getTableHeader();
        cabecera.setBackground(COLOR_SECUNDARIO);  // Fondo de la cabecera
        cabecera.setForeground(COLOR_TEXTO);       // Texto de la cabecera
        cabecera.setFont(FUENTE_TITULO);           // Fuente de la cabecera

        // Bordes y selección
        tabla.setGridColor(COLOR_SECUNDARIO);         // Color de las líneas de la tabla
        tabla.setSelectionBackground(COLOR_SECUNDARIO.darker());  // Fondo de selección
        tabla.setSelectionForeground(COLOR_TEXTO);    // Texto de selección

        // Renderer para el fondo de las celdas
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(COLOR_SECUNDARIO.darker());  // Fondo seleccionado
                    c.setForeground(COLOR_TEXTO);               // Texto seleccionado
                } else {
                    c.setBackground(COLOR_PRIMARIO);            // Fondo normal
                    c.setForeground(COLOR_TEXTO);               // Texto normal
                }
                return c;
            }
        });
        if (reajustar){
            ajustarAnchoColumnas(tabla);
        }
    }

    /**
     * Modifica las visuale de un campo de texto
     * @param textField texto a ser tratado
     */
    public static void aplicarEstiloCampoTexto(JTextField textField) {
        textField.setBackground(DARK_BACKGROUND_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));
        textField.setFont(FUENTE_TEXTO);
    }

    /**
     * Este metodo recibe una jtable y ajusta el largo de cada columna al largo de su texto,
     * para no dejar titulos ilegibles
     * @param tabla a ser tratada
     */
    public static void ajustarAnchoColumnas(JTable tabla) {
        JTableHeader header = tabla.getTableHeader();
        int rowCount = tabla.getRowCount();

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            TableColumn column = tabla.getColumnModel().getColumn(i);
            int maxWidth = header.getDefaultRenderer().getTableCellRendererComponent(tabla, column.getHeaderValue(), false, false, 0, i).getPreferredSize().width;

            for (int j = 0; j < rowCount; j++) {
                int cellWidth = tabla.getCellRenderer(j, i).getTableCellRendererComponent(tabla, tabla.getValueAt(j, i), false, false, j, i).getPreferredSize().width;
                maxWidth = Math.max(maxWidth, cellWidth);
            }

            column.setPreferredWidth(maxWidth + 10); // Añadir un margen adicional para el espaciado
        }
    }

    /**
     * JDialog a ser tratado medio generico
     * @param dialog dialog a ser tratado
     */
    public static void aplicarEstiloDialogo(JDialog dialog) {
        dialog.getContentPane().setBackground(DARK_BACKGROUND_COLOR);
        dialog.setFont(FUENTE_TEXTO);

        // Aplicar estilo a los componentes dentro del diálogo
        for (Component component : dialog.getContentPane().getComponents()) {
            if (component instanceof JPanel) {
                aplicarEstiloPanel((JPanel) component);
            } else if (component instanceof JLabel) {
                aplicarEstiloEtiqueta((JLabel) component);
            } else if (component instanceof JTextField) {
                aplicarEstiloCampoTexto((JTextField) component);
            } else if (component instanceof JButton) {
                aplicarEstiloBoton((JButton) component, true);
            }
        }

        dialog.pack(); // Ajustar el tamaño del diálogo según su contenido
        dialog.setLocationRelativeTo(null); // Centrar el diálogo en la pantalla
    }

    /**
     * Testear mejor, agarra los componentes de un Joptionpane comun usado en clase y quizas falten elementos
     */
    public static void aplicarEstiloJOptionPane() {

    }

    /**
     * Reproduce el sonido al presionar un boton
     */
    private static void reproducirSonido() {
        try {
            URL sonidoURL = StylusUI.class.getResource(StylusUI.NAME_BUTTON_SOUND);
            if (sonidoURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(sonidoURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println(STR."Archivo de sonido no encontrado: \{StylusUI.NAME_BUTTON_SOUND}");
            }
        } catch (Exception e) {
            System.err.println(STR."Error al reproducir el sonido: \{e.getMessage()}");
        }
    }

    /**
     * Configura el estilo de la interfaz a los combobox
     * @param comboBox combobox a ser personalizado
     */
    public static void aplicarEstiloComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(DARK_BACKGROUND_COLOR);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setFont(FUENTE_TEXTO);
        comboBox.setBorder(BorderFactory.createLineBorder(TEXT_COLOR)); ///Cambiar esto, no mgggg

        // Estilo de los ítems desplegados
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(BUTTON_BACKGROUND_COLOR);
                button.setForeground(TEXT_COLOR);
                return button;
            }
        });
    }

    /**
     * Personaliza los scrollpanes para que este en armonia con la UI
     * @param scrollPane
     */
    public static void aplicarEstiloScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(DARK_BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));

        // Personaliza las barras de desplazamiento
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        JScrollBar horizontalBar = scrollPane.getHorizontalScrollBar();

        verticalBar.setBackground(BUTTON_BACKGROUND_COLOR);
        verticalBar.setForeground(TEXT_COLOR);
        horizontalBar.setBackground(BUTTON_BACKGROUND_COLOR);
        horizontalBar.setForeground(TEXT_COLOR);

        // Ajustar el grosor de las barras de desplazamiento
        verticalBar.setPreferredSize(new Dimension(12, Integer.MAX_VALUE));
        horizontalBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 12));
    }

    /**
     * Estiliza la barra de progreso
     * @param progressBar Objeto a ser personalizado
     */
    public static void aplicarEstiloProgressBar(JProgressBar progressBar, Boolean textoBarra) {
        progressBar.setBackground(DARK_BACKGROUND_COLOR);
        progressBar.setForeground(COLOR_SECUNDARIO); // Color del progreso, el progreso no sera detenido. RAINBOWWWWWW PLZ
        progressBar.setFont(FUENTE_TEXTO);
        progressBar.setBorder(BorderFactory.createLineBorder(TEXT_COLOR));

        // Texto sobre la barra de progreso (opcional)
        progressBar.setStringPainted(textoBarra);
        progressBar.setString("Maldito Ilon Mosc"); // O el texto que prefieras
    }

    /**
     * Estiliza los botones radio
     * @param radioButton objeto a ser tratado
     */
    public static void aplicarEstiloRadioButton(JRadioButton radioButton) {
        //Sin utilidad por ahora
        radioButton.setBackground(DARK_BACKGROUND_COLOR);
        radioButton.setForeground(TEXT_COLOR);
        radioButton.setFont(FUENTE_TEXTO);

        // Personaliza el ícono seleccionado
        radioButton.setIcon(new javax.swing.ImageIcon("ruta/iconoNoSeleccionado.png"));
        radioButton.setSelectedIcon(new javax.swing.ImageIcon("ruta/iconoSeleccionado.png"));
    }

    /**
     * Personalizar checkboxes
     * @param checkBox objeto a personalizar
     */
    public static void aplicarEstiloCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(DARK_BACKGROUND_COLOR);
        checkBox.setForeground(TEXT_COLOR);
        checkBox.setFont(FUENTE_TEXTO);

        // Personaliza el ícono marcado
        checkBox.setIcon(new javax.swing.ImageIcon("ruta/iconoNoSeleccionado.png"));
        checkBox.setSelectedIcon(new javax.swing.ImageIcon("ruta/iconoSeleccionado.png"));
    }

    /**
     * Personalizar Tabs
     * @param tabbedPane objeto a tratar
     */
    public static void aplicarEstiloTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setBackground(DARK_BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setFont(FUENTE_TEXTO);

        // Personalizar las pestañas
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                tabAreaInsets.left = 10;
            }

            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(COLOR_SECUNDARIO);
                } else {
                    g.setColor(DARK_BACKGROUND_COLOR);
                }
                g.fillRect(x, y, w, h);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(COLOR_SECUNDARIO.darker());
                g.drawRect(x, y, w, h);
            }
        });
    }

//  No funciona bien, tengo que econtrar manera de pasar la clase y que trate automaticamente todos los elementos
    public static void aplicarEstilosGlobales() {
        // Colores y estilos globales
        UIManager.put("Button.background", COLOR_FONDO_BOTON);
        UIManager.put("Button.foreground", COLOR_TEXTO);
        UIManager.put("Button.font", FUENTE_TEXTO);
        UIManager.put("Button.border", new BordeRedondeado(10));
        UIManager.put("Button.hover", COLOR_HOVER_BOTON);
        UIManager.put("Button.pressed", COLOR_PRESIONADO_BOTON);

        UIManager.put("Label.font", FUENTE_TEXTO);
        UIManager.put("Label.foreground", COLOR_TEXTO);

        UIManager.put("TextField.background", DARK_BACKGROUND_COLOR);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.font", FUENTE_TEXTO);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);

        UIManager.put("Table.background", COLOR_PRIMARIO);
        UIManager.put("Table.foreground", COLOR_TEXTO);
        UIManager.put("Table.font", FUENTE_TEXTO);
        UIManager.put("Table.gridColor", COLOR_SECUNDARIO);

        UIManager.put("TableHeader.background", COLOR_SECUNDARIO);
        UIManager.put("TableHeader.foreground", COLOR_TEXTO);
        UIManager.put("TableHeader.font", FUENTE_TITULO);

        UIManager.put("ScrollBar.background", COLOR_PRIMARIO);
        UIManager.put("ScrollBar.foreground", COLOR_TEXTO);

        UIManager.put("ComboBox.background", DARK_BACKGROUND_COLOR);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.font", FUENTE_TEXTO);
        UIManager.put("ComboBox.selectionBackground", COLOR_SECUNDARIO);
        UIManager.put("ComboBox.selectionForeground", COLOR_TEXTO);

        UIManager.put("ComboBox.editorBackground", DARK_BACKGROUND_COLOR);
        UIManager.put("ComboBox.editorForeground", TEXT_COLOR);

        UIManager.put("OptionPane.background", DARK_BACKGROUND_COLOR);
        UIManager.put("Panel.background", DARK_BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Button.background", BUTTON_BACKGROUND_COLOR);
        UIManager.put("Button.foreground", TEXT_COLOR);
        UIManager.put("Button.font", FUENTE_TEXTO.deriveFont(14f)); // Actualizar con fuente personalizada
        UIManager.put("Button.border", BorderFactory.createLineBorder(TEXT_COLOR));
        UIManager.put("OptionPane.buttonFont", FUENTE_TEXTO.deriveFont(14f)); // Actualizar con fuente personalizada
    }

    /**
     * Ajusta el tamaño del texto y la ventana en un JDialog para que se adapte al contenido.
     * @param dialog El JDialog a ajustar.
     * @param texto El texto que se mostrará en el diálogo.
     */
    public static void ajustarDialogo(JDialog dialog, String texto) {
        // Crear un JLabel con el texto proporcionado
        JLabel label = new JLabel(texto);
        label.setFont(FUENTE_TEXTO); // Aplicar fuente personalizada

        // Ajustar el diseño del JDialog para que se ajuste al texto
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(label, BorderLayout.CENTER);

        // Ajustar el tamaño del diálogo al contenido
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Centrar el diálogo en la pantalla
    }

    /**
     * Metodo para inicializar variables globales
     * @param rick true if you never gona give you up
     */
    public static void inicializar(boolean rick) {
        aplicarEstilosGlobales();
        if (rick){
            configurarRickrolleo();
        }
    }

    /**
     * never gona give you up
     */
    private static void configurarRickrolleo(){
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println(STR."Se ha producido una excepción no manejada: \{throwable.getMessage()}");
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                }
            } catch (IOException | URISyntaxException e) {
                System.err.println(STR."Error al intentar abrir el enlace: \{e.getMessage()}");
            }
        });
    }
}

