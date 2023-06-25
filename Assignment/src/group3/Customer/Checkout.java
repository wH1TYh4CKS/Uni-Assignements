package group3.Customer;

import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.Pages.Section;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Checkout extends Section {

    public Checkout(JPanel orderSummary) {
        super(new GridBagLayout());
        mainContainer.setPreferredSize(new Dimension(800, 500));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        mainContainer.add(orderSummary, c);

        c.gridy = 1;
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)), c);

        c.gridy = 2;
        mainContainer.add(createAddressSection(), c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 3;
        mainContainer.add(createPaymentSection(), c);
    }

    private JPanel createAddressSection() {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel homeAddressHeader = new JLabel("<html><h1>Home Address</h1><html>");
        container.add(homeAddressHeader, c);

        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JSeparator jSeparator = new JSeparator();
        jSeparator.setForeground(Color.black);
        container.add(jSeparator, c);

        c.gridy = 2;
        String[] userData = new FileManager().getUserData(PetShop.getWindow().getUsername());
        String address = String.join(", ", Arrays.copyOfRange(userData, 6, userData.length));
        JLabel homeAddress = new JLabel("<html><body width=400><h2>" +
                address +
                "</h2></body></html>");
        container.add(homeAddress, c);

        return container;
    }

    private JPanel createPaymentSection() {
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 30);
        JLabel title = new JLabel("<html><body><h1>Card Information</h1></body></html>");
        container.add(title, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        Image img = null;
        JLabel logos = new JLabel();
        try {
            img = ImageIO.read(new File("res/creditcard_logos.png")).getScaledInstance(-1, 25, Image.SCALE_SMOOTH);
            logos.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.add(logos, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        JSeparator jSeparator = new JSeparator();
        jSeparator.setForeground(Color.black);
        container.add(jSeparator, c);

        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel nameOnCard = new JLabel("<html><h2>Name on card</h2></html>");
        container.add(nameOnCard, c);

        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField nameField = new JTextField();
        container.add(nameField, c);

        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        JLabel cardNo = new JLabel("<html><h2>Card Number</h2></html>");
        container.add(cardNo, c);

        c.gridy = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        JFormattedTextField cardNoField = new JFormattedTextField(format);
        container.add(cardNoField, c);

        c.gridy = 6;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        JLabel expiryDate = new JLabel("<html><h2>Expiry Date (MM/YY)</h2></html>");
        container.add(expiryDate, c);

        c.gridx = 1;
        JLabel cvc = new JLabel("<html><h2>CVC</h2></html>");
        container.add(cvc, c);

        c.gridx = 0;
        c.gridy = 7;
        c.weightx = 0.75;
        JFormattedTextField dateField = new JFormattedTextField(new SimpleDateFormat("MM/yy"));
        dateField.setColumns(5);
        container.add(dateField, c);

        c.gridx = 1;
        JFormattedTextField cvcField = new JFormattedTextField(format);
        cvcField.setColumns(3);
        container.add(cvcField, c);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(15, 0, 0, 0);
        JButton payButton = new JButton("<html><h2>Pay</h2></html>");
        payButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || cardNoField.getText().isEmpty() || dateField.getText().isEmpty() || cvcField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(PetShop.getWindow().getFrame(), "Please fill in all of your card information.", "Empty Fields", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            PetShop.getWindow().getOrders().addToOrders();
        });
        container.add(payButton, c);

        return container;
    }
}
