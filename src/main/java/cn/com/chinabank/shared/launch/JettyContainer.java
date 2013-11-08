package cn.com.chinabank.shared.launch;

import cn.com.chinabank.shared.utility.LaunchUtil;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.servlet.DispatcherServlet;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created with IntelliJ IDEA.
 * User: baowp
 * Date: 11/7/13
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class JettyContainer implements Container {

    private static final Logger logger = getLogger(JettyContainer.class);

    public static final String JETTY_PORT = "jetty.port";

    public static final int DEFAULT_JETTY_PORT = 8080;

    SelectChannelConnector connector;

    public void start() {
        String serverPort = LaunchUtil.getProperty(JETTY_PORT);
        int port;
        if (serverPort == null || serverPort.length() == 0) {
            port = DEFAULT_JETTY_PORT;
        } else {
            port = Integer.parseInt(serverPort);
        }

        connector = new SelectChannelConnector();
       // connector.setMaxIdleTime(1000);
       // connector.setAcceptors(10);
        connector.setPort(port);
       // connector.setConfidentialPort(8443);

        Server server = new Server();
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler handler = new ServletContextHandler(server, "/", true, false);

        ServletHolder servletHolder = new ServletHolder("rest", DispatcherServlet.class);
        servletHolder.setInitParameter("contextConfigLocation", "classpath:spring-servlet.xml");

        handler.addServlet(servletHolder, "/*");

        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to start jetty server on " + "NetUtils.getLocalHost()" + ":" + port + ", cause: " + e.getMessage(), e);
        }
    }

    public void stop() {
        try {
            if (connector != null) {
                connector.close();
                connector = null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

}
