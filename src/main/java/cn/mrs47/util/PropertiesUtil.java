package cn.mrs47.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author mrs47
 */
public class PropertiesUtil {

    public static String getProperty(String fileName, String Name) {
        Properties  pop = new Properties();
        try {
            pop.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pop.getProperty(Name);


    }

}
