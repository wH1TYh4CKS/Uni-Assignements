package group3.Customer.Register;

import group3.Customer.Register.Register;
import group3.PetShop;
import group3.Store.Login.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationPage {
    private final JFrame frame;
    private final JLabel userNameLabel;
    private final JLabel passwordLabel;
    private final JLabel ageLabel;
    private final JLabel genderLabel;
    private final JLabel phoneNumberLabel;
    private final JLabel emailLabel;
    private final JLabel addressLabel;
    private final JTextField userNameField;
    private final JTextField phoneNumberField;
    private final JTextField emailField;
    private final JTextField addressField;
    private final JComboBox<String> genderComboBox;
    private final JSpinner ageSpinner;
    private final JPasswordField passwordField;
    private final JButton registerButton;
    private final JButton backButton;

    public RegistrationPage() {
        Register register = new Register();

        frame = new JFrame("Registration");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(8, 2));

        userNameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        ageLabel = new JLabel("Age:");
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));
        genderLabel = new JLabel("Gender:");
        genderComboBox = new JComboBox<String>(new String[]{"Male", "Female"});
        phoneNumberLabel = new JLabel("Phone Number:");
        emailLabel = new JLabel("Email:");
        addressLabel = new JLabel("Address:");

        userNameField = new JTextField();
        passwordField = new JPasswordField();
        phoneNumberField = new JTextField();
        emailField = new JTextField();
        addressField = new JTextField();

        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String password = new String(passwordField.getPassword());
                String age = ageSpinner.getValue().toString();
                String gender = (String) genderComboBox.getSelectedItem();
                String phoneNumber = phoneNumberField.getText();
                String email = emailField.getText();
                String address = addressField.getText();

                String invalidInputs = "Invalid Inputs. Please Try Again!";

                if (!register.validateInputs(userName, password, age, gender, phoneNumber, email, address)) {
                    JOptionPane.showMessageDialog(frame, invalidInputs);
                    return;
                }

                if (!register.isValidPassword(password)) {
                    JOptionPane.showMessageDialog(frame, "Password entered is invalid. Try Again!");
                    return;
                }

                if (!register.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(frame, "Email entered is invalid. Try Again!");
                    return;
                }

                register.register(userName, password, age, gender, phoneNumber, email, address);
                JOptionPane.showMessageDialog(frame, "Successfully Registered!");
                frame.dispose();
                LoginPage loginPage = new LoginPage();
                loginPage.show();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginPage loginPage = new LoginPage();
                loginPage.show();
            }
        });
        frame.add(userNameLabel);
        frame.add(userNameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(ageLabel);
        frame.add(ageSpinner);
        frame.add(genderLabel);
        frame.add(genderComboBox);
        frame.add(phoneNumberLabel);
        frame.add(phoneNumberField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(addressLabel);
        frame.add(addressField);
        frame.add(registerButton);
        frame.add(backButton);
    }

    public void show() {
        frame.setVisible(true);
    }
}
