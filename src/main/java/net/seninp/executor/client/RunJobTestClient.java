package net.seninp.executor.client;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.ClusterJobResource;

public class RunJobTestClient {

  public static void main(String[] args) {

    if (args.length != 3) {
      System.err.println("You need to provide three arguments for this code to function.");
      System.exit(10);
    }

    String commandLine = args[0];
    Integer cpuNum = Integer.valueOf(args[1]);
    Integer memGB = Integer.valueOf(args[2]);

    System.out.println("Attempting to execute \"" + commandLine + "\" using " + cpuNum + "CPUs and "
        + memGB + "GB of memory");

    ClusterJob newJob = new ClusterJob();
    newJob.setCommand(commandLine);
    newJob.setResourceCpu(cpuNum);

    // Initialize the resource proxy.
    ClientResource cr = new ClientResource("http://localhost:8181/executor/newjob");

    // Workaround for GAE servers to prevent chunk encoding
    cr.setRequestEntityBuffering(true);
    cr.accept(MediaType.APPLICATION_JSON);

    ClusterJobResource resource = cr.wrap(ClusterJobResource.class);

    resource.run(newJob);

  }
}
