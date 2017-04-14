package net.seninp.executor.resource;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.routing.Router;

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
