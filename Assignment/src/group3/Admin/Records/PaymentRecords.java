package group3.Admin.Records;

import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.UserType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

public class PaymentRecords {
    private final JFrame frame;

    public PaymentRecords() {
        frame = new JFrame("Payment Records");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        HashMap<String, String> orderCustomerMap = new HashMap<>();
        for (File orderFile : new File("res/Orders").listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
                String orderId;
                while ((orderId = br.readLine()) != null) {
                    orderCustomerMap.put(orderId, orderFile.getName().substring(0, orderFile.getName().length() - 4));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileManager fileManager = new FileManager();
        String [][] paymentData = Arrays.stream(fileManager.readFile(UserType.PAYMENTS)).map(p -> new String[]{p[0], p[1], orderCustomerMap.get(p[1]), p[2], p[3]}).toArray(String[][]::new);
        String[] paymentColumns = {"Payment ID", "Order ID", "Customer", "Total Price", "Timestamp"};
        DefaultTableModel paymentModel = new DefaultTableModel(paymentData, paymentColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable paymentTable = new JTable(paymentModel);
        paymentTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane paymentsSP = new JScrollPane(paymentTable);

        frame.add(paymentsSP, BorderLayout.CENTER);

        JPanel botCon = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        JLabel search = new JLabel("Search Payment: ");
        botCon.add(search, c);

        c.gridx = 1;
        c.weightx = 0.8;
        c.fill = GridBagConstraints.HORIZONTAL;
        JTextField searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }

            private void updateTable() {
                String searchString = searchField.getText().toLowerCase();
                if (searchString.isEmpty()) {
                    paymentTable.setModel(paymentModel);
                    return;
                }
                String[][] filteredArray = Arrays.stream(paymentData).filter(p -> (p[0].toLowerCase().contains(searchString)) ||
                        (p[1].toLowerCase().contains(searchString)) || (p[2].toLowerCase().contains(searchString))).toArray(String[][]::new);
                DefaultTableModel filteredModel = new DefaultTableModel(filteredArray, paymentColumns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                paymentTable.setModel(filteredModel);
            }
        });
        botCon.add(searchField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            Records recordsPage = new Records();
            recordsPage.show();
        });
        botCon.add(backButton, c);

        frame.add(botCon, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }
}
