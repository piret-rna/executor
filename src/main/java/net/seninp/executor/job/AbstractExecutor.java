package net.seninp.executor.job;

import net.seninp.executor.resource.ClusterJob;

public abstract class AbstractExecutor<V> {

  public abstract void execute(ClusterJob job);

}
