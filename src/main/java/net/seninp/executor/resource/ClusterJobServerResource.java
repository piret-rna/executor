package net.seninp.executor.resource;

import org.restlet.resource.ServerResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.service.SGDService;

/**
 * The server side implementation of the Restlet resource.
 */
public class ClusterJobServerResource extends ServerResource implements ClusterJobResource {

  public ClusterJobServerResource() {
    super();
  }

  @Override
  public void run(ClusterJob job) {

    //
    // print a debug message
    try {
      com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
      String jsonString = mapper.writeValueAsString(job);
      System.out.println("Recieved the following JSON for the new job execution:\n" + jsonString);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    //
    // if a null passed -- then do nothing
    if (null == job) {
      return;
    }

    if (job.validate()) {
      //
      // save the job in the DB
      ExecutorDB.saveClusterJob(job);

      SGDService executorInstance = SGDService.getInstance();
      executorInstance.execute(job);
    }

  }

  // THIS IS THE GET REQUEST
  @Override
  public ClusterJob retrieve() {
    // Form form = getReference().getQueryAsForm();
    // String jobId = form.getFirstValue("jobid");
    String jobId = getReference().getLastSegment();
    System.out.println(" ** -- > " + jobId);
    ClusterJob job = ExecutorDB.getClusterJob(Long.valueOf(jobId));
    //
    // TODO: if the job is NULL? i.e. doesnt exist, what shall we return?
    //
    return job;
  }

  @Override
  public void update(ClusterJob job) {
    System.out.println("updating job with ID " + job.getJobId());
    ExecutorDB.updateClusterJob(job);
  }

  @Override
  public void remove(Long jobId) {
    assert true;
  }

}
