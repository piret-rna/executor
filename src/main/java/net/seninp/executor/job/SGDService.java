package net.seninp.executor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

  List<Future<JobCompletionStatus>> jobsQueue;

  ExecutorService executor;

  private static SGDService instance = new SGDService();

  private SGDService() {
    super();
    jobsQueue = new ArrayList<Future<JobCompletionStatus>>();
    executor = Executors.newFixedThreadPool(2);
  }

  public static SGDService getInstance() {
    return instance;
  }

  @Override
  public void execute(ClusterJob job) {

    SGDClusterJob newJob = new SGDClusterJob(job);

    Future<JobCompletionStatus> future = executor.submit(newJob);

    jobsQueue.add(future);

  }

  /**
   * Request the job status.
   * 
   * @param jobId
   * @return
   */
  public synchronized static String getJobStatus(String jobId) {
    String command[] = { "qstat", "-j", jobId };
    Process p;
    try {

      p = new ProcessBuilder().command(command).start();

      p.waitFor();

      // System.out.println(" ... > querying stdOut... ");
      BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
      StringBuffer response = new StringBuffer("");
      String line = "";
      while ((line = stdOut.readLine()) != null) {
        response.append(line);
      }
      stdOut.close();

      // System.out.println(" ... > querying stdErr... ");
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

}
