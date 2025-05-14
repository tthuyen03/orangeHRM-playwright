package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try {
            InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                throw new IOException("config.properties file not found");
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Cannot read config.properties: " + e.getMessage());
            throw new RuntimeException("Cannot read config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static boolean isRemote() {
        return Boolean.parseBoolean(get("REMOTE_ENABLED"));
    }

    public static String getGridUrl() {
        return "http://" + get("GRID_HOST") + ":" + get("GRID_PORT");
    }

    public static String getCdpUrl() {
        return getGridUrl().replace("http://", "ws://") + "/session";
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(get("HEADLESS"));
    }

    public static String getBrowser() {
        return get("BROWSER");
    }

    public static String getBaseUrl() {
        return get("BASE_URL");
    }

    public static int getTimeout() {
        return Integer.parseInt(get("TIMEOUT"));
    }

    public static String getBrowserVersion() {
        return get("BROWSER_VERSION");
    }
}
