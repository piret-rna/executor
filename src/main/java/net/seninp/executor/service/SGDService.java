package net.seninp.executor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import net.seninp.executor.job.SGDClusterJob;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.JobCompletionStatus;
import net.seninp.executor.util.StackTrace;

/**
 * The gateway to system calls on SGD cluster.
 * 
 * @author psenin
 *
 */
public class SGDService extends AbstractExecutor<SGDClusterJob> {

  final static Logger logger = Logger.getLogger(SGDClusterJob.class);

  // The executor for QSUB
  //
  // ExecutorService executorService = Executors.newFixedThreadPool(2);
  private static ExecutorService executorService;
  private static ExecutorCompletionService<ClusterJob> jobCompletionService;

  // The executor for qstat
  //
  private static ScheduledExecutorService jobExecutionPollService;

  private static SGDService instance = new SGDService();

  private SGDService() {
    super();

    executorService = Executors.newSingleThreadExecutor();
    jobCompletionService = new ExecutorCompletionService<ClusterJob>(executorService);

    jobExecutionPollService = Executors.newSingleThreadScheduledExecutor();
    jobExecutionPollService.scheduleAtFixedRate(new JobExecutionCompletionPoller(), 1, 1,
        TimeUnit.MINUTES);

  }

  public static SGDService getInstance() {
    return instance;
  }

  @Override
  public void execute(ClusterJob job) {

    SGDClusterJob newJob = new SGDClusterJob(job);

    jobCompletionService.submit(newJob);

  }

  /**
   * Request the job status.
   * 
   * @param jobId
   * @return
   */
  public synchronized static String getJobStatus(String jobId) {

    // there are two ways to get the job data:
    // [1] if it's running, we request the QSTAT for that
    // [2] if it's finished, we can get QACCT for it

    //
    // try to get qstat
    String command[] = { "qstat", "-xml", "-j", jobId };
    try {

      Process p;

      p = new ProcessBuilder().command(command).start();

      p.waitFor();

      BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
      StringBuffer response = new StringBuffer("");
      String line = "";
      while ((line = stdOut.readLine()) != null) {
        response.append(line);
      }
      stdOut.close();

      if (0 == response.length()) {
        stdOut = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((line = stdOut.readLine()) != null) {
          response.append(line);
        }
        stdOut.close();
      }

      return response.toString();
    }
    catch (IOException e) {
      System.err.println("Unable to query job status with command \"" + Arrays.toString(command)
          + "\"... Exception: " + StackTrace.toString(e));
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      System.err.println("The executed command \"" + Arrays.toString(command)
          + "\" doesn't return... Exception: " + StackTrace.toString(e));
    }

    return null;
  }

  public class JobExecutionCompletionPoller implements Runnable {

    @Override
    public void run() {

      Future<ClusterJob> finishedJob;
      while (null != (finishedJob = jobCompletionService.poll())) {
        try {
          ClusterJob job = finishedJob.get();
          if (JobCompletionStatus.ENQUEUED.equals(job.getStatus())) {
            logger.info("The job with SGE ID " + job.getJobId() + " was enqueued at "
                + new Date(job.getStatusTime()));
          }
          else if (JobCompletionStatus.ERRORED.equals(job.getStatus())) {
            logger.info("The job with DB ID " + job.getId() + " errored at "
                + new Date(job.getStatusTime()));
          }
        }
        catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }

    }

  }

}
