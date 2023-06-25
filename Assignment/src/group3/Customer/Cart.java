package group3.Customer;

import group3.PetShop;
import group3.Store.Pages.Section;
import group3.Store.Products.Availability;
import group3.Store.Products.Product;
import group3.Store.UserType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Cart extends Section {
    private final HashMap<Product, Integer> cartMap;
    private BigDecimal subtotal;
    private final DecimalFormat df = new DecimalFormat("#,##0.00");
    private File cartFile;

    public Cart() {
        super(new GridBagLayout());
        setupCartFolder();
        cartMap = new HashMap<>();
        subtotal = BigDecimal.ZERO;
    }

    public void addToCart(Product product, int amount) {
        if (PetShop.getWindow().getUserType() != UserType.CUSTOMER) {
            JOptionPane.showMessageDialog(product.getClickedFrame(), "Please log in.", "Add to Cart Failed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (product.getAvailability() == Availability.OUT_OF_STOCK) {
            JOptionPane.showMessageDialog(product.getClickedFrame(), "Product is out of stock.", "Add to Cart Failed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (cartMap.containsKey(product)) cartMap.replace(product, cartMap.get(product) + amount);
        else cartMap.put(product, amount);
        writeToCart();

        JOptionPane pane = new JOptionPane("Item has been added to cart!", JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(product.getClickedFrame(), "Add to Cart Successful");
        Timer timer = new Timer(500, e -> {
            dialog.dispose();
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
    }

    public void updateCartButton() {
        AtomicInteger totalAmount = new AtomicInteger(0);
        cartMap.values().forEach(totalAmount::addAndGet);

        PetShop.getWindow().getCartButton().setText("Cart (" + totalAmount.get() + ")");
    }

    @Override
    public JPanel getMainContainer() {
        mainContainer.removeAll();
        subtotal = BigDecimal.ZERO;
        GridBagConstraints c = new GridBagConstraints();
        if (cartMap.isEmpty()) {
            JLabel cartEmpty = new JLabel("<html><h1>Cart is empty.</h1></html>");
            mainContainer.add(cartEmpty, c);
            return mainContainer;
        }

        JScrollPane jsp = getJSP();
        fillJSP(jsp);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 15, 0, 15);
        mainContainer.add(jsp, c);

        JPanel orderSummary = createOrderSummary();

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.insets = new Insets(40, 0, 30, 30);
        c.anchor = GridBagConstraints.LINE_END;
        mainContainer.add(orderSummary, c);

        return mainContainer;
    }

    private JScrollPane getJSP() {
        JPanel productsContainer = new JPanel(new GridLayout(0, 1, 0, 5));
        JScrollPane jsp = new JScrollPane(productsContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.getVerticalScrollBar().setUnitIncrement(16);
        Dimension jspDim = new Dimension(1000, 400);
        jsp.setPreferredSize(jspDim);
        jsp.setMaximumSize(jspDim);
        jsp.setMinimumSize(jspDim);
        jsp.setBorder(new EmptyBorder(0, 0, 0, 0));

        return jsp;
    }

    private void fillJSP(JScrollPane jsp) {
        JPanel productsContainer = (JPanel) jsp.getViewport().getView();

        JLabel proDescHeader = new JLabel("<html><h2>Product Description</h2></html>");
        proDescHeader.setPreferredSize(new Dimension(400, 30));
        proDescHeader.setHorizontalAlignment(JLabel.CENTER);
        JLabel quantityHeader = new JLabel("<html><h2>Quantity</h2></html>");
        JLabel priceHeader = new JLabel("<html><h2>Price</h2></html>");
        JLabel totalHeader = new JLabel("<html><h2>Total</h2></html>");
        JButton emptyCart = new JButton("Empty Cart");
        emptyCart.addActionListener(e -> {
            clearCart();
        });

        jsp.setColumnHeaderView(getRow(proDescHeader, quantityHeader, priceHeader, totalHeader, emptyCart));

        for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
            JLabel proDesc = new JLabel();
            proDesc.setIcon(new ImageIcon(entry.getKey().getImg().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            proDesc.setText("<html>" +
                    "<style>" +
                    "div { font-size: 16pt; }" +
                    ".brand { margin-bottom: 10px; }" +
                    "</style>" +
                    "<div class='brand'>" + entry.getKey().getProductInfo().getBrand() + "</div>" +
                    "<div>" + entry.getKey().getProductInfo().getName() + "</div>" +
                    "</html>");
            proDesc.setIconTextGap(15);

            SpinnerNumberModel value = new SpinnerNumberModel();
            value.setValue(entry.getValue());
            if (entry.getKey().getAvailability() == Availability.OUT_OF_STOCK) {
                value.setMaximum(entry.getValue());
            }
            value.setMinimum(1);
            JSpinner quantity = new JSpinner(value);
            quantity.addChangeListener(e -> {
                entry.setValue((int) ((JSpinner) e.getSource()).getValue());
                writeToCart();
                updateCartButton();
                PetShop.getWindow().updatePage(getMainContainer());
            });

            JLabel price = new JLabel("<html><h2>" + entry.getKey().getProductInfo().getPriceText() + "</h2></html>");

            BigDecimal productTotal = entry.getKey().getProductInfo().getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
            subtotal = subtotal.add(productTotal);
            JLabel subtotalLabel = new JLabel("<html><h2>RM" + df.format(productTotal) + "</h2></html>");

            JButton remove = new JButton("X");
            remove.addActionListener(e -> {
                cartMap.remove(entry.getKey());
                writeToCart();
                updateCartButton();
                PetShop.getWindow().updatePage(getMainContainer());
            });

            productsContainer.add(getRow(proDesc, quantity, price, subtotalLabel, remove));
        }
    }

    public BigDecimal calcDeliveryFee(BigDecimal subtotal) {
        BigDecimal deliveryFee = BigDecimal.valueOf(5).add(subtotal.multiply(BigDecimal.valueOf(0.05))).setScale(2, RoundingMode.CEILING);
        return subtotal.compareTo(BigDecimal.valueOf(100)) >= 0 ? BigDecimal.ZERO : deliveryFee;
    }

    public JPanel getOrderSummary(BigDecimal subtotal, DecimalFormat df) {
        JPanel orderSummary = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.fill = GridBagConstraints.NONE;
        JLabel title = new JLabel("<html><h1>Order Summary</h1></html>");
        orderSummary.add(title, c);

        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        JSeparator jSeparator = new JSeparator();
        jSeparator.setForeground(Color.black);
        orderSummary.add(jSeparator, c);

        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JLabel subtotalText = new JLabel("<html><h2>Subtotal</h2></html>");
        orderSummary.add(subtotalText, c);

        c.gridy = 3;
        JLabel deliveryFeeText = new JLabel("<html><h2>Delivery Fee</h2></html>");
        orderSummary.add(deliveryFeeText, c);

        c.gridy = 4;
        JLabel totalPaymentText = new JLabel("<html><h2>Total Payment</h2></html>");
        orderSummary.add(totalPaymentText, c);

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        orderSummary.add(Box.createRigidArea(new Dimension(150, 0)), c);

        c.gridx = 1;
        c.gridy = 2;
        JLabel subtotalAmount = new JLabel("<html><h2>RM" + df.format(subtotal) + "</h2><html>");
        orderSummary.add(subtotalAmount, c);

        c.gridy = 3;
        BigDecimal deliveryFee = calcDeliveryFee(subtotal);
        JLabel deliveryFeeAmount = new JLabel("<html><h2>" + (deliveryFee.compareTo(BigDecimal.ZERO) == 0 ? "Free Delivery" : "RM" + deliveryFee) + "</h2><html>");
        orderSummary.add(deliveryFeeAmount, c);

        c.gridy = 4;
        BigDecimal totalPayment = subtotal.add(deliveryFee);
        JLabel totalPaymentAmount = new JLabel("<html><h2>RM" + df.format(totalPayment) + "</h2></html>");
        orderSummary.add(totalPaymentAmount, c);

        return orderSummary;
    }

    private JPanel createOrderSummary() {
        JPanel orderSummary = getOrderSummary(subtotal, df);
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(10, 0, 0, 0);
        JButton checkoutButton = new JButton("<html><h2>Checkout<html><h2>");
        checkoutButton.setPreferredSize(new Dimension(0, 50));
        BigDecimal finalSubtotal = subtotal;
        checkoutButton.addActionListener(e -> {
            Checkout checkout = new Checkout(getOrderSummary(finalSubtotal, df));
            PetShop.getWindow().updatePage(checkout.getMainContainer());
        });
        orderSummary.add(checkoutButton, c);

        return orderSummary;
    }

    private JPanel getRow(JLabel proDesc, JComponent quantity, JLabel price, JLabel subtotal, JButton remove) {
        JPanel row = new JPanel();

        if (proDesc.getPreferredSize().height == 100) proDesc.setPreferredSize(new Dimension(400, 100));
        row.add(proDesc);

        row.add(Box.createRigidArea(new Dimension(25, 0)));

        quantity.setPreferredSize(new Dimension(75, 30));
        quantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.add(quantity);

        row.add(Box.createRigidArea(new Dimension(25, 0)));

        price.setPreferredSize(new Dimension(100, 30));
        price.setHorizontalAlignment(JLabel.CENTER);
        row.add(price);

        row.add(Box.createRigidArea(new Dimension(25, 0)));

        subtotal.setPreferredSize(new Dimension(125, 30));
        subtotal.setHorizontalAlignment(JLabel.CENTER);
        row.add(subtotal);

        row.add(Box.createRigidArea(new Dimension(25, 0)));

        JPanel buttonCon = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        buttonCon.setPreferredSize(new Dimension(100, 30));
        c.gridx = 0;
        c.gridy = 0;
        buttonCon.add(remove, c);
        row.add(buttonCon);

        return row;
    }

    private void setupCartFolder() {
        new File("res/Carts").mkdir();
    }

    public void clearCartMap() {
        cartMap.clear();
    }

    public void clearCart() {
        cartMap.clear();
        writeToCart();
        updateCartButton();
        PetShop.getWindow().updatePage(getMainContainer());
    }

    public void loadCart() {
        try {
            cartFile = new File("res/Carts/" + PetShop.getWindow().getUsername() + ".txt");
            if (!cartFile.isFile()) {
                cartFile.createNewFile();
            }

            BufferedReader in = new BufferedReader(new FileReader(cartFile));
            if (in.readLine() == null) return;
            in.close();

            in = new BufferedReader(new InputStreamReader(Files.newInputStream(cartFile.toPath()), StandardCharsets.ISO_8859_1));
            int cartCount = (int) Files.lines(cartFile.toPath()).count() / 2;
            for (int i = 0; i < cartCount; i++) {
                String sku = in.readLine();
                if (sku == null) continue;
                int amount = Integer.parseInt(in.readLine());
                Product product = PetShop.getProducts().getAllProducts().get(PetShop.getProducts().getSKUList().indexOf(sku));
                cartMap.put(product, amount);
                in.readLine();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeToCart() {
        try {
            PrintWriter writer = new PrintWriter(cartFile);
            if (cartMap.isEmpty()) writer.print("");
            else {
                for (Map.Entry<Product, Integer> entry : cartMap.entrySet()) {
                    writer.println(entry.getKey().getProductInfo().getSKU());
                    writer.println(entry.getValue());
                    writer.println();
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<Product, Integer> getCartMap() {
        return cartMap;
    }

    public BigDecimal getTotal() {
        return subtotal.add(calcDeliveryFee(subtotal));
    }
}
