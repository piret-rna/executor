package net.seninp.executor.resource;

import java.io.Serializable;

/**
 * Cluster job data structure.
 * 
 * @author psenin
 *
 */
public class ClusterJob implements Serializable {

  private static final long serialVersionUID = -5507989606477828422L;

  private long id;

  private String username;

  private long jobId;

  private String command;

  private int resourceCpu;

  private int resourceMem;

  private Long startTime;

  private Long endTime;

  private JobCompletionStatus status;

  private Long statusTime;

  public ClusterJob() {
    super();
  }

  public ClusterJob(String username, String command, int resourceCpu, int resourceMem) {
    super();
    this.username = username;
    this.command = command;
    this.resourceCpu = resourceCpu;
    this.resourceMem = resourceMem;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ClusterJob [id=").append(id).append(", username=").append(username)
        .append(", jobId=").append(jobId).append(", command=").append(command)
        .append(", resourceCpu=").append(resourceCpu).append(", resourceMem=").append(resourceMem)
        .append(", startTime=").append(startTime).append(", endTime=").append(endTime)
        .append(", status=").append(status).append(", statusTime=").append(statusTime).append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((command == null) ? 0 : command.hashCode());
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + (int) (jobId ^ (jobId >>> 32));
    result = prime * result + ((username == null) ? 0 : username.hashCode());
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
    if (id != other.id)
      return false;
    if (jobId != other.jobId)
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    }
    else if (!username.equals(other.username))
      return false;
    return true;
  }

  public long getJobId() {
    return jobId;
  }

  public void setJobId(long jobId) {
    this.jobId = jobId;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getEndTime() {
    return endTime;
  }

  public void setEndTime(Long endTime) {
    this.endTime = endTime;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public JobCompletionStatus getStatus() {
    return status;
  }

  public void setStatus(JobCompletionStatus status) {
    this.status = status;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Long getStatusTime() {
    return statusTime;
  }

  public void setStatusTime(Long statusTime) {
    this.statusTime = statusTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getResourceCpu() {
    return resourceCpu;
  }

  public void setResourceCpu(int resourceCpu) {
    this.resourceCpu = resourceCpu;
  }

  public int getResourceMem() {
    return resourceMem;
  }

  public void setResourceMem(int resourceMem) {
    this.resourceMem = resourceMem;
  }

  public boolean validate() {
    // check that username is specified
    if (null == this.username || this.username.isEmpty()) {
      return false;
    }
    // check that command is specified
    if (null == this.command || this.command.isEmpty()) {
      return false;
    }
    return true;
  }

}
