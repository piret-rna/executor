package net.seninp.executor.job;

import java.util.concurrent.Callable;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.JobCompletionStatus;

/**
 * A facade for the ClusterJob tuned to SGD engine and executor.
 * 
 * @author psenin
 *
 */
public class SGDClusterJob implements Callable<JobCompletionStatus> {

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
  private static final String SUFFIX = "#$ -m abe\n#$ -j y\numask 002";

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
    this.job = job;
  }

  @Override
  public JobCompletionStatus call() throws Exception {

    //
    // execute the job
    this.command = job.getCommand();
    this.jobName = "testJob";
    this.jobLog = "testJob.log";
    this.userEmail = "psenin@lanl.gov";
    this.cpuCores = job.getResourceCpu();
    this.memoryGigabytes = job.getResourceMem();
    System.out.println("the job script:\n" + getScript());

    return executeSystemCall(getScript());

  }

  public ClusterJob execute() {
    // save the thing to the DB -- the way to keep the job ID is to use DB primary ID key
    return null;
  }

  private JobCompletionStatus executeSystemCall(String script) {
    // todo: do stuff.
    return JobCompletionStatus.ENQUEUED;
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
