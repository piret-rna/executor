package net.seninp.executor.job;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.JobCompletionStatus;

public class SGDJobExecutor extends AbstractExecutor<SGDClusterJob> {

  List<Future<JobCompletionStatus>> jobsQueue;

  ExecutorService executor;

  private static SGDJobExecutor instance = new SGDJobExecutor();

  private SGDJobExecutor() {
    super();
    jobsQueue = new ArrayList<Future<JobCompletionStatus>>();
    executor = Executors.newFixedThreadPool(2);
  }

  public static SGDJobExecutor getInstance() {
    return instance;
  }

  @Override
  public void execute(ClusterJob job) {

    SGDClusterJob newJob = new SGDClusterJob(job);

    Future<JobCompletionStatus> future = executor.submit(newJob);

    jobsQueue.add(future);

  }
  
}
