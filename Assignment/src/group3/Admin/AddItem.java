package group3.Admin;

import com.sun.xml.internal.ws.util.StringUtils;
import group3.PetShop;
import group3.Store.Products.Availability;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.text.DecimalFormat;

public class AddItem {
    private final JFrame frame;

    public AddItem() {
        frame = new JFrame("Add Item");
        frame.setIconImage(PetShop.getWindow().getIcon());
        frame.setSize(525, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel container = new JPanel(new GridBagLayout());
        frame.add(container, BorderLayout.CENTER);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.LINE_START;
        JLabel sku = new JLabel("SKU:");
        container.add(sku, c);

        c.gridy = 1;
        JLabel category = new JLabel("Category:");
        container.add(category, c);

        c.gridy = 2;
        JLabel name = new JLabel("Name:");
        container.add(name, c);

        c.gridy = 3;
        JLabel brand = new JLabel("Brand:");
        container.add(brand, c);

        c.gridy = 4;
        JLabel price = new JLabel("Price (RM):");
        container.add(price, c);

        c.gridy = 5;
        JLabel availability = new JLabel("Availability:");
        container.add(availability, c);

        c.gridy = 6;
        JLabel image = new JLabel("Product Image:");
        container.add(image, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 0.8;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        JTextField skuField = new JTextField();
        container.add(skuField, c);

        c.gridy = 1;
        JComboBox catBox = new JComboBox<>(PetShop.getProducts().getAnimalTypes().stream().map(t -> StringUtils.capitalize(t.replaceAll("_", " "))).toArray());
        container.add(catBox, c);

        c.gridy = 2;
        JTextField nameField = new JTextField();
        container.add(nameField, c);

        c.gridy = 3;
        JTextField brandField = new JTextField();
        container.add(brandField, c);

        c.gridy = 4;
        DecimalFormat df = new DecimalFormat("0.00");
        JFormattedTextField priceField = new JFormattedTextField(df);
        container.add(priceField, c);

        c.gridy = 5;
        JComboBox availabilityField = new JComboBox(Availability.values());
        container.add(availabilityField, c);

        c.gridy = 6;
        c.weightx = 0.7;
        c.gridwidth = 1;
        JLabel imagePath = new JLabel("No image selected");
        container.add(imagePath, c);

        c.gridx = 2;
        c.weightx = 0.1;
        JButton openButton = new JButton("Open");
        openButton.addActionListener(new OpenButtonActionListener(imagePath));
        container.add(openButton, c);

        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 3;
        c.weightx = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel details = new JLabel("Details:");
        container.add(details, c);

        c.gridy = 8;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;
        JTextArea detailsField = new JTextArea();
        detailsField.setLineWrap(true);
        detailsField.setWrapStyleWord(true);
        JScrollPane detailsSP = new JScrollPane(detailsField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsSP.getVerticalScrollBar().setUnitIncrement(16);
        detailsSP.setSize(new Dimension(500, 300));
        detailsSP.setBorder(new EmptyBorder(10, 0, 0, 0));
        container.add(detailsSP, c);

        c.gridy = 9;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(new AddItemActionListener(skuField, nameField, brandField, catBox, availabilityField, priceField, imagePath, detailsField));
        container.add(addItemButton, c);

        c.gridy = 10;
        JButton backButton = new JButton("Back to Menu Page");
        backButton.addActionListener(e -> {
            frame.dispose();
            AdminPage adminPage = new AdminPage();
            adminPage.show();
        });
        container.add(backButton, c);
    }

    public void show() {
        frame.setVisible(true);
    }

    private class OpenButtonActionListener implements ActionListener {
        private final JLabel label;

        public OpenButtonActionListener(JLabel label) {
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setDialogTitle("Select an image file");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
            chooser.addChoosableFileFilter(filter);

            int option = chooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                label.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private class AddItemActionListener implements ActionListener {
        private final JTextField skuField, nameField, brandField;
        private final JComboBox catBox, availabilityField;
        private final JFormattedTextField priceField;
        private final JLabel imagePath;
        private final JTextArea detailsField;

        public AddItemActionListener(JTextField skuField, JTextField nameField, JTextField brandField, JComboBox catBox, JComboBox availabilityField, JFormattedTextField priceField, JLabel imagePath, JTextArea detailsField) {
            this.skuField = skuField;
            this.nameField = nameField;
            this.brandField = brandField;
            this.catBox = catBox;
            this.availabilityField = availabilityField;
            this.priceField = priceField;
            this.imagePath = imagePath;
            this.detailsField = detailsField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String sku = skuField.getText().trim().toUpperCase();
            if (PetShop.getProducts().getSKUList().contains(sku)) {
                JOptionPane.showMessageDialog(frame, "SKU already exists.", "Invalid SKU", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            String price = priceField.getText();
            String details = detailsField.getText().trim().replaceAll("\n", "\\\\n");
            String type = catBox.getSelectedItem().toString().toLowerCase().replaceAll(" ", "_");
            Availability availability = (Availability) availabilityField.getSelectedItem();
            File imgFile = new File(imagePath.getText());
            String imgName = imgFile.getName();

            if (sku.isEmpty() || name.isEmpty() || brand.isEmpty() || price.isEmpty() || details.isEmpty() || imagePath.getText().equals("No image selected")) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Missing Values", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            File typeFolder = new File("res/products/" + type);
            File productsFile = new File(typeFolder.getPath() + "/" + type + ".txt");

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(productsFile, true))) {
                bw.write(sku);
                bw.newLine();
                bw.write(name);
                bw.newLine();
                bw.write(brand);
                bw.newLine();
                bw.write(price);
                bw.newLine();
                bw.write(details);
                bw.newLine();
                bw.write(availability.name());
                bw.newLine();
                bw.write(imgName);
                bw.newLine();
                bw.newLine();

                File destFile = new File(typeFolder.getPath() + "/" + imgName);
                if (!destFile.exists()) Files.copy(imgFile.toPath(), destFile.toPath());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            frame.dispose();
            PetShop.getWindow().getHomePage().update();
            PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
        }
    }
}
