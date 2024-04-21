package in.mahesh.utils;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Controller;
@Controller
public class PasswordGenerator {
	
	
	private PasswordGenerator() {
		super();
	}

	public static String getRandomString() {
        return getRandomString(8); // Default length is 8
    }

 public static String getRandomString(int length) {

	    SecureRandom random = new SecureRandom();
	    byte[] bytes = new byte[length];
	    random.nextBytes(bytes);
	    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);

}
}

