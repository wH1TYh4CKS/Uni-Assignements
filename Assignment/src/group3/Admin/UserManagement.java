package group3.Admin;

import group3.Admin.AdminPage;
import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.Login.Login;
import group3.Store.UserType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class UserManagement {
    private final JTextField userNameField;
    private final JPasswordField passwordField;
    private final JTable adminTable;
    private final JTable deliveryStaffTable;
    private final JFrame frame;
    private final FileManager fileManager = new FileManager();
    String[][] readFileAdmin = fileManager.readFile(UserType.ADMIN);
    String[][] readFileDeliveryStaff = fileManager.readFile(UserType.DELIVERY_STAFF);
    private final Login login = new Login();

    public UserManagement() {
        frame = new JFrame("User Management");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(920, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Read the contents of the text files into data arrays

        String[][] adminData = readFileAdmin;
        String[][] deliveryStaffData = readFileDeliveryStaff;

        // Create table models based on the data arrays
        DefaultTableModel adminModel = new DefaultTableModel(adminData, new String[]{"Username", "Password"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableModel deliveryStaffModel = new DefaultTableModel(deliveryStaffData, new String[]{"Username", "Password"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create JTable instances based on the table models
        adminTable = new JTable(adminModel);
        deliveryStaffTable = new JTable(deliveryStaffModel);

        adminTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        adminTable.getTableHeader().setReorderingAllowed(false);
        deliveryStaffTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deliveryStaffTable.getTableHeader().setReorderingAllowed(false);

        // Add the JTable instances to scroll panes
        JScrollPane adminScrollPane = new JScrollPane(adminTable);
        JScrollPane deliveryStaffScrollPane = new JScrollPane(deliveryStaffTable);

        // Add the scroll panes to the frame
        frame.add(adminScrollPane, BorderLayout.WEST);
        frame.add(deliveryStaffScrollPane, BorderLayout.EAST);

        // Add label and text fields for username and password
        JLabel userNameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        userNameField = new JTextField();
        passwordField = new JPasswordField();

        // Add buttons for adding, deleting, modifying, and returning
        JButton addUserButton = new JButton("Add new User");
        JButton deleteUserButton = new JButton("Delete Existing User");
        JButton modifyUserButton = new JButton("Modify User Password");
        JButton backButton = new JButton("Back to Menu Page");

        // Add action listeners to the buttons
        addUserButton.addActionListener(new AddUserActionListener());
        deleteUserButton.addActionListener(new DeleteUserActionListener());
        modifyUserButton.addActionListener(new ModifyUserActionListener());
        backButton.addActionListener(new BackActionListener());

        // Create a panel for the labels, text fields, and buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(5, 2, 5, 5));
        controlPanel.add(userNameLabel);
        controlPanel.add(userNameField);
        controlPanel.add(passwordLabel);
        controlPanel.add(passwordField);
        controlPanel.add(addUserButton);
        controlPanel.add(deleteUserButton);
        controlPanel.add(modifyUserButton);
        controlPanel.add(backButton);
        // Add the control panel to the frame
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void refreshAdminTable() {
        String[][] adminData = fileManager.readFile(UserType.ADMIN);
        DefaultTableModel adminModel = new DefaultTableModel(adminData, new String[]{"Username", "Password"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        adminTable.setModel(adminModel);
        adminTable.revalidate();
    }

    private void refreshDSTable() {
        String[][] DeliveryStaffData = fileManager.readFile(UserType.DELIVERY_STAFF);
        DefaultTableModel deliveryStaffModel = new DefaultTableModel(DeliveryStaffData, new String[]{"Username", "Password"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deliveryStaffTable.setModel(deliveryStaffModel);
        deliveryStaffTable.revalidate();
    }

    // Inner class for the Add User button
    class AddUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String username = userNameField.getText();
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please input a new password to replace.");
                return;
            }

            login.identifyUser(username);
            UserType userType = login.identifyUser(username);

            switch (userType) {
                case ADMIN:
                    if (!fileManager.isExistingUsername(UserType.ADMIN, username)) {
                        fileManager.appendFile(userType, username + "," + password);
                        fileManager.readFile(userType);
                        refreshAdminTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username already exist.");
                    }
                    break;

                case DELIVERY_STAFF:
                    if (!fileManager.isExistingUsername(UserType.DELIVERY_STAFF, username)) {
                        fileManager.appendFile(userType, username + "," + password);
                        fileManager.readFile(userType);
                        refreshDSTable();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username already exist.");
                    }
                    break;

                default:
                    JOptionPane.showMessageDialog(frame, "Invalid Input");
                    break;
            }

        }
    }

    // Inner class for the Delete User button
    class DeleteUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = userNameField.getText();
            String password = new String(passwordField.getPassword());
            login.identifyUser(username);
            UserType userType = login.identifyUser(username);
            List<String> data = new ArrayList<>();

            switch (userType) {
                case ADMIN:
                    if (fileManager.isExistingUsername(UserType.ADMIN, username)) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader("Admin.txt"));
                            CheckAdminFile(username, password, userType, data, br);
                            refreshAdminTable();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username does not exist.");
                    }
                    break;

                case DELIVERY_STAFF:
                    if (fileManager.isExistingUsername(UserType.DELIVERY_STAFF, username)) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader("Delivery_staff.txt"));
                            CheckAdminFile(username, password, userType, data, br);
                            refreshDSTable();
                            break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username does not exist.");
                        break;
                    }

                default:
                    JOptionPane.showMessageDialog(frame, "Invalid Input");
                    break;
            }
        }

        private void CheckAdminFile(String username, String password, UserType userType, List<String> data, BufferedReader br) throws IOException {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(username + "," + password)) {
                    data.add(line);
                }
            }
            br.close();
            fileManager.writeFile(userType, data);
            fileManager.readFile(userType);
        }
    }

    // Inner class for the Modify User button
    class ModifyUserActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            String username = userNameField.getText();
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please input a new password to replace.");
                return;
            }
            login.identifyUser(username);
            UserType userType = login.identifyUser(username);
            List<String> data = new ArrayList<>();

            switch (userType) {
                case ADMIN:
                    if (fileManager.isExistingUsername(UserType.ADMIN, username)) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader("Admin.txt"));
                            CheckFileData(username, password, userType, data, br);
                            refreshAdminTable();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username does not exist.");
                    }
                    break;

                case DELIVERY_STAFF:
                    if (fileManager.isExistingUsername(UserType.DELIVERY_STAFF, username)) {
                        try {
                            BufferedReader br = new BufferedReader(new FileReader("Delivery_staff.txt"));
                            CheckFileData(username, password, userType, data, br);
                            refreshDSTable();
                            break;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username does not exist.");
                        break;
                    }

                default:
                    JOptionPane.showMessageDialog(frame, "Invalid Input");
                    break;
            }
        }


        private void CheckFileData(String username, String password, UserType userType, List<String> data, BufferedReader br) throws IOException {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(username + ",")) {
                    data.add(line);
                }
            }
            br.close();
            data.add(username + "," + password);
            fileManager.writeFile(userType, data);
            fileManager.readFile(userType);
        }

    }

    // Inner class for the Back button
    class BackActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            AdminPage adminPage = new AdminPage();
            adminPage.show();
        }
    }
}
