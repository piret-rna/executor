package net.seninp.executor.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.resource.JobCompletionStatus;

public class TestExecutorDB {

  private static SqlSessionFactory sqlSessionFactory;

  private static final String TEST_DB_URI = "jdbc:hsqldb:mem:testrnadb";

  private static final long TEST_JOBID = 7;
  private static final String TEST_UNAME = "psenin@lanl.gov";
  private static final int TEST_MEM = 77;
  private static final int TEST_CPU = 88;
  private static final String TEST_COMMAND = "test_cmd a b c";
  private static final String TEST_NOTES = "notes str";

  @BeforeClass
  public static void Before() throws IOException {

    // set URL with factory builder properties
    Properties properties = new Properties();
    properties.setProperty("url", TEST_DB_URI);

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    inputStream = Resources.getResourceAsStream(resource);
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);

  }

  @Test
  public void testDBURI() {
    ExecutorDB.connect(TEST_DB_URI);
    assertTrue(TEST_DB_URI.equalsIgnoreCase(ExecutorDB.getDbURI()));
  }

  @Test
  public void testNewJob() {

    ExecutorDB.connect(sqlSessionFactory);

    ClusterJob job = new ClusterJob(TEST_UNAME, TEST_COMMAND, TEST_CPU, TEST_MEM);
    job.setJobId(TEST_JOBID);
    long tstamp = new Date().getTime();
    job.setStartTime(tstamp);
    job.setEndTime(tstamp + 1);
    job.setStatusTime(tstamp + 2);
    job.setStatus(JobCompletionStatus.ENQUEUED);
    job.setNotes(TEST_NOTES);
    ExecutorDB.saveClusterJob(job);

    ClusterJob tJob = ExecutorDB.getClusterJobBySGDId(TEST_JOBID);
    assertTrue(TEST_UNAME.equalsIgnoreCase(tJob.getUsername()));
    assertTrue(TEST_COMMAND.equalsIgnoreCase(tJob.getCommand()));
    assertEquals(TEST_CPU, tJob.getResourceCpu());
    assertEquals(TEST_MEM, tJob.getResourceMem());
    assertEquals(tstamp, tJob.getStartTime().longValue());
    assertEquals(tstamp + 1, tJob.getEndTime().longValue());
    assertEquals(tstamp + 2, tJob.getStatusTime().longValue());
    assertEquals(JobCompletionStatus.ENQUEUED, job.getStatus());
    assertTrue(TEST_NOTES.equalsIgnoreCase(tJob.getNotes()));

  }

  @Test
  public void testUpdateJobByDBId() {

    ExecutorDB.connect(sqlSessionFactory);

    ClusterJob defaultJob = ExecutorDB.getClusterJobByDBId(0l);
    // session.insert("saveClusterJob", new ClusterJob("psenin", "test cmd", 4, 8));

    //
    // test the default job
    assertTrue("psenin".equalsIgnoreCase(defaultJob.getUsername()));
    assertTrue("test cmd".equalsIgnoreCase(defaultJob.getCommand()));
    assertEquals(4, defaultJob.getResourceCpu());
    assertEquals(8, defaultJob.getResourceMem());
    assertNull(defaultJob.getStartTime());
    assertNull(defaultJob.getEndTime());
    assertNull(defaultJob.getStatusTime());
    assertNull(defaultJob.getStatus());
    assertNull(defaultJob.getNotes());

    //
    // updating the default job
    defaultJob.setCommand(TEST_COMMAND);
    defaultJob.setUsername(TEST_UNAME);
    defaultJob.setResourceCpu(TEST_CPU);
    defaultJob.setResourceMem(TEST_MEM);
    defaultJob.setJobId(TEST_JOBID);
    defaultJob.setNotes(TEST_NOTES);

    long tstamp = new Date().getTime();
    defaultJob.setStartTime(tstamp);
    defaultJob.setEndTime(tstamp + 1);
    defaultJob.setStatusTime(tstamp + 2);
    defaultJob.setStatus(JobCompletionStatus.COMPLETED);

    ExecutorDB.updateClusterJobByDBId(defaultJob);

    ClusterJob tJob = ExecutorDB.getClusterJobByDBId(0l);

    assertTrue(TEST_UNAME.equalsIgnoreCase(tJob.getUsername()));
    assertTrue(TEST_COMMAND.equalsIgnoreCase(tJob.getCommand()));
    assertEquals(TEST_CPU, tJob.getResourceCpu());
    assertEquals(TEST_MEM, tJob.getResourceMem());
    assertEquals(TEST_JOBID, tJob.getJobId());
    assertEquals(tstamp, tJob.getStartTime().longValue());
    assertEquals(tstamp + 1, tJob.getEndTime().longValue());
    assertEquals(tstamp + 2, tJob.getStatusTime().longValue());
    assertEquals(JobCompletionStatus.COMPLETED, tJob.getStatus());
    assertTrue(TEST_NOTES.equalsIgnoreCase(tJob.getNotes()));

  }

  @Test
  public void testUpdateJobByJobId() {

    ExecutorDB.connect(sqlSessionFactory);

    ClusterJob defaultJob = ExecutorDB.getClusterJobBySGDId(0l);
    // session.insert("saveClusterJob", new ClusterJob("psenin", "test cmd", 4, 8));

    //
    // test the default job
    assertTrue("psenin".equalsIgnoreCase(defaultJob.getUsername()));
    assertTrue("test cmd".equalsIgnoreCase(defaultJob.getCommand()));
    assertEquals(4, defaultJob.getResourceCpu());
    assertEquals(8, defaultJob.getResourceMem());
    assertNull(defaultJob.getStartTime());
    assertNull(defaultJob.getEndTime());
    assertNull(defaultJob.getStatusTime());
    assertNull(defaultJob.getStatus());

    //
    // updating the default job
    defaultJob.setCommand(TEST_COMMAND);
    defaultJob.setUsername(TEST_UNAME);
    defaultJob.setResourceCpu(TEST_CPU);
    defaultJob.setResourceMem(TEST_MEM);
    defaultJob.setNotes(TEST_NOTES);

    long tstamp = new Date().getTime();
    defaultJob.setStartTime(tstamp);
    defaultJob.setEndTime(tstamp + 1);
    defaultJob.setStatusTime(tstamp + 2);
    defaultJob.setStatus(JobCompletionStatus.COMPLETED);

    ExecutorDB.updateClusterJobBySGDId(defaultJob);

    ClusterJob tJob = ExecutorDB.getClusterJobBySGDId(0l);

    assertTrue(TEST_UNAME.equalsIgnoreCase(tJob.getUsername()));
    assertTrue(TEST_COMMAND.equalsIgnoreCase(tJob.getCommand()));
    assertEquals(TEST_CPU, tJob.getResourceCpu());
    assertEquals(TEST_MEM, tJob.getResourceMem());
    assertEquals(0l, tJob.getJobId());
    assertEquals(tstamp, tJob.getStartTime().longValue());
    assertEquals(tstamp + 1, tJob.getEndTime().longValue());
    assertEquals(tstamp + 2, tJob.getStatusTime().longValue());
    assertEquals(JobCompletionStatus.COMPLETED, tJob.getStatus());
    assertTrue(TEST_NOTES.equalsIgnoreCase(tJob.getNotes()));

  }
}
