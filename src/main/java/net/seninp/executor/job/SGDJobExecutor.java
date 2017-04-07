package net.seninp.executor.job;

import java.util.concurrent.Future;
import net.seninp.executor.resource.ClusterJob;

public class SGDJobExecutor extends AbstractExecutor<ClusterJob> {

  @Override
  public Future<ClusterJob> execute(ClusterJob job) {

    JobScriptFactory qcJob = new JobScriptFactory();

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
