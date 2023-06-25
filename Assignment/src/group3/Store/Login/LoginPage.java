package group3.Store.Login;

import group3.PetShop;
import group3.Customer.Register.RegistrationPage;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    private final JLabel userNameLabel;
    private final JLabel passwordLabel;
    private final JTextField userNameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final Login login = new Login();
    private final JFrame frame;

    public LoginPage() {
        frame = new JFrame("Login");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(400, 150);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 2));

        userNameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        userNameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.addActionListener(e -> {
            String userName = userNameField.getText();
            String password = new String(passwordField.getPassword());

            if (login.login(userName, password)) {
                frame.dispose();
            } else if (!login.login(userName, password)) {
                JOptionPane.showMessageDialog(LoginPage.this.frame, "Login failed.");
            }

        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            RegistrationPage registerPage = new RegistrationPage();
            registerPage.show();
        });

        frame.add(userNameLabel);
        frame.add(userNameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(registerButton);
    }

    public void show() {

        frame.setVisible(true);
    }

}
