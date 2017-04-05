package net.seninp.executor;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

public class ClusterJobServerTestClient {

  public static void main(String[] args) {

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/jobservice/json");

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.APPLICATION_JSON);

    ClusterJobResource resource = cr.wrap(ClusterJobResource.class);

    // Get the remote contact
    ClusterJob job = resource.retrieve();
    if (job != null) {
      System.out.println("    jobId: " + job.getJobId());
      System.out.println("startTime: " + job.getStartTime());
      System.out.println("      cmd: " + job.getCommand());
      System.out.println("   status: " + job.getStatus());
    }

    // Update the contact
    job.setJobId(77l);
    resource.store(job);

  }

}