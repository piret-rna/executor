package net.seninp.executor;

import org.apache.log4j.Logger;
import org.restlet.Component;
import org.restlet.data.Protocol;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.resource.ClusterJobServerApplication;

/**
 * Main server's executable.
 * 
 * @author psenin
 *
 */
public class ExecutorServer {

  final static Logger logger = Logger.getLogger(ExecutorServer.class);

  public static void main(String[] args) throws Exception {

    // take care about properties
    ExecutorServerProperties properties = new ExecutorServerProperties();
    System.err.println(properties.echoProperties());

    // make sure the DB is initialized
    logger.info("attempting to initialize the DB");
    ExecutorDB.connect();

    // Create a new Component.
    logger.info("instantiating the component");
    Component component = new Component();

    // Add a new HTTP server listening on port 8181.
    logger.info("attaching the component to the port");
    component.getServers().add(Protocol.HTTP, 8181);

    // Attach the sample application.
    logger.info("attaching the Application to the /"
        + System.getProperty(ExecutorServerProperties.CONTEXT_ROOT_KEY) + " URI");
    component.getDefaultHost().attach(
        "/" + System.getProperty(ExecutorServerProperties.CONTEXT_ROOT_KEY),
        new ClusterJobServerApplication());

    // Start the component.
    logger.info("going live");
    component.start();
  }

}
