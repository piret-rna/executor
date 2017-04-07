package net.seninp.executor.job;

import java.util.concurrent.Future;
import net.seninp.executor.resource.ClusterJob;

public abstract class AbstractExecutor<V> {

  public abstract Future<V> execute(ClusterJob job);

}
