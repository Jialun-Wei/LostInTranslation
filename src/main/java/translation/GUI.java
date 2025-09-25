package translation;

import javax.swing.*;
import java.awt.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConv = new CountryCodeConverter();
            LanguageCodeConverter langConv   = new LanguageCodeConverter();

            // Build display lists (codes -> names)
            java.util.List<String> countryNames = new java.util.ArrayList<>();
            for (String c : translator.getCountryCodes()) {
                String name = countryConv.fromCountryCode(c.toLowerCase());
                if (name != null) countryNames.add(name);
            }

            java.util.List<String> languageNames = new java.util.ArrayList<>();
            for (String lc : translator.getLanguageCodes()) {
                String name = langConv.fromLanguageCode(lc.toLowerCase());
                if (name != null) languageNames.add(name);
            }

            //VIEW
            JLabel title = new JLabel("Country Name Translator");
            title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel languagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            languagePanel.add(new JLabel("Language:"));
            JComboBox<String> languageBox = new JComboBox<>(languageNames.toArray(new String[0]));
            languageBox.setPrototypeDisplayValue("Portuguese (Brazil)");
            languagePanel.add(languageBox);
            languagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JList<String> countryList = new JList<>(countryNames.toArray(new String[0]));
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countryList.setVisibleRowCount(12);
            JScrollPane countryScroll = new JScrollPane(countryList);
            countryScroll.setPreferredSize(new Dimension(300, 220));

            JPanel countryPanel = new JPanel(new BorderLayout());
            countryPanel.add(new JLabel("Country:"), BorderLayout.NORTH);
            countryPanel.add(countryScroll, BorderLayout.CENTER);
            countryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            resultPanel.add(new JLabel("Translation:"));
            JTextField resultField = new JTextField(24);
            resultField.setEditable(false);
            resultPanel.add(resultField);
            resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            //CONTROLLER
            Runnable update = () -> {
                String countryName = countryList.getSelectedValue();
                Object langObj = languageBox.getSelectedItem();
                if (countryName == null || langObj == null) {
                    resultField.setText("");
                    return;
                }
                String alpha3   = countryConv.fromCountry(countryName);
                String langCode = langConv.fromLanguage(langObj.toString());
                String out = (alpha3 == null || langCode == null)
                        ? null
                        : translator.translate(alpha3.toLowerCase(), langCode.toLowerCase());
                resultField.setText((out == null || out.isEmpty()) ? "no translation found!" : out);
            };

            languageBox.addActionListener(e -> update.run());
            countryList.addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) update.run(); });

            if (!countryNames.isEmpty()) countryList.setSelectedIndex(0);
            if (!languageNames.isEmpty()) languageBox.setSelectedIndex(0);

            //LAYOUT
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.add(title);
            mainPanel.add(Box.createVerticalStrut(8));
            mainPanel.add(languagePanel);
            mainPanel.add(Box.createVerticalStrut(8));
            mainPanel.add(countryPanel);
            mainPanel.add(Box.createVerticalStrut(8));
            mainPanel.add(resultPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }
}
