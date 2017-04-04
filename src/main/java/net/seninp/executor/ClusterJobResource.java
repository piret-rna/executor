package net.seninp.executor;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface ClusterJobResource {

  @Get("json")
  public ClusterJob retrieve();

  @Put
  public void store(ClusterJob job);

  @Delete
  public void remove(Long jobId);

}
