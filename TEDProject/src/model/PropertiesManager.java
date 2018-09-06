package model;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {
	
	private static Properties properties = new Properties();
	
	static {
		// Setting defaults:
		properties.put("hostname", "localhost");
		properties.put("port", "8443");
		properties.put("protocol", "https");
		properties.put("dbURL", "jdbc:mysql://localhost:3306/TED?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
		properties.put("dbUser", "myuser");
		properties.put("dbPassword", "MYUSERSQL");
		properties.put("saveDir", "D:/TEDProjectStorage");
		properties.put("dateFormat", "dd/MM/yyyy");
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream("config.properties");
			properties.load(in);
			in.close();
			System.out.println("Successfully loaded properties from config.properties.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load properties from config.properties - Using default properties.");
		}
	}

	public static String getProperty(String propName) {
		return properties.getProperty(propName);
	}
	
}
