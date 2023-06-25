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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OrderRecords {
    private final JFrame frame;

    public OrderRecords() {
        frame = new JFrame("Order Records");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(920, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        FileManager fileManager = new FileManager();

        List<List<String>> orderList = new ArrayList<>();
        for (File orderFile : new File("res/Orders").listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
                String orderId;
                while ((orderId = br.readLine()) != null) {
                    orderList.add(new ArrayList<>(Arrays.asList(orderId, orderFile.getName().substring(0, orderFile.getName().length() - 4))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        orderList.sort((o1, o2) -> {
            int num1 = Integer.parseInt(o1.get(0).substring(2));
            int num2 = Integer.parseInt(o2.get(0).substring(2));
            return num1 - num2;
        });

        String[][] ordersData = fileManager.readFile(UserType.ORDERS);
        HashMap<String, String[][]> orderMap = new HashMap<>();
        for (String[] order : ordersData) {
            String orderId = order[0];
            List<List<String>> products = new ArrayList<>();
            for (int i = 1; i <= (order.length - 1); i += 2) {
                products.add(new ArrayList<>(Arrays.asList(order[i], order[i + 1])));
            }
            String[][] productsArray = products.stream().map(list -> list.toArray(new String[0])).toArray(String[][]::new);
            orderMap.put(orderId, productsArray);
        }

        String[][] orderArray = orderList.stream().map(list -> list.toArray(new String[0])).toArray(String[][]::new);
        String[] ordersColumns = {"Order ID", "Customer"};
        DefaultTableModel ordersModel = new DefaultTableModel(orderArray, ordersColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable ordersTable = new JTable(ordersModel);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane ordersSP = new JScrollPane(ordersTable);

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

        frame.add(ordersSP, BorderLayout.WEST);
        frame.add(productsSP, BorderLayout.EAST);

        JPanel botCon = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        JLabel search = new JLabel("Search Order: ");
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
                    ordersTable.setModel(ordersModel);
                    return;
                }
                String[][] filteredArray = orderList.stream().filter(order -> (order.get(0).toLowerCase().contains(searchString)) ||
                        (order.get(1).toLowerCase().contains(searchString))).map(list -> list.toArray(new String[0])).toArray(String[][]::new);
                DefaultTableModel filteredModel = new DefaultTableModel(filteredArray, ordersColumns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                ordersTable.setModel(filteredModel);
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
