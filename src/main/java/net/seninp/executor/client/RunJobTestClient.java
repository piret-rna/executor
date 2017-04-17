package net.seninp.executor.client;

import java.io.IOException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.util.StackTrace;

/**
 * An example of the run job client.
 * 
 * @author psenin
 *
 */
public class RunJobTestClient {

  private static final String URI = "http://localhost:8181/executor/jobs";

  /**
   * Accepts three arguments -- (1) the command line (2) the CPU resource requirements and (3) the
   * Memory requirements.
   * 
   * @param args
   */
  public static void main(String[] args) {

    // check the CLI params
    //
    if (args.length != 3) {
      System.err.println("You need to provide three arguments for this code to function.");
      System.exit(10);
    }

    // parse the CLI parameters
    //
    String commandLine = args[0];
    Integer cpuNum = Integer.valueOf(args[1]);
    Integer memGB = Integer.valueOf(args[2]);

    System.out.println("Attempting to execute \"" + commandLine + "\" using " + cpuNum + "CPUs and "
        + memGB + "GB of memory, " + " using \"" + URI + "\"");

    // create the new job object
    //
    ClusterJob newJob = new ClusterJob();
    newJob.setUsername("psenin");
    newJob.setCommand(commandLine);
    newJob.setResourceCpu(cpuNum);
    newJob.setResourceMem(memGB);

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource(URI);

    // preventing chunk encoding
    cr.setRequestEntityBuffering(true);

    // configure resource to accept JSON-like response
    cr.accept(MediaType.APPLICATION_JSON);

    // perform POST
    try {

      // post the request and get the server's response
      Representation res = cr.post(newJob);

      // check for the updated job or an error message
      ObjectMapper mapper = new ObjectMapper();
      ClusterJob updatedJob = mapper.readValue(res.getText(), ClusterJob.class);
      System.out.println(updatedJob);
    }
    catch (ResourceException e) {
      System.err.println("The client is unable to reach executor at " + URI
          + " or other communication exception occurred: " + StackTrace.toString(e));
    }
    catch (JsonParseException e) {
      System.err.println("Unable to parse JSON response: " + StackTrace.toString(e));
    }
    catch (JsonMappingException e) {
      System.err.println("Unable to map JSON from response: " + StackTrace.toString(e));

    }
    catch (IOException e) {
      System.err.println("Another IO exception occured: " + StackTrace.toString(e));
    }
    finally {
      assert true;
    }

  }
}
