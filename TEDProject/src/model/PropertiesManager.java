package model;

import java.io.File;
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
		properties.put("stopwordsFile", "engStopwords.csv");
		properties.put("stopwordsFileSep", ",");
		properties.put("sessionTimeoutMin", "20");
		properties.put("dateFormat", "dd/MM/yyyy");
		properties.put("timezoneOffset", "-180");
		properties.put("passwordHashingEnable", "true");
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
		// Create required folders:
		File dirPath = new File(getProperty("saveDir") + "/profile");
		if (! dirPath.exists()) { 
			dirPath.mkdirs();	
		}
		dirPath = new File(getProperty("saveDir") + "/article");
		if (! dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	public static String getProperty(String propName) {
		return properties.getProperty(propName);
	}
	
	public static void setProperty(String propName, String value) {
		properties.put(propName, value);
	}
	
}
