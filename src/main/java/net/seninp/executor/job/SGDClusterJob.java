package net.seninp.executor.job;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import net.seninp.executor.ExecutorServerProperties;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.JobCompletionStatus;
import net.seninp.executor.util.StackTrace;

/**
 * A facade for the ClusterJob tuned to SGD engine and executor.
 * 
 * @author psenin
 *
 */
public class SGDClusterJob implements Callable<ClusterJob> {

  final static Logger logger = Logger.getLogger(SGDClusterJob.class);

  private ClusterJob job;

  private static final String CR = "\n";

  // #!/bin/sh
  // #$ -S /bin/sh
  private static final String HEADER = "#!/bin/sh\n#$ -S /bin/sh";
  // #$ -N <JOB_NAME>
  private static final String NAME_PREFIX = "#$ -N ";
  // #$ -l <JOB_RESOURCE_REQUEST> "h_vmem=8G -pe smp 8"
  private static final String RESOURCE_MEMORY = "#$ -l h_vmem=";
  private static final String RESOURCE_CORES = "#$ -pe smp ";
  // #$ -m abe
  // #$ -M <JOB_NOTIFY>
  private static final String EMAIL_PREFIX = "#$ -M ";
  // #$ -j y
  // #$ -o <JOB_LOG>
  private static final String LOG_PREFIX = "#$ -o ";
  //
  private static final String SUFFIX = "#$ -m abe\n#$ -j y";

  //
  // umask 002
  private static final String UMASK = "umask 002";
  //
  // export _JAVA_OPTIONS="-Xmx512M -XX:MaxPermSize=512m"
  private static final String ENVIRONMENT_OPTIONS = "_JAVA_OPTIONS=\"-Xmx512M -XX:MaxPermSize=512m\"";
  //
  // <COMMAND>

  private String jobName;
  private String jobLog;
  private String userEmail;
  private int memoryGigabytes;
  private int cpuCores;

  private String command;

  public SGDClusterJob(ClusterJob job) {
    super();
    if (0 == job.getId()) {
      throw new NullPointerException("job id can't be null or 0");
    }
    this.job = job;
  }

  @Override
  public ClusterJob call() throws Exception {

    //
    // make the script
    this.command = job.getCommand();
    this.jobName = "job_" + String.valueOf(job.getId());
    this.jobLog = System.getProperty(ExecutorServerProperties.APPLICATION_FOLDER_KEY)
        + java.io.File.separator + "job_" + String.valueOf(job.getId()) + ".log";
    this.userEmail = "psenin@lanl.gov";
    this.cpuCores = job.getResourceCpu();
    this.memoryGigabytes = job.getResourceMem();

    String jobScript = getScript();
    logger.debug("the new job script: " + jobScript.replaceAll("\n", "\t"));

    //
    // save script into a file
    String scriptFname = "job_" + String.valueOf(job.getId()) + ".sh";
    BufferedWriter bw = new BufferedWriter(new FileWriter(scriptFname));
    bw.write(jobScript);
    bw.close();
    logger.debug("the new job script saved into " + scriptFname);

    //
    // and run it

    boolean errored = true;
    try {

      // in the case of success "qsub -terse" prints the job id into STDOUT and quits
      Process p = new ProcessBuilder().command("qsub", "-terse", scriptFname).start();
      p.waitFor();

      StringBuffer response = new StringBuffer("STDOUT response:\n");
      BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line, oldline = "";
      int lineCounter = 0;
      while ((line = stdOut.readLine()) != null) {
        response.append(line);
        System.out.println("stdout: " + line);
        lineCounter = lineCounter + 1;
        oldline = line;
      }
      stdOut.close();
      if (1 == lineCounter) {
        long sgeJobId = Long.valueOf(oldline.trim()).longValue();
        logger.debug("the new Job Id: " + sgeJobId);
      }

      response.append("\nSTDERR response:\n");
      BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((line = stdErr.readLine()) != null) {
        response.append(line);
      }
      stdErr.close();

      logger.debug("the execution results for job " + scriptFname + "\n" + response.toString());

      errored = false;
    }
    catch (NullPointerException e) {
      System.err.println("Apparently the script is not specified: " + StackTrace.toString(e));
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("Invalid command args: " + StackTrace.toString(e));
    }
    catch (SecurityException e) {
      System.err.println("Can not create a subprocess: " + StackTrace.toString(e));
    }
    catch (IOException e) {
      System.err.println("IO Exception happened: " + StackTrace.toString(e));
    }
    catch (InterruptedException e) {
      System.err
          .println("Interruption occured while waiting for qsub output: " + StackTrace.toString(e));
    }
    catch (NumberFormatException e) {
      System.err.println(
          "Interruption occured while recognizing the new job ID: " + StackTrace.toString(e));
    }
    catch (Exception e) {
      System.err.println("Unknown exception caught: " + StackTrace.toString(e));
    }
    finally {
      if (errored) {
        this.job.setStatus(JobCompletionStatus.ERRORED);
        this.job.setStatusTime(System.currentTimeMillis());
      }
      else {
        this.job.setStatus(JobCompletionStatus.ENQUEUED);
        this.job.setStatusTime(System.currentTimeMillis());
      }
    }

    return this.job;
  }

  protected String getScript() {
    StringBuffer sb = new StringBuffer();

    sb.append(HEADER).append(CR);
    //
    sb.append(NAME_PREFIX).append(jobName).append(CR);
    //
    sb.append(RESOURCE_MEMORY).append(this.memoryGigabytes).append("G").append(CR);
    sb.append(RESOURCE_CORES).append(this.cpuCores).append(CR);
    //
    sb.append(LOG_PREFIX).append(this.jobLog).append(CR);
    sb.append(EMAIL_PREFIX).append(this.userEmail).append(CR);
    //
    sb.append(SUFFIX).append(CR);
    sb.append(UMASK).append(CR);
    sb.append(ENVIRONMENT_OPTIONS).append(CR);
    sb.append(this.command).append(CR);

    return sb.toString();
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getJobLog() {
    return jobLog;
  }

  public void setJobLog(String jobLog) {
    this.jobLog = jobLog;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public int getMemoryGigabytes() {
    return memoryGigabytes;
  }

  public void setMemoryGigabytes(int memoryGigabytes) {
    this.memoryGigabytes = memoryGigabytes;
  }

  public int getCpuCores() {
    return cpuCores;
  }

  public void setCpuCores(int cpuCores) {
    this.cpuCores = cpuCores;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("SGDJob [jobName=").append(jobName).append(", jobLog=").append(jobLog)
        .append(", userEmail=").append(userEmail).append(", memoryGigabytes=")
        .append(memoryGigabytes).append(", cpuCores=").append(cpuCores).append(", command=")
        .append(command).append("]");
    return builder.toString();
  }

}
