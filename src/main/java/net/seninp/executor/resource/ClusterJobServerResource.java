package net.seninp.executor.resource;

import org.restlet.resource.ServerResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.job.JobScriptFactory;

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
    // save the job in the DB
    ExecutorDB.saveClusterJob(job);

    //
    // execute the job
    JobScriptFactory newJob = new JobScriptFactory();
    newJob.setCommand(job.getCommand());
    newJob.setJobName("testQCJob");
    newJob.setJobLog("testQCJob.log");
    newJob.setUserEmail("psenin@lanl.gov");
    newJob.setCpuCores(job.getResourceCpu());
    newJob.setMemoryGigabytes(job.getResourceMem());
    System.out.println("the job script:\n" + newJob.getScript());

  }

  @Override
  public ClusterJob retrieve() {
    // Form form = getReference().getQueryAsForm();
    // String jobId = form.getFirstValue("jobid");
    String jobId = getReference().getLastSegment();
    ClusterJob job = ExecutorDB.getClusterJob(Long.valueOf(jobId));
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
