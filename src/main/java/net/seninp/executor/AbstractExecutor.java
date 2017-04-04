package net.seninp.executor;

import java.util.concurrent.Future;

public abstract class AbstractExecutor<V> {

  public abstract Future<V> execute(ClusterJob job);

}
