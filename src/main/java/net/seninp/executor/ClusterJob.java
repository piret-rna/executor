package net.seninp.executor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Cluster job container.
 * 
 * @author psenin
 *
 */
public class ClusterJob implements Serializable {

  private static final long serialVersionUID = -5507989606477828422L;

  private long id;

  private String username;

  private long jobId;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String command;

  private JobCompletionStatus status;

  private LocalDateTime statusTime;

  public ClusterJob() {
    super();
  }

  public ClusterJob(String username, long jobId, LocalDateTime startTime, LocalDateTime endTime,
      String command, JobCompletionStatus status, LocalDateTime statusTime) {
    super();
    this.username = username;
    this.jobId = jobId;
    this.startTime = startTime;
    this.endTime = endTime;
    this.command = command;
    this.status = status;
    this.statusTime = statusTime;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ClusterJob [id=").append(id).append(", username=").append(username)
        .append(", jobId=").append(jobId).append(", startTime=").append(startTime)
        .append(", endTime=").append(endTime).append(", command=\'").append(command)
        .append("\', status=").append(status).append(", statusTime=").append(statusTime).append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((command == null) ? 0 : command.hashCode());
    result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
    result = prime * result + (int) (jobId ^ (jobId >>> 32));
    result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + ((statusTime == null) ? 0 : statusTime.hashCode());
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
    if (endTime == null) {
      if (other.endTime != null)
        return false;
    }
    else if (!endTime.equals(other.endTime))
      return false;
    if (jobId != other.jobId)
      return false;
    if (startTime == null) {
      if (other.startTime != null)
        return false;
    }
    else if (!startTime.equals(other.startTime))
      return false;
    if (status != other.status)
      return false;
    if (statusTime == null) {
      if (other.statusTime != null)
        return false;
    }
    else if (!statusTime.equals(other.statusTime))
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

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
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

  public LocalDateTime getStatusTime() {
    return statusTime;
  }

  public void setStatusTime(LocalDateTime statusTime) {
    this.statusTime = statusTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

}
