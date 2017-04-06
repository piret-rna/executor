package net.seninp.executor.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import net.seninp.executor.ClusterJob;
import net.seninp.executor.JobCompletionStatus;
import net.seninp.executor.util.StackTrace;

/**
 * Static wrapper for the myBatis-based mapper.
 * 
 * @author psenin
 *
 */
public class ExecutorDB {

  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Disable constructor.
   */
  private ExecutorDB() {
    super();
    assert true;
  }

  /**
   * Attempts to establish the database connection. Also creates a database if not exists and
   * populates the test user.
   * 
   * @param dbURI the non-default DB URI, specify an empty string for default.
   */
  public static void connect(String dbURI) {

    // set URL with factory builder properties
    Properties properties = new Properties();

    if (null == dbURI || dbURI.isEmpty()) {
      // compose the HSQL DB url
      // <HSQLDB prefix> + <USER HOME> + ".executor/database"
      //
      StringBuilder dbHome = new StringBuilder();
      dbHome.append("jdbc:hsqldb:file:");
      dbHome.append(System.getProperty("user.home"));
      dbHome.append(java.io.File.separator);
      dbHome.append(".executor/database");
      dbHome.append(";shutdown=true");

      dbURI = dbHome.toString();
    }

    properties.setProperty("url", dbURI);

    // locate the mapper config file and bootstrap the DB
    String resource = "SqlMapConfig.xml";
    InputStream inputStream;
    try {
      // load the config
      inputStream = Resources.getResourceAsStream(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
    }
    catch (IOException e) {
      System.err.println(StackTrace.toString(e));
    }

    recreateTables();

  }

  /**
   * In the case of building an SqlSession manually use this constructor.
   * 
   * @param sqlSessionFactory the SQL Session factory to use for db connection.
   * 
   */
  public static void connect(SqlSessionFactory sqlSessionFactory) {
    ExecutorDB.sqlSessionFactory = sqlSessionFactory;
    recreateTables();
  }

  /**
   * Re-creates the Schema tables.
   */
  private static void recreateTables() {

    // open session
    SqlSession session = sqlSessionFactory.openSession();

    // bootstrapping
    try {

      // ***DROP*** and re-create the USER table if not exists
      session.insert("dropClusterJobTable"); // drops the user table...
      session.insert("createClusterJobTable");

      // add the test user if not in there
      ClusterJob testJob = session.selectOne("getClusterJobById", 0l);
      if (null == testJob) {
        session.insert("saveClusterJob",
            new ClusterJob("psenin", 0, Long.valueOf(new Date().getTime()),
                Long.valueOf(new Date().getTime()), "test cmd", JobCompletionStatus.ENQUEUED,
                Long.valueOf(new Date().getTime())));
        session.commit();
      }

    }
    catch (Exception e) {
      System.err.println(StackTrace.toString(e));
    }
    finally {
      session.close();
    }

  }

  /**
   * Reports the actual database URI.
   * 
   * @return the database URI.
   */
  public static String getDbURI() {
    SqlSession session = sqlSessionFactory.openSession();
    String dbURL = (String) session.getConfiguration().getVariables().get("url");
    session.close();
    return dbURL;
  }

  public static ClusterJob getClusterJob(Long jobId) {
    SqlSession session = sqlSessionFactory.openSession();
    ClusterJob job = session.selectOne("getClusterJobById", jobId);
    System.out.println(job);
    session.close();
    return job;
  }

  public static long saveClusterJob(ClusterJob job) {
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("saveClusterJob", job);
    session.commit();
    session.close();
    return job.getId();
  }

}
