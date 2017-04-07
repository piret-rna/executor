package net.seninp.executor;

import java.util.Date;
import java.util.concurrent.Future;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import net.seninp.executor.job.SGDJobExecutor;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.ClusterJobResource;

public class ExecutorServerTestClient {

  public static void main(String[] args) {

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/executor/json");

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.APPLICATION_JSON);

    ClusterJobResource resource = cr.wrap(ClusterJobResource.class);

    // Get the remote contact
    ClusterJob job = resource.retrieve();
    if (job != null) {
      System.out.println("    jobId: " + job.getJobId());
      System.out.println("startTime: " + new Date(job.getStartTime()));
      System.out.println("      cmd: " + job.getCommand());
      System.out.println("   status: " + job.getStatus());
    }

    // Update the contact
    //job.setJobId(77l);
    //resource.store(job);

    //
    //

    ClusterJob qcJob = new ClusterJob();
    qcJob.setCommand(
        "perl /data/edge_v1.0/scripts/illumina_fastq_QC.pl  -p /data/edge_v1.0/edge_ui/EDGE_output/1028/SRA_Download/SRR908279.1.fastq.gz");

    SGDJobExecutor executor = new SGDJobExecutor();
    Future<ClusterJob> res = executor.execute(qcJob);

  }

}