package net.seninp.executor.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import net.seninp.executor.util.StackTrace;

public class SGDService {

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
