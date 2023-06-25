package group3.Admin.Records;

import group3.PetShop;
import group3.Admin.AdminPage;

import javax.swing.*;
import java.awt.*;

public class Records {
    private final JFrame frame;

    public Records() {
        frame = new JFrame("Records Page");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel container = new JPanel(new GridLayout(3, 1));
        JButton ordersButton = new JButton("Order Records");
        JButton paymentsButton = new JButton("Payment Records");
        JButton backButton = new JButton("Go Back");

        container.add(ordersButton);
        container.add(paymentsButton);
        container.add(backButton);

        frame.add(container, BorderLayout.CENTER);

        ordersButton.addActionListener(e -> {
            frame.dispose();
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.show();
        });

        paymentsButton.addActionListener(e -> {
            frame.dispose();
            PaymentRecords paymentRecords = new PaymentRecords();
            paymentRecords.show();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            AdminPage adminPage = new AdminPage();
            adminPage.show();
        });
    }

    public void show() {
        frame.setVisible(true);
    }
}
