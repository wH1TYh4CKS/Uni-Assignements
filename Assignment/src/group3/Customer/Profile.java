package group3.Customer;

import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.Pages.Section;
import group3.Customer.Register.Register;
import group3.Store.UserType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Profile extends Section {
    private final String[] userData;

    public Profile() {
        super(new BorderLayout());
        userData = new FileManager().getUserData(PetShop.getWindow().getUsername());

        JPanel profileCon = createProfileCon();

        mainContainer.add(profileCon, BorderLayout.WEST);

        try {
            mainContainer.add(getPurchaseHistory(), BorderLayout.EAST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createProfileCard(String type, String value, JComponent fieldType, String original) {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(new EmptyBorder(5, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel title = new JLabel("<html><h2>" + type + "</h2></html>");
        container.add(title, c);

        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JSeparator jSeparator = new JSeparator();
        jSeparator.setForeground(Color.black);
        container.add(jSeparator, c);

        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.9;
        JLabel valueLabel = new JLabel("<html><body width=200><h3>" + value + "</h3></body></html>");
        container.add(valueLabel, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.anchor = GridBagConstraints.CENTER;
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonActionListener(type, fieldType, original));
        container.add(editButton, c);

        return container;
    }

    private JPanel createProfileCon() {
        JPanel profileCon = new JPanel(new GridLayout(4, 2));
        profileCon.add(createProfileCard("Username", userData[0], new JTextField(), userData[0]));
        profileCon.add(createProfileCard("Password", "**********", new JPasswordField(), userData[1]));
        profileCon.add(createProfileCard("Age", userData[2], new JSpinner(new SpinnerNumberModel(Integer.parseInt(userData[2]), 18, 100, 1)), userData[2]));
        profileCon.add(createProfileCard("Gender", userData[3], new JComboBox<>(new String[]{"Male", "Female"}), userData[3]));
        profileCon.add(createProfileCard("Phone Number", userData[4], new JTextField(), userData[4]));
        profileCon.add(createProfileCard("Email", userData[5], new JTextField(), userData[5]));
        String address = String.join(", ", Arrays.copyOfRange(userData, 6, userData.length));
        profileCon.add(createProfileCard("Address", address, new JTextField(), address));

        JPanel buttonCon = new JPanel(new GridBagLayout());
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> {
            PetShop.getWindow().logout();
            PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
        });
        logoutButton.setPreferredSize(new Dimension(150, 50));
        buttonCon.add(logoutButton);
        profileCon.add(buttonCon);

        return profileCon;
    }

    private JPanel getPurchaseHistory() throws Exception {
        JPanel container = new JPanel(new BorderLayout());

        FileManager fileManager = new FileManager();

        List<String> orderIds = Files.readAllLines(new File("res/Orders/" + PetShop.getWindow().getUsername() + ".txt").toPath());
        orderIds.sort((o1, o2) -> {
            int num1 = Integer.parseInt(o1.substring(2));
            int num2 = Integer.parseInt(o2.substring(2));
            return num1 - num2;
        });

        String[][] orders = Arrays.stream(fileManager.readFile(UserType.ORDERS)).filter(o -> orderIds.contains(o[0])).sorted((o1, o2) -> {
            int num1 = Integer.parseInt(o1[0].substring(2));
            int num2 = Integer.parseInt(o2[0].substring(2));
            return num1 - num2;
        }).toArray(String[][]::new);
        HashMap<String, String[][]> orderMap = new HashMap<>();
        for (String[] order : orders) {
            String orderId = order[0];
            List<List<String>> products = new ArrayList<>();
            for (int i = 1; i <= (order.length - 1); i += 2) {
                products.add(new ArrayList<>(Arrays.asList(order[i], order[i + 1])));
            }
            String[][] productsArray = products.stream().map(list -> list.toArray(new String[0])).toArray(String[][]::new);
            orderMap.put(orderId, productsArray);
        }

        String[][] payments = Arrays.stream(fileManager.readFile(UserType.PAYMENTS)).filter(p -> orderIds.contains(p[1])).sorted((o1, o2) -> {
            int num1 = Integer.parseInt(o1[1].substring(2));
            int num2 = Integer.parseInt(o2[1].substring(2));
            return num1 - num2;
        }).toArray(String[][]::new);
        String[][] status = Arrays.stream(fileManager.readFile(UserType.ORDER_STATUS)).filter(s -> orderIds.contains(s[0])).sorted((o1, o2) -> {
            int num1 = Integer.parseInt(o1[0].substring(2));
            int num2 = Integer.parseInt(o2[0].substring(2));
            return num1 - num2;
        }).toArray(String[][]::new);

        String[][] combinedList = new String[orderIds.size()][];
        for (int i = 0; i < orderIds.size(); i++) {
            combinedList[i] = new String[]{orderIds.get(i), payments[i][0], payments[i][2], payments[i][3], status[i][1]};
        }

        String[] ordersColumns = {"Order ID", "Payment ID", "Total Price", "Timestamp", "Order Status"};
        DefaultTableModel ordersModel = new DefaultTableModel(combinedList, ordersColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable ordersTable = new JTable(ordersModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        ordersTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Dimension tableDim = new Dimension(600, 400);
        ordersTable.setPreferredSize(tableDim);
        ordersTable.setPreferredScrollableViewportSize(tableDim);
        JScrollPane ordersSP = new JScrollPane(ordersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        String[] productsColumns = {"Product SKU", "Amount"};
        AtomicReference<DefaultTableModel> productsModel = new AtomicReference<>(new DefaultTableModel(new String[0][], productsColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JTable productsTable = new JTable(productsModel.get());
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane productsSP = new JScrollPane(productsTable);

        ordersTable.getSelectionModel().addListSelectionListener(e -> {
            String orderId = (String) ordersTable.getValueAt(ordersTable.getSelectedRow(), 0);
            productsModel.set(new DefaultTableModel(orderMap.get(orderId), productsColumns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });
            productsTable.setModel(productsModel.get());
        });

        container.add(ordersSP, BorderLayout.WEST);
        container.add(productsSP, BorderLayout.EAST);

        return container;
    }

    private class EditButtonActionListener implements ActionListener {
        private final String type;
        private final JComponent fieldType;
        private final String original;

        public EditButtonActionListener(String type, JComponent fieldType, String original) {
            this.type = type;
            this.fieldType = fieldType;
            this.original = original;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame f = new JFrame("Edit " + type);
            f.setIconImage(PetShop.getWindow().getIcon());
            f.setSize(350, 150);
            f.setLayout(new GridLayout(3, 2));
            f.setLocationRelativeTo(PetShop.getWindow().getFrame());
            f.setVisible(true);

            JLabel newType = new JLabel("New " + type + ":");
            f.add(newType);
            f.add(fieldType);

            JLabel password = new JLabel("Enter your password:");
            f.add(password);

            JPasswordField passwordField = new JPasswordField();
            f.add(passwordField);

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e2 -> {
                String input = "";
                if (fieldType instanceof JTextComponent) {
                    JTextComponent textField = (JTextComponent) fieldType;
                    input = textField instanceof JPasswordField ? new String(((JPasswordField) textField).getPassword()) : textField.getText().trim();
                    if (input.isEmpty()) {
                        JOptionPane.showMessageDialog(f, "Please fill in your details.", "Missing Input", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    FileManager fileManager = new FileManager();
                    Register register = new Register();
                    if (type.equals("Username") && fileManager.isExistingUsername(UserType.CUSTOMER, input)) {
                        JOptionPane.showMessageDialog(f, "Username already exists.", "Invalid Username", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else if (type.equals("Password") && !register.isValidPassword(input)) {
                        JOptionPane.showMessageDialog(f, "Invalid Password.", "Invalid Password", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else if (type.equals("Email") && !register.isValidEmail(input)) {
                        JOptionPane.showMessageDialog(f, "Invalid Email.", "Invalid Email", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } else if (fieldType instanceof JSpinner) {
                    input = ((JSpinner) fieldType).getValue().toString();
                } else if (fieldType instanceof JComboBox) {
                    input = (String) ((JComboBox) fieldType).getSelectedItem();
                }

                if (!new String(passwordField.getPassword()).equals(userData[1])) {
                    JOptionPane.showMessageDialog(f, "Wrong Password.", "Invalid Password", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                List<String> newData = Arrays.stream(userData).collect(Collectors.toList());
                if (!type.equals("Address")) {
                    newData.set(newData.indexOf(original), input);
                } else {
                    newData = newData.subList(0, 6);
                    newData.addAll(Arrays.asList(input.split(",")));
                }

                FileManager fileManager = new FileManager();
                String[][] allData = fileManager.readFile(UserType.CUSTOMER);
                List<String> newAllData = Arrays.stream(allData).map(d -> String.join(",", d)).collect(Collectors.toList());
                newAllData.set(newAllData.indexOf(String.join(",", userData)), String.join(",", newData));
                fileManager.writeFile(UserType.CUSTOMER, newAllData);

                if (type.equals("Username")) {
                    File cartFile = new File("res/Carts/" + userData[0] + ".txt");
                    File orderFile = new File("res/Orders/" + userData[0] + ".txt");
                    cartFile.renameTo(new File("res/Carts/" + newData.get(0) + ".txt"));
                    orderFile.renameTo(new File("res/Orders/" + newData.get(0) + ".txt"));
                    PetShop.getWindow().setUsername(newData.get(0));
                }
                f.dispose();
                PetShop.getWindow().updatePage(new Profile().getMainContainer());
            });
            f.add(saveButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e2 -> {
                f.dispose();
            });
            f.add(cancelButton);
        }
    }
}
