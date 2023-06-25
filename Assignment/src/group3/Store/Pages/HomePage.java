package group3.Store.Pages;

import group3.PetShop;
import group3.Store.Products.Product;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends Section {
    private final JPanel productsContainer;
    private final SearchBar searchBar;
    private Categories catSec;

    public HomePage() {
        super(new GridBagLayout());
        productsContainer = new JPanel();
        GridLayout grid = new GridLayout(0, 4, 20, 20);
        productsContainer.setLayout(grid);
        productsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane jsp = new JScrollPane(productsContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        Dimension jspDim = new Dimension(900, 670);
        jsp.setPreferredSize(jspDim);
        jsp.setMinimumSize(jspDim);
        jsp.setMaximumSize(jspDim);
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        searchBar = new SearchBar();
        mainContainer.add(searchBar.getMainContainer(), c);

        c.gridy = 1;
        catSec = new Categories();
        mainContainer.add(catSec.getMainContainer(), c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2;
        mainContainer.add(jsp, c);
    }

    @Override
    public JPanel getMainContainer() {
        productsContainer.removeAll();
        if (catSec.getCheckedList().isEmpty() && searchBar.getSearchBarField().getText().isEmpty()) {
            for (Product product : PetShop.getProducts().getAllProducts()) {
                productsContainer.add(product.getPanel());
            }
            fixSizing();
            PetShop.getProducts().clearSelectedProducts();
            return mainContainer;
        }
        if (!catSec.getCheckedList().isEmpty()) {
            setFilteredProducts(PetShop.getProducts().getAllProducts());
        } else {
            PetShop.getProducts().clearSelectedProducts();
        }
        if (!searchBar.getSearchBarField().getText().isEmpty()) {
            if (PetShop.getProducts().getSelectedProducts().isEmpty()) {
                setSearchedProducts(PetShop.getProducts().getAllProducts());
            } else {
                setSearchedProducts(PetShop.getProducts().getSelectedProducts());
            }
        }
        PetShop.getProducts().getSelectedProducts().forEach(p -> productsContainer.add(p.getPanel()));
        fixSizing();
        return mainContainer;
    }

    public void setFilteredProducts(List<Product> productList) {
        PetShop.getProducts().setSelectedProducts(productList.stream().filter(p -> catSec.getCheckedList().contains(p.getAnimalType())).collect(Collectors.toCollection(ArrayList::new)));
    }

    public void setSearchedProducts(List<Product> productList) {
        String searchText = searchBar.getSearchBarField().getText().toLowerCase();
        PetShop.getProducts().setSelectedProducts(productList.stream()
                .filter(p -> (p.getProductInfo().getName().toLowerCase().contains(searchText)) ||
                        (p.getProductInfo().getBrand().toLowerCase().contains(searchText))).collect(Collectors.toList()));
    }

    private void fixSizing() {
        int amount = productsContainer.getComponents().length;
        if (amount > 0 && amount < 5 ) {
            for (int i = 0; i < 5 - amount; i++) {
                productsContainer.add(Product.getEmptyPanel());
            }
        }
    }

    public void update() {
        PetShop.getProducts().loadAll();
        catSec = new Categories();
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        mainContainer.remove(1);
        mainContainer.add(catSec.getMainContainer(), c, 1);
    }
}
