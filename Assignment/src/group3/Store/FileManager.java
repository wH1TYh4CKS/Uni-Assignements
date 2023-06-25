package group3.Store;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class FileManager {

    private final String ADMIN_FILE = "Admin.txt";
    private final String DELIVERY_STAFF_FILE = "Delivery_staff.txt";
    private final String CUSTOMER_FILE = "Customer.txt";
    private final String ORDER_STATUS_FILE = "OrderStatus.txt";
    private final String ORDERS_FILE = "Orders.txt";
    private final String PAYMENTS_FILE = "Payments.txt";
    private final HashMap<UserType, String> FILE_MAP;

    public FileManager() {
        FILE_MAP = new HashMap<>();
        FILE_MAP.put(UserType.ADMIN, ADMIN_FILE);
        FILE_MAP.put(UserType.DELIVERY_STAFF, DELIVERY_STAFF_FILE);
        FILE_MAP.put(UserType.CUSTOMER, CUSTOMER_FILE);
        FILE_MAP.put(UserType.ORDER_STATUS, ORDER_STATUS_FILE);
        FILE_MAP.put(UserType.ORDERS, ORDERS_FILE);
        FILE_MAP.put(UserType.PAYMENTS, PAYMENTS_FILE);
    }

    public String[][] readFile(UserType userType) {
        List<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILE_MAP.get(userType)))) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return lines.stream().map(line -> line.split(",")).toArray(String[][]::new);
    }

    public void writeFile(UserType userType, List<String> data) {
        String fileName = FILE_MAP.get(userType);
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (String line : data) {
                fileWriter.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to file: " + fileName);
        }
    }

    public void appendFile(UserType userType, String line) {
        String fileName = FILE_MAP.get(userType);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + fileName);
            e.printStackTrace();
        }
    }

    public boolean isExistingUsername(UserType userType, String username) {
        String fileName = FILE_MAP.get(userType);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void checkFiles() {
        for (UserType userType : UserType.values()) {
            String fileName = FILE_MAP.get(userType);
            if (!Files.exists(Paths.get(fileName))) {
                System.out.println(fileName + " file not found, creating file now...");
                createFile(userType);
            }
        }
    }

    private void createFile(UserType userType) {
        String fileName = FILE_MAP.get(userType);
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println(fileName + " file created successfully.");
            } else {
                System.out.println(fileName + " file already exists.");
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + fileName);
            e.printStackTrace();
        }
    }

    public String getOrderStatusFile() {
        return ORDER_STATUS_FILE;
    }

    public int getFileLength(UserType type) {
        int count = 0;
        try {
            count = (int) Files.lines(new File(FILE_MAP.get(type)).toPath()).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String[] getUserData(String username) {
        String[][] allData = readFile(UserType.CUSTOMER);
        return Arrays.stream(allData).filter(u -> u[0].equals(username)).collect(Collectors.toList()).get(0);
    }
}
