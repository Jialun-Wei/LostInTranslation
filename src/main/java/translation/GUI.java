package translation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // create new json translator, language code converter, and country code converter
            Translator jsonTranslator = new JSONTranslator("sample.json");
            LanguageCodeConverter languageCoder = new LanguageCodeConverter("language-codes.txt");
            CountryCodeConverter countryCoder = new CountryCodeConverter("country-codes.txt");

            // create JComboBox and fill with languages
            JComboBox<String> jcomboboxLanguages = new JComboBox<>();
            for(String languageCode : jsonTranslator.getLanguageCodes()) {
                jcomboboxLanguages.addItem(languageCoder.fromLanguageCode(languageCode));
            }

            // create array of size number of languages and fill with languages
            String[] countries = new String[countryCoder.getNumCountries()];
            int i = 0;
            for (String countryCode : jsonTranslator.getCountryCodes()) {
                countries[i++] = countryCoder.fromCountryCode(countryCode);
            }

            // create JList of countries to choose from
            JList<String> jlistCountries = new JList<>(countries);
            jlistCountries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane jscrollCountries = new JScrollPane(jlistCountries);

            // wrap panels round each section
            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(jcomboboxLanguages);

            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(jscrollCountries);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String languageCode = languageCoder.fromLanguage(jcomboboxLanguages.getSelectedItem().toString());
                    String countryCode = countryCoder.fromCountry(jlistCountries.getSelectedValue().toString());

                    String result = jsonTranslator.translate(countryCode, languageCode);

                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);
                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(countryPanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
