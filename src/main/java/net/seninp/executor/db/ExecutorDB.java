package net.seninp.executor.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import net.seninp.executor.ExecutorServerProperties;
import net.seninp.executor.resource.ClusterJob;
import net.seninp.executor.util.StackTrace;

/**
 * Static wrapper for the myBatis-based mapper.
 * 
 * @author psenin
 *
 */
public class ExecutorDB {

  final static Logger logger = Logger.getLogger(ExecutorDB.class);

  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Disable constructor.
   */
  private ExecutorDB() {
    super();
    assert true;
  }

  /**
   * A wrapper to the default constructor.
   */
  public static void connect() {
    logger.info("no URI provided, starting the DB with default settings");
    String nullURI = null;
    connect(nullURI);
  }

  /**
   * Attempts to establish the database connection, will re-create the tables and populate the test
   * user if not exists.
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

      // dbHome.append(System.getProperty("user.home"));
      // dbHome.append(java.io.File.separator);
      // dbHome.append(".executor/database");
      dbHome.append(System.getProperty(ExecutorServerProperties.DB_DIR_KEY));

      dbHome.append(";shutdown=true");

      dbURI = dbHome.toString();
      logger.info(
          "no URI provided, starting the DB with default settings: \'" + dbHome.toString() + "\'");
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
      logger.error("exception why creating the SqlSessionFactory: " + StackTrace.toString(e));
    }

    logger.info("the SqlSessionFactory instantiated, recreating the tables");
    recreateTables();

  }

  /**
   * In the case of building an SqlSession manually use this constructor, will re-create the tables
   * and populate the test user if not exists.
   * 
   * @param sqlSessionFactory the SQL Session factory to use for db connection.
   * 
   */
  public static void connect(SqlSessionFactory sqlSessionFactory) {
    logger.info("connecting using a custom SqlSessionFactory "
        + sqlSessionFactory.getConfiguration().getVariables().get("url"));
    ExecutorDB.sqlSessionFactory = sqlSessionFactory;
    logger.info("recreating the tables");
    recreateTables();
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

  /**
   * Get the record by the job ID.
   * 
   * @param jobId
   * @return
   */
  public static ClusterJob getClusterJob(Long jobId) {
    logger.info("getting a job with an id " + String.valueOf(jobId));
    if (null == jobId || jobId < 0) {
      logger.error("a suspicious ID given: " + String.valueOf(jobId) + ", skipping...");
      return null;
    }
    SqlSession session = sqlSessionFactory.openSession();
    ClusterJob job = session.selectOne("getClusterJobById", jobId);
    session.close();
    return job;
  }

  /**
   * Saves the cluster job record.
   * 
   * @param job
   * @return
   */
  public static long saveClusterJob(ClusterJob job) {
    logger.info(
        "saving a cluster job instance with Id " + job.getJobId() + "{" + job.toString() + "}");
    SqlSession session = sqlSessionFactory.openSession();
    session.insert("saveClusterJob", job);
    session.commit();
    session.close();
    return job.getId();
  }

  /**
   * Updates the cluster job record.
   * 
   * @param job
   * @return
   */
  public static long updateClusterJob(ClusterJob job) {
    logger.info(
        "updating the cluster job instance with Id " + job.getJobId() + "{" + job.toString() + "}");
    SqlSession session = sqlSessionFactory.openSession();
    session.update("updateClusterJob", job);
    session.commit();
    session.close();
    return job.getId();
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
        session.insert("saveClusterJob", new ClusterJob("psenin", "test cmd", 4, 8));
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

}
