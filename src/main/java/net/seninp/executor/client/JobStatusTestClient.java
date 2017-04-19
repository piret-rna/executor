package net.seninp.executor.client;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import com.beust.jcommander.JCommander;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import net.seninp.executor.ExecutorServerProperties;
import net.seninp.executor.util.StackTrace;

public class JobStatusTestClient {

  final static Logger logger = Logger.getLogger(RunJobTestClient.class);

  private static final String JOBS_CONTEXT_URI = "jobs";

  private static final Object CR = "\n";

  public static void main(String[] args) {

    // take care about properties
    ExecutorServerProperties properties = new ExecutorServerProperties();
    System.err.println(properties.echoProperties());
    String URI = properties.getFullHost() + JOBS_CONTEXT_URI;
    logger.info("job execution client to use \"" + URI + "\" URI");

    JobStatusArgs params = new JobStatusArgs();
    @SuppressWarnings("unused")
    JCommander jct = new JCommander(params, args);

    StringBuffer sb = new StringBuffer(1024);
    sb.append("PiReT Executor CLI client").append(CR);
    sb.append("  querying jobs using specified parameters:").append(CR);

    sb.append("    executor URI:   ").append(URI).append(CR);
    sb.append("    user name:      ").append(params.user).append(CR);
    sb.append("    interval start: ").append(params.start).append(CR);
    sb.append("    interval end:   ").append(params.end).append(CR);
    sb.append("    job id:         ").append(params.jobId).append(CR);

    System.out.println(sb.toString());

    try {
      //
      // no params specified -- get the list of running jobs
      if (0 == args.length) {

        logger.info(
            "No arguments provided, printing the list of current (running and enqueued) jobs");
        ClientResource cr = new ClientResource(URI);
        cr.accept(MediaType.TEXT_PLAIN);

        // perform GET
        Representation response = cr.get();

        // TODO: got to process jobs list here
        // check for the updated job or an error message
        // ObjectMapper mapper = new ObjectMapper();
        // mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //
        // String res = mapper.writeValueAsString(response);

        System.out.println(response.getText());

        System.exit(0);
      }
      else if (null != params.jobId) {

        logger.info("Querying status of the job with id " + params.jobId
            + ", ignoring all other parameters");

        String jobIdURI = URI + "/" + params.jobId;
        logger.debug("Querying " + jobIdURI);

        // Initialize the resource proxy.
        ClientResource cr = new ClientResource(jobIdURI);

        cr.setRequestEntityBuffering(true);
        cr.accept(MediaType.ALL);

        Representation responseEntity = cr.get();

        System.out.println(responseEntity.getText());
      }

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
    catch (Exception e) {
      System.err.println("Unknown exception occurred: " + StackTrace.toString(e));
    }
    finally {
      assert true;
    }

  }

}
