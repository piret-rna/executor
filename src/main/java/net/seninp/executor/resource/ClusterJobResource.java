package net.seninp.executor.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface ClusterJobResource {

  @Post
  public void run(ClusterJob job);

  @Get("json")
  public ClusterJob retrieve();

  @Put
  public void update(ClusterJob job);

  @Delete
  public void remove(Long jobId);

}
