package net.seninp.executor.job;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;
import net.seninp.executor.resource.ClusterJob;

public class SGDJobExecutor extends AbstractExecutor<ClusterJob> {

  Queue<ClusterJob> toBeExecuted = new LinkedList<ClusterJob>();

  @Override
  public Future<ClusterJob> execute(ClusterJob job) {

    this.toBeExecuted.add(job);
    
    return null;
  }

}
