package org.isfpp.interfaz.panelesPrincipal;

import org.isfpp.interfaz.stylusUI.StylusUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class LanguageSelectorPanel extends JFrame {
    private JTextField nameField;
    private JComboBox<String> languageComboBox;
    private JLabel greetingLabel;
    private ResourceBundle messages;
    private final CountDownLatch latch;
    private static Locale loc;

    public LanguageSelectorPanel(CountDownLatch latch) {
        this.latch = latch;
        setTitle("Selección de idioma");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cargar mensajes en el idioma predeterminado
        messages = ResourceBundle.getBundle("messages", new Locale("es", "ES"));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        StylusUI.aplicarEstiloPanel(mainPanel);

        JLabel introLabel = new JLabel("Primera vez? Vamos a personalazirar tu experiencia, por favor diganos su nombre y lenguaje de preferencia");
        introLabel.setHorizontalAlignment(JLabel.CENTER);
        StylusUI.aplicarEstiloEtiqueta(introLabel);


        JLabel nameLabel = new JLabel(messages.getString("nombre"));
        nameField = new JTextField(15);
        StylusUI.aplicarEstiloEtiqueta(nameLabel);

        String[] languages = {"Español", "English", "Français"};
        languageComboBox = new JComboBox<>(languages);
        StylusUI.aplicarEstiloComboBox(languageComboBox);

        greetingLabel = new JLabel(messages.getString("greeting"));
        StylusUI.aplicarEstiloEtiqueta(greetingLabel);


        JPanel namePanel = new JPanel();
        StylusUI.aplicarEstiloPanel(namePanel);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        namePanel.add(languageComboBox);

        JButton continueButton = new JButton("Continuar");
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

        pack();
        setVisible(true);
    }

    private void updateLanguage() {
        String selectedLanguage = (String) languageComboBox.getSelectedItem();
        assert selectedLanguage != null;
        Locale locale = switch (selectedLanguage) {
            case "English" -> new Locale("en", "US");
            case "Français" -> new Locale("fr", "FR");
            default -> new Locale("es", "ES");
        };

        // Recargar las propiedades en el nuevo idioma
        messages = ResourceBundle.getBundle("messages", locale);

        // Actualizar los textos de todos los componentes
        reloadTexts();
        loc = locale;
    }

    private void reloadTexts() {
        // Actualizar el saludo con el nombre ingresado
        greetingLabel.setText(messages.getString("greeting") + ", " + nameField.getText() + "!");

        // Aquí actualizarías otros componentes si los tuvieras
        revalidate();

        repaint();
    }

    public static Locale getLoc() {
        return loc;
    }
    public String getName(){
        return nameField.getText();
    }
}
