package net.seninp.executor.client;

import java.util.Date;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.ClusterJobResource;

public class JSONTestClient {

  public static void main(String[] args) {

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/executor/json");

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.APPLICATION_JSON);

    ClusterJobResource resource = cr.wrap(ClusterJobResource.class);

    //
    // Get the remote job as JSON
    ClusterJob job = resource.retrieve();
    if (job != null) {
      System.out.println("    jobId: " + job.getJobId());
      System.out.println("startTime: " + new Date(job.getStartTime()));
      System.out.println("      cmd: " + job.getCommand());
      System.out.println("   status: " + job.getStatus());
    }

  }

}