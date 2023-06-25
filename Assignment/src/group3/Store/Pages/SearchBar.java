package group3.Store.Pages;

import group3.PetShop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SearchBar extends Section {
    private final JTextField searchBar;

    public SearchBar() {
        super(new GridBagLayout());
        mainContainer.setBorder(new EmptyBorder(10, 20, 10, 10));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        JLabel name = new JLabel("<html><h2>Search</h2></html>");
        mainContainer.add(name, c);

        c.gridy = 1;
        searchBar = new JTextField();
        Dimension searchBarDim = new Dimension(100, 20);
        searchBar.setPreferredSize(searchBarDim);
        searchBar.setMaximumSize(searchBarDim);
        searchBar.setMinimumSize(searchBarDim);
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateProducts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateProducts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            public void updateProducts() {
                PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
                searchBar.requestFocusInWindow();
            }
        });
        mainContainer.add(searchBar, c);
    }

    public JTextField getSearchBarField() {
        return searchBar;
    }
}
