package net.seninp.executor.resource;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.job.SGDService;

public class ClusterJobServerApplication extends Application {

  @Override
  public Restlet createInboundRoot() {

    // init the DB connection
    ExecutorDB.connect("");

    Router router = new Router(getContext());

    router.attach("/json/{jobid}", ClusterJobServerResource.class);
    
    router.attach("/newjob", ClusterJobServerResource.class);

    // Create the account handler
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

    return router;
  }

}
