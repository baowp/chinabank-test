package cn.com.chinabank.shared.launch;

import cn.com.chinabank.shared.utility.LaunchUtil;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created with IntelliJ IDEA.
 * User: baowp
 * Date: 11/7/13
 * Time: 9:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpringContainer implements Container {

    private static final Logger logger = getLogger(SpringContainer.class);

    public static final String SPRING_CONFIG = "spring.config";

    public static final String DEFAULT_SPRING_CONFIG = "classpath:spring/spring-context.xml";

    static ClassPathXmlApplicationContext context;

    public static ClassPathXmlApplicationContext getContext() {
        return context;
    }

    public void start() {
        String configPath =  LaunchUtil.getProperty(SPRING_CONFIG);
        if (configPath == null || configPath.length() == 0) {
            configPath = DEFAULT_SPRING_CONFIG;
        }
        context = new ClassPathXmlApplicationContext(configPath.split("[,\\s]+"));
        context.start();
    }

    public void stop() {
        try {
            if (context != null) {
                context.stop();
                context.close();
                context = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

}
