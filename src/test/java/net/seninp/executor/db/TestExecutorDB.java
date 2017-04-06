package net.seninp.executor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import net.seninp.executor.ClusterJob;
import net.seninp.executor.JobCompletionStatus;

public class TestExecutorDB {

  private static SqlSessionFactory sqlSessionFactory;

  private static final String TEST_UNAME = "psenin@lanl.gov";
  private static final int TEST_MEM = 77;
  private static final int TEST_CPU = 88;
  private static final String TEST_COMMAND = "test_cmd a b c";

  @BeforeClass
  public static void Before() throws IOException {

    // set URL with factory builder properties
    Properties properties = new Properties();
    properties.setProperty("url", "jdbc:hsqldb:mem:testrnadb");

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

  }

  @Test
  public void testUserRNADb() {

    ExecutorDB.connect(sqlSessionFactory);

    ClusterJob defaultJob = ExecutorDB.getClusterJob(0l);

    // session.insert("saveClusterJob", new ClusterJob("psenin", "test cmd", 4, 8));
    assertTrue("psenin".equalsIgnoreCase(defaultJob.getUsername()));
    assertTrue("test cmd".equalsIgnoreCase(defaultJob.getCommand()));
    assertEquals(4, defaultJob.getResourceCpu());
    assertEquals(8, defaultJob.getResourceMem());

    //
    ClusterJob job2 = new ClusterJob(TEST_UNAME, TEST_COMMAND, TEST_CPU, TEST_MEM);
    long tstamp = new Date().getTime();
    job2.setStartTime(tstamp);
    job2.setEndTime(tstamp + 1);
    job2.setStatusTime(tstamp + 2);
    job2.setStatus(JobCompletionStatus.ENQUEUED);

    ExecutorDB.saveClusterJob(job2);

  }

}
