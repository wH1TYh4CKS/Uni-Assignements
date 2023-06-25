package group3;

import group3.Store.Products.Products;
import group3.Store.Window;

import javax.swing.*;

public class PetShop {
    private static Window window;
    private static Products products;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            products = new Products();
            products.loadAll();
            window = new Window();
            window.showWindow();
        });
    }

    public static Window getWindow() {

        return window;
    }

    public static Products getProducts() {

        return products;
    }
}
