package net.seninp.executor.client;

import java.io.IOException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import net.seninp.executor.util.StackTrace;

public class JobStatusTestClient {

  public static void main(String[] args) {

    String jobId = args[0];

    System.out.println("Querying status of the job with id " + jobId);

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/executor/jobstatus/" + jobId);

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.TEXT_PLAIN);

    Representation responseEntity = cr.get();

    String text;
    try {
      text = responseEntity.getText();

      if (text.toLowerCase().contains("jobs do not exist or permissions are not sufficient")) {
        System.out.println("Unknown job (" + jobId + ") queried or no permission ...");
      }
      else {
        System.out.println("Unknown response recieved: " + text);
      }
    }
    catch (IOException e) {
      System.err.println("Exception thrown: " + StackTrace.toString(e));
    }

  }
}
