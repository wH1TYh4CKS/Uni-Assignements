package group3.Customer.Register;

import group3.Store.FileManager;
import group3.Store.UserType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register {
    private final FileManager fileManager = new FileManager();

    public Register() {

    }

    public boolean validateInputs(String username, String password, String age, String gender, String phoneNumber, String email, String address) {
        // Check if any of the inputs are empty or null
        if (Trimmer(username, password, age)) return false;
        if (Trimmer(gender, phoneNumber, email)) return false;
        if (address == null || address.trim().length() == 0) return false;

        // Check if the username is already in use
        return !fileManager.isExistingUsername(UserType.CUSTOMER, username);
    }

    public boolean Trimmer(String username, String password, String age) {
        if (username == null || username.trim().length() == 0) return true;
        if (password == null || password.trim().length() == 0) return true;
        return age == null || age.trim().length() == 0;
    }

    public boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasUpperCase = false;
        boolean hasSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialCharacter = true;
            if (c == ',') return false;
        }
        return hasUpperCase && hasSpecialCharacter;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void register(String username, String password, String age, String gender, String phoneNumber, String email, String address) {
        if (validateInputs(username, password, age, gender, phoneNumber, email, address)) {
            String customerData = username + "," + password + "," + age + "," + gender + "," + phoneNumber + "," + email + "," + address;
            fileManager.appendFile(UserType.CUSTOMER, customerData);
        }
    }
}
