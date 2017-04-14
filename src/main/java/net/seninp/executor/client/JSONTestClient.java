package net.seninp.executor.client;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.ClusterJobResource;
import net.seninp.executor.util.StackTrace;

public class JSONTestClient {

  public static void main(String[] args) {

    if (args.length < 1 || args.length != 1) {
      System.err.println("You need to provide a job Id for this code to function.");
      System.exit(10);
    }

    String jobId = args[0];

    System.out.println("Querying JSON for the job " + jobId);
    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/executor/json/" + args[0]);

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.APPLICATION_JSON);

    ClusterJobResource resource = cr.wrap(ClusterJobResource.class);

    //
    // Get the remote job as JSON
    ClusterJob job = (ClusterJob) resource.retrieve();
    if (job != null) {
      try {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String jsonString = mapper.writeValueAsString(job);
        System.out.println("Recieved the following JSON:\n" + jsonString);
      }
      catch (JsonProcessingException e) {
        System.err.println("Exception thrown while formatting JSON: " + StackTrace.toString(e));
      }

    }

  }

}