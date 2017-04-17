package net.seninp.executor.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;

/**
 * Working with jobs via REST interface.
 * 
 * @author psenin
 *
 */
public interface ClusterJobResource {

//  /**
//   * Executing a new job.
//   * 
//   * @param job
//   */
//  @Post
//  public void run(ClusterJob job);

  /**
   * Getting a job info.
   * 
   * @return
   */
  @Get("json")
  public Object retrieve();

  /**
   * Interrupting a running job.
   */
  @Delete
  public void interrupt();

  // @Put
  // public void update(ClusterJob job);
  //

}
