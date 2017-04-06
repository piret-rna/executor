package net.seninp.executor.jobs;

import java.util.concurrent.Future;
import net.seninp.executor.ClusterJob;

public class QCExecutor extends AbstractExecutor<ClusterJob> {

  @Override
  public Future<ClusterJob> execute(ClusterJob job) {

    SGDJob qcJob = new SGDJob();

    qcJob.setCommand(job.getCommand());

    qcJob.setJobName("testQCJob");
    qcJob.setJobLog("testQCJob.log");

    qcJob.setUserEmail("psenin@lanl.gov");

    qcJob.setCpuCores(4);
    qcJob.setMemoryGigabytes(8);

    System.out.println(qcJob);

    return null;
  }

}
