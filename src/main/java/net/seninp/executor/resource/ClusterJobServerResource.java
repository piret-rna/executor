package net.seninp.executor.resource;

import org.apache.log4j.Logger;
import org.restlet.resource.ServerResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.seninp.executor.ExecutorServer;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.service.SGDService;
import net.seninp.executor.util.ErrorMessage;

/**
 * The server side implementation of the Restlet resource.
 */
public class ClusterJobServerResource extends ServerResource implements ClusterJobResource {

  final static Logger logger = Logger.getLogger(ClusterJobServerResource.class);

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

  @Override
  public Object retrieve() {

    String jobId = getReference().getLastSegment();
    logger.info("recieved request for retrieving info for a job with id " + jobId);

    if (!(jobId.matches("[0-9]+"))) {
      logger.error("invalid job id provided: " + jobId);
      return new ErrorMessage("0001", "Invalid job ID", "The job id shall contain only digits");
    }

    // TODO: shall we update the job status right here???
    ClusterJob job = ExecutorDB.getClusterJob(Long.valueOf(jobId));

    return job;
  }

  @Override
  public void interrupt() {
    String jobId = getReference().getLastSegment();
    logger.info("recieved request for interrupting the job with id " + jobId);
    // TODO: the logic
  }

}
