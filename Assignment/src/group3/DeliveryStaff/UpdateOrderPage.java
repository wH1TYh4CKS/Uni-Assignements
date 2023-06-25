package group3.DeliveryStaff;

import group3.DeliveryStaff.DeliveryStaffPage;
import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.UserType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class UpdateOrderPage {
    private final JFrame frame;
    private final JTable orderTable;
    private final DefaultTableModel model;
    private final JButton editButton;
    private final JButton backButton;
    private final FileManager fileManager = new FileManager();
    private final String[][] allData;

    public UpdateOrderPage() {
        frame = new JFrame("Order Management");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(920, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        allData = fileManager.readFile(UserType.ORDER_STATUS);

        // Initialize the components
        String[] columns = new String[]{"Order ID", "Status"};
        model = new DefaultTableModel(loadOrderData(), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(model);
        editButton = new JButton("Update Status with Comment");
        backButton = new JButton("Back");

        // Add the scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add the edit button
        editButton.addActionListener(new EditButtonActionListener());
        backButton.addActionListener(new BackButtonActionListener());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(editButton);
        bottomPanel.add(backButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private String[][] loadOrderData() {
        String[][] data = Arrays.stream(allData).filter(d -> d[1].equals("Delivering")).toArray(String[][]::new);
        return data;
    }

    public void show() {
        frame.setVisible(true);
    }

    private class EditButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                String status = JOptionPane.showInputDialog("Enter the new status:");
                model.setValueAt(status, selectedRow, 1);
                List<String> newData = Arrays.stream(allData).map(o -> {
                    if (o[0].equals(model.getValueAt(selectedRow, 0))) o[1] = status;
                    return String.join(",", o);
                }).collect(Collectors.toList());
                fileManager.writeFile(UserType.ORDER_STATUS, newData);
            } else {
                JOptionPane.showMessageDialog(null, "Please select an order.");
            }
        }
    }

    private class BackButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            DeliveryStaffPage deliveryStaffPage = new DeliveryStaffPage();
            deliveryStaffPage.show();
        }
    }

}

