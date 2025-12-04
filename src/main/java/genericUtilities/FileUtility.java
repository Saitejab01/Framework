package genericUtilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Provides generic utility methods for reading data from property files.
 * <p>
 * Supports reading key-value pairs from a specified properties file.
 * </p>
 * 
 * @author: Bandi Saiteja
 */
public class FileUtility {

    private String path;

    /**
     * Initializes the utility with the path of the properties file.
     * 
     * @param path The absolute or relative path to the properties file.
     */
    public FileUtility(String path) {
        this.path = path;
    }

    /**
     * Reads the value associated with the given key from the properties file.
     * 
     * @param key The key to look up in the properties file.
     * @return The value associated with the key, or null if the key does not exist.
     * @throws IOException If the properties file cannot be read.
     */
    public String readDataFromPropertiesFile(String key) throws IOException {
        try (FileInputStream fis = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.load(fis);
            return properties.getProperty(key);
        }
    }
}
