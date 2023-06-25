package group3.Admin;

import group3.Admin.Records.Records;
import group3.PetShop;
import group3.Admin.AdminOrderPage;
import group3.Store.Login.LoginPage;
import group3.Admin.Statistics.StatisticalReportpage;
import group3.Admin.UserManagement;

import javax.swing.*;
import java.awt.*;

public class AdminPage {

    private final JFrame frame;


    public AdminPage() {
        frame = new JFrame("Admin Page");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);


        JPanel buttonPanel = new JPanel(new GridLayout(7, 1));
        JButton userManagementButton = new JButton("User Management");
        JButton categoryManagementButton = new JButton("Category Management");
        JButton addItemButton = new JButton("Add Item");
        JButton displayRecordsButton = new JButton("Display Records");
        JButton orderDeliveryManagementButton = new JButton("Order Delivery Management");
        JButton statisticalReportsButton = new JButton("Statistical Reports");
        JButton LogOutButton = new JButton("Log Out");

        buttonPanel.add(userManagementButton);
        buttonPanel.add(categoryManagementButton);
        buttonPanel.add(addItemButton);
        buttonPanel.add(displayRecordsButton);
        buttonPanel.add(orderDeliveryManagementButton);
        buttonPanel.add(statisticalReportsButton);
        buttonPanel.add(LogOutButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        userManagementButton.addActionListener(e -> {
            frame.dispose();
            UserManagement userManagement = new UserManagement();
            userManagement.show();

        });

        categoryManagementButton.addActionListener(e -> {
            frame.dispose();
            CategoryManagement catManagement = new CategoryManagement();
            catManagement.show();
        });

        addItemButton.addActionListener(e -> {
            frame.dispose();
            AddItem addItemPage = new AddItem();
            addItemPage.show();
        });

        displayRecordsButton.addActionListener(e -> {
            frame.dispose();
            Records recordsPage = new Records();
            recordsPage.show();
        });

        orderDeliveryManagementButton.addActionListener(e -> {
            frame.dispose();
            AdminOrderPage adminOrderPage = new AdminOrderPage();
            adminOrderPage.show();
        });

        statisticalReportsButton.addActionListener(e -> {
            frame.dispose();
            StatisticalReportpage statisticalReportPage = new StatisticalReportpage();
            statisticalReportPage.show();
        });

        LogOutButton.addActionListener(e -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JFrame) {
                    JFrame frame = (JFrame) window;
                    if (frame.equals(PetShop.getWindow().getFrame())) continue;
                    frame.dispose();
                }
            }
            frame.dispose();
            PetShop.getWindow().logout();
            LoginPage loginPage = new LoginPage();
            loginPage.show();
        });
    }

    public void show() {

        frame.setVisible(true);
    }
}