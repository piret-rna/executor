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
    ClusterJob job = ExecutorDB.getClusterJob(0l);
    System.out.println("retrieving: " + job);
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
