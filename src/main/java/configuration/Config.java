package configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static boolean isRemote(){
        return "remote".equalsIgnoreCase(get("TARGET"));
    }

    public static String getRemoteURL(){
        return "ws://" + get("REMOTE_URL") + ":" + get("REMOTE_PORT");
    }

    public static boolean isHeadless(){
        return Boolean.parseBoolean(get("HEADLESS"));
    }
}
