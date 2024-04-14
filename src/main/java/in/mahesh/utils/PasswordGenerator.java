package in.mahesh.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Controller;
@Controller
public class PasswordGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            int len = 12; // Change the length as needed
            String randomPassword = generatePassword(len);
            password.append(CHARACTERS.charAt(randomIndex));
            
        }

        return password.toString();
    }

}

