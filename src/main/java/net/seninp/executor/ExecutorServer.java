package net.seninp.executor;

import org.restlet.Component;
import org.restlet.data.Protocol;
import net.seninp.executor.resource.ClusterJobServerApplication;

/**
 * Main server's executable.
 * 
 * @author psenin
 *
 */
public class ExecutorServer {

  public static void main(String[] args) throws Exception {

    // Create a new Component.
    Component component = new Component();

    // Add a new HTTP server listening on port 8181.
    component.getServers().add(Protocol.HTTP, 8181);

    // Attach the sample application.
    component.getDefaultHost().attach("/executor", new ClusterJobServerApplication());

    // Start the component.
    component.start();
  }

}
