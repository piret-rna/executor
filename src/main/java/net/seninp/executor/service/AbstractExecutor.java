package net.seninp.executor.service;

import net.seninp.executor.resource.ClusterJob;

/**
 * A template for an executor.
 * 
 * @author psenin
 *
 * @param <V>
 */
public abstract class AbstractExecutor<V> {

  public abstract void execute(ClusterJob job);

}
