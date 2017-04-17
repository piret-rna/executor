package net.seninp.executor.resource;

import java.io.IOException;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.routing.Router;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The main Executor app.
 * 
 * @author psenin
 *
 */
public class ClusterJobServerApplication extends Application {

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    // [1.0] list of jobs
    //
    Restlet clusterJob = new Restlet() {
      @Override
      public void handle(Request request, Response response) {

        Method method = request.getMethod();

        // POST -- get the new job configuration
        //
        if (Method.POST.equals(method)) {

          String jsonRepresentation = request.getEntityAsText();

          ObjectMapper mapper = new ObjectMapper();

          ClusterJob newJob;
          try {
            newJob = mapper.readValue(jsonRepresentation, ClusterJob.class);
            System.out.println(newJob);
            newJob.setJobId(775);

            JacksonRepresentation<ClusterJob> updatedJob = new JacksonRepresentation<ClusterJob>(
                newJob);
            response.bufferEntity();
            response.setStatus(Status.SUCCESS_CREATED);
            response.setEntity(updatedJob);
            response.commit();

          }
          catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

        }

        Form params = request.getResourceRef().getQueryAsForm();

        // if params are empty,

        System.out.println(method + " : " + params.toString());

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
