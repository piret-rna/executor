package net.seninp.executor.client;

import com.beust.jcommander.Parameter;

public class JobStatusArgs {

  @Parameter(names = "-user", description = "The user name")
  public String user;

  @Parameter(names = "-start", description = "The start date, MM/DD/YYYY")
  public String start;

  @Parameter(names = "-end", description = "The end date, MM/DD/YYYY")
  public String end;

  @Parameter(names = "-jobid", description = "The job id")
  public Long jobId;

  @Parameter(names = "-debug", description = "Debug mode")
  public boolean debug = true;

}
