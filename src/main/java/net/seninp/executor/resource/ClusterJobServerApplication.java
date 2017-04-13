package net.seninp.executor.resource;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import net.seninp.executor.service.SGDService;

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

    //
    // [1.0]
    // /json{jobId} reports a status of the job from database except the RUNNING state
    // -- in this case it checks with the cluster
    //
    router.attach("/json/{jobid}", ClusterJobServerResource.class);

    //
    // [2.0]
    // the /newjob URI expects JSON-formatted job to be POSTED for running with
    // username, command line, and resources (CPU and MEM) specified
    //
    //
    router.attach("/newjob", ClusterJobServerResource.class);

    //
    // [3.0]
    // querying QSUB itself
    //
    // Create the handler
    Restlet clusterJob = new Restlet() {
      @Override
      public void handle(Request request, Response response) {

        String jobId = String.valueOf(request.getAttributes().get("jobid"));

        String jobStatus = SGDService.getJobStatus(jobId);

        //
        String message = "Job status of job \"" + request.getAttributes().get("jobid") + "\": "
            + jobStatus;
        response.setEntity(message, MediaType.TEXT_PLAIN);
      }
    };
    router.attach("/jobstatus/{jobid}", clusterJob);

    //
    // voila!
    //
    return router;
  }

}
