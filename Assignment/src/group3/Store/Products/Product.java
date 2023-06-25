package group3.Store.Products;

import group3.PetShop;
import group3.Store.UserType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class Product implements MouseListener {
    private final ProductInfo productInfo;
    private final String animalType;
    private final Availability availability;
    private final File imgFile;
    private Image img;
    private final JPanel panel;
    private JFrame clickedFrame;

    public Product(ProductInfo productInfo, String animalType, Availability availability, String imgName) {
        this.productInfo = productInfo;
        this.animalType = animalType;
        this.availability = availability;
        this.imgFile = new File("res/products/" + animalType + "/" + imgName);
        try {
            this.img = ImageIO.read(imgFile).getScaledInstance(175, 175, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.panel = createPanel();
    }

    private JPanel createPanel() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.setPreferredSize(new Dimension(200, 325));
        container.setBorder(new EmptyBorder(5, 5, 5, 5));
        container.setBackground(Color.WHITE);
        container.addMouseListener(this);

        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(img));
        icon.setPreferredSize(new Dimension(175, 175));
        icon.setMaximumSize(new Dimension(175, 175));
        icon.setBorder(new EmptyBorder(0, 13, 0, 0));
        container.add(icon);

        JLabel top = new JLabel();
        String topHtml = "<html>" +
                "<style>" +
                "html, body { margin: 0px; padding: 0px; }" +
                "body { width: 150px; font-size: 16pt; }" +
                ".brand { color: #808080; }" +
                "</style>" +
                "<body>" +
                "<div class='brand'>" + productInfo.getBrand() + "</div>" +
                "<div class='name'>" + productInfo.getName() + "</div>" +
                "</body>" +
                "</html>";
        top.setText(topHtml);
        container.add(top);

        container.add(Box.createVerticalGlue());

        JLabel bot = new JLabel();
        String botHtml = "<html>" +
                "<style>" +
                "html, body { margin: 0px; padding: 0px; }" +
                "body { width: 150px; }" +
                ".price { font-size: 16pt; }" +
                ".availability { font-size: 12pt; color: #555555 }" +
                "</style>" +
                "<body>" +
                "<div class='price'>" + productInfo.getPriceText() + "</div>" +
                "<div class='availability'>" + availability.toString() + "</div>" +
                "</body>" +
                "</html>";
        bot.setText(botHtml);
        container.add(bot);

        return container;
    }

    public String getAnimalType() {
        return animalType;
    }

    public JPanel getPanel() {
        return panel;
    }

    public Availability getAvailability() {
        return availability;
    }

    public JFrame getClickedFrame() {
        return clickedFrame;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public Image getImg() {
        return img;
    }

    public static JPanel getEmptyPanel() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(200, 325));

        return  emptyPanel;
    }

    private void showDetails() {
        clickedFrame = new JFrame("Product Details");
        clickedFrame.setIconImage(PetShop.getWindow().getIcon());

        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();

        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(img));
        icon.setPreferredSize(new Dimension(175, 175));
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        c.insets = new Insets(0, 0, 0, 10);
        container.add(icon, c);

        JLabel basicInfo = new JLabel();
        String html = "<html>" +
                "<style>" +
                "html, body { padding: 0px; margin: 0px; }" +
                "body { width: 300px; } " +
                ".brand { color: #808080; }" +
                ".sku { color: #bababa }" +
                "</style>" +
                "<body>" +
                "<h2 class='brand'>" + productInfo.getBrand() + "</h2>" +
                "<h1 class='name'>" + productInfo.getName() + "</h1>" +
                "<h3 class='sku'>SKU: " + productInfo.getSKU() + "</h3>" +
                "<h3 class='availability'>" + availability.toString() + "</h3>" +
                "<h2 class='price'>" + productInfo.getPriceText() + "</h2>" +
                "</body>" +
                "</html>";
        basicInfo.setText(html);
        c.gridx = 1;
        c.gridheight = 1;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(basicInfo, c);

        SpinnerNumberModel value = new SpinnerNumberModel();
        value.setValue(1);
        value.setMinimum(1);
        JSpinner quantity = new JSpinner(value);
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(quantity, c);

        JButton addToCartButton = new JButton("Add to cart");
        addToCartButton.setFocusable(false);
        c.gridy = 2;
        addToCartButton.addActionListener(e2 -> {
            PetShop.getWindow().getCart().addToCart(this, (int) quantity.getValue());
            PetShop.getWindow().getCart().updateCartButton();
        });
        container.add(addToCartButton, c);

        JLabel details = new JLabel();
        JScrollPane detailsSP = new JScrollPane(details, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsSP.getVerticalScrollBar().setUnitIncrement(16);
        detailsSP.setPreferredSize(new Dimension(475, 300));
        detailsSP.setBorder(new EmptyBorder(10, 0, 0, 0));
        String detailsHtml = "<html>" +
                "<body width=420px>" +
                productInfo.getDetails().replaceAll("\\\\n", "<br>") +
                "</body>" +
                "</html>";
        details.setText(detailsHtml);
        details.setVerticalAlignment(JLabel.TOP);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 1;
        container.add(detailsSP, c);

        clickedFrame.setContentPane(container);
        clickedFrame.pack();
        clickedFrame.setLocationRelativeTo(PetShop.getWindow().getFrame());
        clickedFrame.setVisible(true);
    }

    private void editDetails() {
        clickedFrame = new JFrame("Edit Product Details");
        clickedFrame.setIconImage(PetShop.getWindow().getIcon());

        JPanel container = new JPanel(new GridBagLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();

        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(img));
        icon.setPreferredSize(new Dimension(175, 175));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 10);
        container.add(icon, c);

        JPanel rightCon = new JPanel(new GridBagLayout());
        rightCon.setPreferredSize(new Dimension(315, 200));
        c.insets = new Insets(5, 0, 5, 5);
        c.weightx = 0.2;
        c.anchor = GridBagConstraints.LINE_START;
        JLabel brand = new JLabel("Brand:");
        rightCon.add(brand, c);

        c.gridy = 1;
        JLabel name = new JLabel("Name:");
        rightCon.add(name, c);

        c.gridy = 2;
        JLabel sku = new JLabel("SKU:");
        rightCon.add(sku, c);

        c.gridy = 3;
        JLabel availabilityLabel = new JLabel("Availability:");
        rightCon.add(availabilityLabel, c);

        c.gridy = 4;
        JLabel price = new JLabel("Price (RM):");
        rightCon.add(price, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.8;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 0);
        JTextField brandField = new JTextField(productInfo.getBrand());
        rightCon.add(brandField, c);

        c.gridy = 1;
        JTextField nameField = new JTextField(productInfo.getName());
        rightCon.add(nameField, c);

        c.gridy = 2;
        JTextField skuField = new JTextField(productInfo.getSKU());
        skuField.setEditable(false);
        rightCon.add(skuField, c);

        c.gridy = 3;
        JComboBox availabilityBox = new JComboBox(Availability.values());
        availabilityBox.setSelectedItem(availability);
        rightCon.add(availabilityBox, c);

        c.gridy = 4;
        DecimalFormat df = new DecimalFormat("0.00");
        JFormattedTextField priceField = new JFormattedTextField(df);
        priceField.setValue(productInfo.getPrice());
        rightCon.add(priceField, c);

        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0.34;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 0, 5, 3);
        JButton resetButton = new JButton("Reset");
        rightCon.add(resetButton, c);

        c.gridx = 1;
        c.weightx = 0.33;
        c.insets = new Insets(5, 2, 5, 2);
        JButton saveButton = new JButton("Save");
        rightCon.add(saveButton, c);

        c.gridx = 2;
        c.weightx = 0.33;
        c.insets = new Insets(5, 3, 5, 0);
        JButton delButton = new JButton("Delete");
        rightCon.add(delButton, c);

        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 0);
        container.add(rightCon, c);

        JTextPane details = new JTextPane();
        details.setContentType("text/html");
        details.setText("<p>" + productInfo.getDetails().replaceAll("\\\\n", "<br>") + "</p>");
        JScrollPane detailsSP = new JScrollPane(details, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailsSP.getVerticalScrollBar().setUnitIncrement(16);
        detailsSP.setPreferredSize(new Dimension(500, 300));
        detailsSP.setBorder(new EmptyBorder(10, 0, 0, 0));
        SwingUtilities.invokeLater(() -> {
            detailsSP.getViewport().setViewPosition(new Point(0, 0));
        });
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        container.add(detailsSP, c);

        saveButton.addActionListener(new SaveButtonActionListener(brandField, nameField, skuField, availabilityBox, priceField, details));
        resetButton.addActionListener(new ResetButtonActionListener(brandField, nameField, skuField, availabilityBox, priceField, detailsSP));
        delButton.addActionListener(new DelButtonActionListener());

        clickedFrame.setContentPane(container);
        clickedFrame.pack();
        clickedFrame.setLocationRelativeTo(PetShop.getWindow().getFrame());
        clickedFrame.setVisible(true);
    }

    private String formatDetails(String details) {
        String formatted = details.replaceAll("    ", "")
                .replaceAll("\n", "")
                .replaceAll("  ", "")
                .replaceAll("<p></p>", "<br>")
                .replaceAll("<p>", "")
                .replaceAll("</p>", "");
        formatted = formatted.substring(25, formatted.length() - 14).replaceAll("<br>", "\\\\n");
        return formatted;
    }

    public File getImgFile() {
        return imgFile;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (PetShop.getWindow().getUserType() == UserType.ADMIN) {
            editDetails();
        } else {
            showDetails();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class SaveButtonActionListener implements ActionListener {
        private final JTextField brandField, nameField, skuField;
        private final JComboBox availabilityBox;
        private final JFormattedTextField priceField;
        private final JTextPane details;

        public SaveButtonActionListener(JTextField brandField, JTextField nameField, JTextField skuField, JComboBox availabilityBox, JFormattedTextField priceField, JTextPane details) {
            this.brandField = brandField;
            this.nameField = nameField;
            this.skuField = skuField;
            this.availabilityBox = availabilityBox;
            this.priceField = priceField;
            this.details = details;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String formatted = formatDetails(details.getText());
            if (brandField.getText().isEmpty() || nameField.getText().isEmpty() || skuField.getText().isEmpty() || priceField.getText().isEmpty() || formatted.isEmpty()) {
                JOptionPane.showMessageDialog(clickedFrame, "Please fill in all fields.", "Missing Values", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            ProductInfo newInfo = new ProductInfo(skuField.getText(), nameField.getText(), brandField.getText(), priceField.getText(), formatted);
            Product newProduct = new Product(newInfo, animalType, (Availability) availabilityBox.getSelectedItem(), imgFile.getName());
            PetShop.getProducts().updateProduct(Product.this, newProduct);
            clickedFrame.dispose();
            update();
        }
    }

    private void update() {
        File file = new File("res/products/" + animalType + "/" + animalType + ".txt");
        List<Product> typeProductList = PetShop.getProducts().getAllProducts().stream().filter(p -> p.animalType.equals(animalType)).collect(Collectors.toList());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Product p : typeProductList) {
                bw.write(p.getProductInfo().getSKU());
                bw.newLine();
                bw.write(p.getProductInfo().getName());
                bw.newLine();
                bw.write(p.getProductInfo().getBrand());
                bw.newLine();
                bw.write(p.getProductInfo().getPrice().toString());
                bw.newLine();
                bw.write(p.getProductInfo().getDetails());
                bw.newLine();
                bw.write(p.getAvailability().name());
                bw.newLine();
                bw.write(p.getImgFile().getName());
                bw.newLine();
                bw.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        PetShop.getProducts().loadAll();
        PetShop.getWindow().updatePage(PetShop.getWindow().getHomePage().getMainContainer());
    }

    private class ResetButtonActionListener implements ActionListener {
        private final JTextField brandField, nameField, skuField;
        private final JComboBox availabilityBox;
        private final JFormattedTextField priceField;
        private final JScrollPane detailsSP;

        public ResetButtonActionListener(JTextField brandField, JTextField nameField, JTextField skuField, JComboBox availabilityBox, JFormattedTextField priceField, JScrollPane detailsSP) {
            this.brandField = brandField;
            this.nameField = nameField;
            this.skuField = skuField;
            this.availabilityBox = availabilityBox;
            this.priceField = priceField;
            this.detailsSP = detailsSP;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            brandField.setText(productInfo.getBrand());
            nameField.setText(productInfo.getName());
            skuField.setText(productInfo.getSKU());
            availabilityBox.setSelectedItem(availability);
            priceField.setValue(productInfo.getPrice());
            ((JTextPane) detailsSP.getViewport().getComponent(0)).setText("<p>" + productInfo.getDetails().replaceAll("\\\\n", "<br>") + "</p>");
            SwingUtilities.invokeLater(() -> {
                detailsSP.getViewport().setViewPosition(new Point(0, 0));
            });
        }
    }

    private class DelButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PetShop.getProducts().removeProduct(Product.this);
            imgFile.delete();
            clickedFrame.dispose();
            update();
        }
    }

}
