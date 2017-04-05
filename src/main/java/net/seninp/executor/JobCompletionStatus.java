package net.seninp.executor;

public enum JobCompletionStatus {

  ENQUEUED(0), RUNNING(1), ERRORED(2), COMPLETED(3);

  private final int index;

  JobCompletionStatus(int index) {
    this.index = index;
  }

  public int index() {
    return index;
  }

  public static JobCompletionStatus fromValue(int value) {
    switch (value) {
    case 0:
      return JobCompletionStatus.ENQUEUED;
    case 1:
      return JobCompletionStatus.RUNNING;
    case 2:
      return JobCompletionStatus.ERRORED;
    case 3:
      return JobCompletionStatus.COMPLETED;
    default:
      throw new RuntimeException("Unknown index:" + value);
    }
  }

  public static JobCompletionStatus fromValue(String value) {
    if (value.equalsIgnoreCase("enqueued")) {
      return JobCompletionStatus.ENQUEUED;
    }
    else if (value.equalsIgnoreCase("running")) {
      return JobCompletionStatus.RUNNING;
    }
    else if (value.equalsIgnoreCase("errored")) {
      return JobCompletionStatus.ERRORED;
    }
    else if (value.equalsIgnoreCase("completed")) {
      return JobCompletionStatus.COMPLETED;
    }
    else {
      throw new RuntimeException("Unknown index:" + value);
    }
  }

  @Override
  public String toString() {
    switch (this.index) {
    case 0:
      return "ENQUEUED";
    case 1:
      return "RUNNING";
    case 2:
      return "ERRORED";
    case 3:
      return "COMPLETED";
    default:
      throw new RuntimeException("Unknown index");
    }
  }

}
