package dpc.utilities;

import dpc.exceptions.PropertyLoaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jiaweizhang on 10/2/2016.
 * http://codippa.com/how-to-load-property-file-in-java/
 */
public class PropertiesLoader {
    static Properties loadPropertiesFromPackage(String filePath) {
        Properties prop = new Properties();
        try (InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            prop.load(in);
            return prop;
        } catch (IOException e) {
            throw new PropertyLoaderException();
        }
    }
}
