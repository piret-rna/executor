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
  public void run(ClusterJob job) {
    // TODO Auto-generated method stub

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
