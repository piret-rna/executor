package net.seninp.executor.resource;

import org.restlet.resource.ServerResource;
import net.seninp.executor.db.ExecutorDB;

/**
 * The server side implementation of the Restlet resource.
 */
public class ClusterJobServerResource extends ServerResource implements ClusterJobResource {

  public ClusterJobServerResource() {
    super();
  }

  @Override
  public ClusterJob retrieve() {
    // Form form = getReference().getQueryAsForm();
    // String jobId = form.getFirstValue("jobid");
    String jobId = getReference().getLastSegment();
    System.out.println("retrieving: " + jobId);
    ClusterJob job = ExecutorDB.getClusterJob(Long.valueOf(jobId));
    return job;
  }

  @Override
  public void store(ClusterJob job) {
    System.out.println("posting: " + job);
    ExecutorDB.saveClusterJob(job);
  }

  @Override
  public void remove(Long jobId) {
    assert true;
  }

}
