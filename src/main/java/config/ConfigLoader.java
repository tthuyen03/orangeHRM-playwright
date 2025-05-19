package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties;
    private static final String ENV = System.getProperty("env", "local");
    private static final String CONFIG_FILE = "src/main/resources/config." + ENV + ".properties";

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + CONFIG_FILE + " file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static String getEnvironment() {
        return ENV;
    }
} 