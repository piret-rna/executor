package net.seninp.executor.jobs;

/**
 * Implements an SGD shell script for job execution
 * 
 * @author psenin
 *
 */
public class SGDJob {

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

  public String getScript() {
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

  public SGDJob() {
    super();
  }

  public SGDJob(String command, String jobName, String jobLog, String userEmail,
      int memoryGigabytes, int cpuCores) {
    super();
    this.command = command;
    this.jobName = jobName;
    this.jobLog = jobLog;
    this.userEmail = userEmail;
    this.memoryGigabytes = memoryGigabytes;
    this.cpuCores = cpuCores;
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
