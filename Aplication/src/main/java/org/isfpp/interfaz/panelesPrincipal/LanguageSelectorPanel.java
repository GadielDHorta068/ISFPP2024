package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.stylusUI.MacOSWindowButtons;
import org.isfpp.interfaz.stylusUI.StylusUI;
import org.isfpp.interfaz.stylusUI.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

/**
 * Panel selector de lenguaje que se inicia al ejecutar el programa
 */
public class LanguageSelectorPanel extends JFrame {
    private final JTextField nameField;
    private final JComboBox<String> languageComboBox;
    private final JLabel greetingLabel;
    private ResourceBundle messages;
    private final CountDownLatch latch;
    private static Locale loc;
    private final JLabel introLabel;
    private final JButton continueButton;
    private final JLabel nameLabel;

    /**
     * Constructor del panel
     * @param latch Detiene la ejecucion del programa para esperar a que se continue en el boton
     */
    public LanguageSelectorPanel(CountDownLatch latch) {
        this.latch = latch;
        setTitle("Selección de idioma");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());


        messages = ResourceBundle.getBundle("messages", new Locale("es", "ES"));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StylusUI.aplicarEstiloPanel(mainPanel);


        introLabel = new JLabel(messages.getString("greeting"));
        introLabel.setHorizontalAlignment(JLabel.CENTER);
        StylusUI.aplicarEstiloEtiqueta(introLabel);


        nameLabel = new JLabel(messages.getString("nombre"));
        nameField = new JTextField(15);
        nameField.setText("admin");
        StylusUI.aplicarEstiloEtiqueta(nameLabel);

        String[] languages = {"Español", "English", "Français"};
        languageComboBox = new JComboBox<>(languages);
        languageComboBox.setSelectedIndex(0);
        StylusUI.aplicarEstiloComboBox(languageComboBox);

        greetingLabel = new JLabel(messages.getString("greeting"));
        StylusUI.aplicarEstiloEtiqueta(greetingLabel);

        JPanel namePanel = new JPanel();
        StylusUI.aplicarEstiloPanel(namePanel);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        namePanel.add(languageComboBox);
        continueButton = new JButton(messages.getString("continuar"));
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                latch.countDown();  // Permite continuar con la ejecución
                dispose();
            }
        });
        StylusUI.aplicarEstiloBoton(continueButton, false);


        mainPanel.add(introLabel, BorderLayout.NORTH);
        mainPanel.add(namePanel, BorderLayout.CENTER);
        mainPanel.add(greetingLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        StylusUI.aplicarEstiloPanel(buttonPanel);
        buttonPanel.add(continueButton);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(mainPanel);

        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLanguage();
            }
        });
        updateLanguage();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    /**
     * Configurar el locale con el lenguaje deseado
     */
    private void updateLanguage() {
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        assert selectedLanguage != null;
        Locale locale = switch (selectedLanguage) {
            case "English" -> new Locale("en", "US");
            case "Français" -> new Locale("fr", "FR");
            default -> new Locale("es", "ES");
        };

        messages = ResourceBundle.getBundle("messages", locale);

        reloadTexts();
        loc = locale;
    }

    /**
     * Actualizar textos con el idioma elejido
     */
    private void reloadTexts() {
        greetingLabel.setText(messages.getString("greeting") + ", " + nameField.getText() + "!");
        introLabel.setText(messages.getString("greeting"));
        continueButton.setText(messages.getString("continuar"));
        nameLabel.setText(messages.getString("nombre"));
        revalidate();

        repaint();
    }

    /**
     * Obtener idioma elejido
     * @return Locale
     */
    public static Locale getLoc() {
        return loc;
    }

    /**
     * Obtener nombre del usuario
     * @return String
     */
    public String getName(){
        return nameField.getText();
    }
}
