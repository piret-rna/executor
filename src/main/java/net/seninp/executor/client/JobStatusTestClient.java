package net.seninp.executor.client;

import java.io.IOException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

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
      System.out.println(text);
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}