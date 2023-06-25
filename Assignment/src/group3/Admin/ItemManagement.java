package group3.Admin;

import group3.PetShop;

import javax.swing.*;
import java.awt.*;

public class ItemManagement {
    private final JFrame frame;

    public ItemManagement() {
        frame = new JFrame("Item Management");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


    }
}
