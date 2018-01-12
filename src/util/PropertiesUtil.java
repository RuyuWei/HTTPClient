package util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static final Properties properties = new Properties();
    static {
         try {
          properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
         } catch (IOException e) { 
             System.out.println("初始config配置文件失败");
        }
    }
}