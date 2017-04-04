package net.seninp.executor;

/**
 * Cluster job container.
 * 
 * @author psenin
 *
 */
public class ClusterJob {


  private long jobId;
  
  private long startTime; 
  
  private long endTime; 
  
  private String command;

  public ClusterJob(long jobId, long startTime, long endTime, String command) {
    super();
    this.jobId = jobId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.command = command;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ClusterJob [jobId=").append(jobId).append(", startTime=").append(startTime)
        .append(", endTime=").append(endTime).append(", command=").append(command).append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((command == null) ? 0 : command.hashCode());
    result = prime * result + (int) (endTime ^ (endTime >>> 32));
    result = prime * result + (int) (jobId ^ (jobId >>> 32));
    result = prime * result + (int) (startTime ^ (startTime >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClusterJob other = (ClusterJob) obj;
    if (command == null) {
      if (other.command != null)
        return false;
    }
    else if (!command.equals(other.command))
      return false;
    if (endTime != other.endTime)
      return false;
    if (jobId != other.jobId)
      return false;
    if (startTime != other.startTime)
      return false;
    return true;
  }

  public long getJobId() {
    return jobId;
  }

  public void setJobId(long jobId) {
    this.jobId = jobId;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }
  
}
