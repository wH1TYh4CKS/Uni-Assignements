package group3.Admin;

import com.sun.xml.internal.ws.util.StringUtils;
import group3.PetShop;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CategoryManagement {
    private final JFrame frame;
    private final File catFile;
    private final AtomicReference<List<String>> catList;
    private final JTextField addField, modifyField;
    private final String column = "Category";
    private final JTable catTable;
    private final JLabel deleteSelected;

    public CategoryManagement() {
        frame = new JFrame("Category Management");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        catFile = new File("res/categories.txt");
        catList = new AtomicReference<>(getCatList());
        List<String> formattedList = formatList(catList.get());

        DefaultTableModel catModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        catModel.addColumn(column, formattedList.toArray());
        catTable = new JTable(catModel);
        catTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        catTable.getTableHeader().setReorderingAllowed(false);
        catTable.getSelectionModel().addListSelectionListener(new TableSelectListener());
        JScrollPane catSP = new JScrollPane(catTable);

        frame.add(catSP, BorderLayout.CENTER);

        JPanel botCon = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        JLabel search = new JLabel("Search Category:");
        botCon.add(search, c);

        c.gridx = 1;
        c.weightx = 0.8;
        c.gridwidth = 2;
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
                    catTable.setModel(catModel);
                    return;
                }
                String[] filteredArray = formattedList.stream().filter(c -> c.toLowerCase().contains(searchString)).toArray(String[]::new);
                DefaultTableModel filteredModel = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                filteredModel.addColumn(column, filteredArray);
                catTable.setModel(filteredModel);
            }
        });
        botCon.add(searchField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JLabel add = new JLabel("Add Category:");
        botCon.add(add, c);

        c.gridx = 1;
        c.weightx = 0.7;
        c.fill = GridBagConstraints.HORIZONTAL;
        addField = new JTextField();
        botCon.add(addField, c);

        c.gridx = 2;
        c.weightx = 0.1;
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonActionListener());
        botCon.add(addButton, c);

        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JLabel modify = new JLabel("Modify Category:");
        botCon.add(modify, c);

        c.gridx = 1;
        c.weightx = 0.7;
        c.fill = GridBagConstraints.HORIZONTAL;
        modifyField = new JTextField();
        botCon.add(modifyField, c);

        c.gridx = 2;
        c.weightx = 0.1;
        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(new ModifyButtonActionListener());
        botCon.add(modifyButton, c);

        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JLabel delete = new JLabel("Delete Category:");
        botCon.add(delete, c);

        c.gridx = 1;
        c.weightx = 0.7;
        deleteSelected = new JLabel("Select a category to delete.");
        botCon.add(deleteSelected, c);

        c.gridx = 2;
        c.weightx = 0.1;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonActionListener());
        botCon.add(deleteButton, c);

        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 3;
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            AdminPage adminPage = new AdminPage();
            adminPage.show();
        });
        botCon.add(backButton, c);

        frame.add(botCon, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }

    private List<String> getCatList() {
        List<String> catList = null;
        try {
            catList = Files.readAllLines(catFile.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return catList;
    }

    private List<String> formatList(List<String> list) {
        return list.stream().map(c -> StringUtils.capitalize(c.replaceAll("_", " ").toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
    }

    private void updateTable(List<String> newList) {
        catList.set(newList);
        DefaultTableModel newModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        newModel.addColumn(column, formatList(catList.get()).toArray());
        catTable.setModel(newModel);
    }

    private void updateHomePage() {
        PetShop.getWindow().getHomePage().update();
        PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
    }

    private class TableSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (catTable.getSelectedRow() == -1) return;
            String selected = (String) catTable.getValueAt(catTable.getSelectedRow(), 0);
            deleteSelected.setText("Selected: " + selected);
        }
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newCat = addField.getText().toLowerCase().trim();
            Pattern pattern = Pattern.compile("^[ a-z]+$");
            Matcher matcher = pattern.matcher(newCat);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid category name.", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            newCat = newCat.replaceAll(" ", "_");

            if (catList.get().contains(newCat)) {
                JOptionPane.showMessageDialog(frame, "Category already exists.", "Invalid Category", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(catFile, true))) {
                bw.append(newCat);
                bw.newLine();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            new File("res/products/" + newCat).mkdir();
            try {
                new File("res/products/" + newCat + "/" + newCat + ".txt").createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            updateTable(getCatList());
            updateHomePage();
        }
    }

    private class ModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (catTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a category to modify.", "Invalid Category", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String newCat = modifyField.getText().trim();
            Pattern pattern = Pattern.compile("^[ A-Za-z]+$");
            Matcher matcher = pattern.matcher(newCat);
            if (!matcher.matches()) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid category name.", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String formattedCat = newCat.toLowerCase().replaceAll(" ", "_");
            if (catList.get().contains(formattedCat)) {
                JOptionPane.showMessageDialog(frame, "Category already exists.", "Invalid Category", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String selected = (String) catTable.getValueAt(catTable.getSelectedRow(), 0);
            int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to rename " + selected + " to " + newCat + "?"
                    , "Modify Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation != JOptionPane.YES_OPTION) return;
            selected = selected.trim().toLowerCase().replace(" ", "_");
            newCat = formattedCat;
            catList.get().set(catList.get().indexOf(selected), newCat);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(catFile))) {
                for (String line : catList.get()) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            File oldFolder = new File("res/products/" + selected);
            File newFolder = new File("res/products/" + newCat);
            System.out.println(oldFolder.renameTo(newFolder));

            File oldFile = new File(newFolder.getPath() + "/" + selected + ".txt");
            File newFile = new File(newFolder.getPath() + "/" + newCat + ".txt");
            System.out.println(oldFile.renameTo(newFile));

            updateTable(getCatList());
            updateHomePage();
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (catTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a category to delete.", "Invalid Category", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String selected = (String) catTable.getValueAt(catTable.getSelectedRow(), 0);
            int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the " + selected + " category?"
                    , "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation != JOptionPane.YES_OPTION) return;
            selected = selected.trim().toLowerCase().replace(" ", "_");
            List<String> newList = getCatList();
            newList.remove(selected);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(catFile));) {
                for (String line : newList) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            updateTable(getCatList());
            updateHomePage();
        }
    }
}
