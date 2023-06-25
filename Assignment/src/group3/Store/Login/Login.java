package group3.Store.Login;

import group3.Admin.AdminPage;
import group3.DeliveryStaff.DeliveryStaffPage;
import group3.PetShop;
import group3.Store.FileManager;
import group3.Store.UserType;

import java.util.ArrayList;
import java.util.List;

public class Login {
    private final FileManager fileManager;

    public Login() {
        this.fileManager = new FileManager();
    }

    public boolean checkInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return false;
        }
        return true;
    }

    public UserType identifyUser(String username) {
        if (username.startsWith("AD")) {
            return UserType.ADMIN;
        } else if (username.startsWith("DS")) {
            return UserType.DELIVERY_STAFF;
        } else {
            return UserType.CUSTOMER;
        }
    }

    public boolean checkCredentials(String username, String password) {
        UserType userType = identifyUser(username);
        String[][] usersArray = fileManager.readFile(userType);
        List<String> usersList = new ArrayList<>();
        for (String[] user : usersArray) {
            usersList.add(user[0] + "," + user[1]);
        }

        if (userType != UserType.CUSTOMER && usersList.isEmpty()) {
            System.out.println("No " + userType.getType() + " user found. Adding new user.");
            fileManager.appendFile(userType, username + "," + password);
            return true;
        }

        for (String user : usersList) {
            String[] credentials = user.split(",");
            if (credentials[0].equals(username) && credentials[1].equals(password)) {
                return true;
            }
        }
        System.out.println("Username and password did not match.");
        return false;
    }

    public boolean login(String username, String password) {
        if (checkInput(username, password) && checkCredentials(username, password)) {
            UserType userType = identifyUser(username);
            PetShop.getWindow().setUserType(userType);
            PetShop.getWindow().refreshWindow();
            switch (userType) {
                case ADMIN:
                    AdminPage adminPage = new AdminPage();
                    adminPage.show();
                    break;
                case DELIVERY_STAFF:
                    DeliveryStaffPage deliveryStaffPage = new DeliveryStaffPage();
                    deliveryStaffPage.show();
                    break;
                case CUSTOMER:
                    PetShop.getWindow().setUsername(username);
                    PetShop.getWindow().getCart().loadCart();
                    PetShop.getWindow().getCart().updateCartButton();
                    PetShop.getWindow().getOrders().setupFile();
                    break;
                default:
                    System.out.println("Invalid user type.");
                    break;
            }
            return true;
        } else {
            System.out.println("Login failed.");
            return false;
        }
    }

}