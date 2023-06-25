package group3.DeliveryStaff;

import group3.PetShop;
import group3.Store.Login.LoginPage;
import group3.DeliveryStaff.SelectOrderPage;
import group3.DeliveryStaff.UpdateOrderPage;

import javax.swing.*;
import java.awt.*;

public class DeliveryStaffPage {
    private final JFrame frame;

    public DeliveryStaffPage() {
        frame = new JFrame("DeliveryStaff Page");
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        JButton selectOrderButton = new JButton("Select order for Delivery");
        JButton updateOrderButton = new JButton("Update Delivery Status");
        JButton LogOutButton = new JButton("Log Out");
        //Adding created Buttons to Panel
        buttonPanel.add(selectOrderButton);
        buttonPanel.add(updateOrderButton);
        buttonPanel.add(LogOutButton);
        //Adding button Panel to frame and centering it
        frame.add(buttonPanel, BorderLayout.CENTER);

        //select Order button action
        selectOrderButton.addActionListener(e -> {
            frame.dispose();
            SelectOrderPage selectOrderPage = new SelectOrderPage();
            selectOrderPage.show();

        });

        //update order button Action
        updateOrderButton.addActionListener(e -> {
            frame.dispose();
            UpdateOrderPage updateOrderPage = new UpdateOrderPage();
            updateOrderPage.show();

        });

        LogOutButton.addActionListener(e -> {
            frame.dispose();
            PetShop.getWindow().logout();
            LoginPage loginPage = new LoginPage();
            loginPage.show();
        });

    }

    //setting the frame as visible = opening window
    public void show() {
        frame.setVisible(true);
    }
}
