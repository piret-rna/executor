package net.seninp.executor.jobs;

import java.util.concurrent.Future;
import net.seninp.executor.ClusterJob;

public abstract class AbstractExecutor<V> {

  public abstract Future<V> execute(ClusterJob job);

}
