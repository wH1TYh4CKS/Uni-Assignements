package group3.Store;

import group3.PetShop;
import group3.Store.Products.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Orders {
    private File ordersFile;

    public Orders() {
        createOrdersFolder();
    }

    private void createOrdersFolder() {
        new File("res/Orders").mkdir();
    }

    public void setupFile() {
        try {
            ordersFile = new File("res/Orders/" + PetShop.getWindow().getUsername() + ".txt");
            if (!ordersFile.isFile()) ordersFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToOrders() {
        FileManager fileManager = new FileManager();
        String orderId = "OD" + (fileManager.getFileLength(UserType.ORDERS) + 1);
        StringBuilder order = new StringBuilder(orderId);

        for (Map.Entry<Product, Integer> entry : PetShop.getWindow().getCart().getCartMap().entrySet()) {
            order.append(",");
            order.append(entry.getKey().getProductInfo().getSKU());
            order.append(",");
            order.append(entry.getValue());
        }
        fileManager.appendFile(UserType.ORDERS, order.toString());

        String paymentId = "PM" + (fileManager.getFileLength(UserType.PAYMENTS) + 1);
        String payment = paymentId + "," +
                orderId +
                "," +
                PetShop.getWindow().getCart().getTotal() +
                "," +
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        fileManager.appendFile(UserType.PAYMENTS, payment);

        fileManager.appendFile(UserType.ORDER_STATUS, orderId + ",To Be Assign");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ordersFile, true))) {
            bw.write(orderId);
            bw.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PetShop.getWindow().getCart().clearCart();
    }
}
