package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordManager {
	
	public static String getHashSHA256(String text) {
		try {
			// Append pre-defined salt to text then hash it using SHA-256 algorithm
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			String saltedText = text + PropertiesManager.getProperty("passwordHashSalt");
			byte[] hash = digest.digest(saltedText.getBytes(StandardCharsets.UTF_8));
			return MyUtil.bytesToHex(hash);
		} catch (Exception e) {			// shouldn't ever happen
			System.out.println("Failed to hash. Returning original text.");
			e.printStackTrace();
		    return text;
		}
	}
	
}
