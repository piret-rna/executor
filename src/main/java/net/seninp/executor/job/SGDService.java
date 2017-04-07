package net.seninp.executor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.seninp.executor.util.StackTrace;

public class SGDService {

  public synchronized static String getJobStatus(String jobId) {
    String jobStatusQuery = "qstat -j " + jobId;
    Runtime r = Runtime.getRuntime();
    Process p;
    try {
      System.out.println(" --> " + jobStatusQuery);
      p = r.exec(jobStatusQuery);
      p.waitFor();
      BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
      StringBuffer response = new StringBuffer("");
      String line = "";
      while ((line = b.readLine()) != null) {
        System.out.println(" --> " + line);
        response.append(line);
      }
      b.close();
      return response.toString();
    }
    catch (IOException e) {
      System.err.println("Unable to query job status with command \"" + jobStatusQuery
          + "\"... Exception: " + StackTrace.toString(e));
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      System.err.println("The executed command \"" + jobStatusQuery
          + "\" doesn't return... Exception: " + StackTrace.toString(e));
    }

    return null;
  }

}
