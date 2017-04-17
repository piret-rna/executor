package net.seninp.executor.resource;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.routing.Router;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.seninp.executor.job.SGDClusterJob;
import net.seninp.executor.util.StackTrace;

/**
 * The main Executor app.
 * 
 * @author psenin
 *
 */
public class ClusterJobServerApplication extends Application {

  final static Logger logger = Logger.getLogger(ClusterJobServerApplication.class);

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    // [1.0] list of jobs
    //
    Restlet clusterJob = new Restlet() {
      @Override
      public void handle(Request request, Response response) {

        // decode the request
        //
        Method method = request.getMethod();
        Form params = request.getResourceRef().getQueryAsForm();
        logger.info("recieved a " + method + " request with parameters " + params.toString());

        // POST -- get the new job configuration
        //
        if (Method.POST.equals(method)) {

          // try to decode the POST -- must be a JSONized new job
          try {

            // handle the JSON to ClusterJob conversion
            String jsonRepresentation = request.getEntityAsText();
            ObjectMapper mapper = new ObjectMapper();
            ClusterJob newJob = mapper.readValue(jsonRepresentation, ClusterJob.class);
            logger.info("new job to be executed: " + newJob);

            // execute the job
            SGDClusterJob sgdJob = new SGDClusterJob(newJob);
            ClusterJob updatedJob = sgdJob.execute();

            // handle the response to the client
            JacksonRepresentation<ClusterJob> responseJobRepresentation = new JacksonRepresentation<ClusterJob>(
                updatedJob);
            response.bufferEntity();
            response.setStatus(Status.SUCCESS_CREATED);
            response.setEntity(responseJobRepresentation);
            response.commit();

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

        }

        // Form params = request.getResourceRef().getQueryAsForm();
        // if params are empty,
        // System.out.println(method + " : " + params.toString());
        // String jobId = String.valueOf(request.getAttributes().get("jobid"));
        //
        // String jobStatus = SGDService.getJobStatus(jobId);
        //
        // //
        // String message = "Job status of job \"" + request.getAttributes().get("jobid") + "\": "
        // + jobStatus;
        // response.setEntity(message, MediaType.TEXT_PLAIN);
      }
    };
    router.attach("/jobs", clusterJob);

    //
    // [1.0]
    //
    // GET and DELETE processed directly by the resource
    //
    router.attach("/jobs/{jobid}", ClusterJobServerResource.class);

    //
    // voila!
    //
    return router;
  }

}
