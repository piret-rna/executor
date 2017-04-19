package net.seninp.executor.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.hsqldb.lib.StringInputStream;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.routing.Router;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.seninp.executor.db.ExecutorDB;
import net.seninp.executor.service.SGDService;
import net.seninp.executor.util.StackTrace;

/**
 * The main Executor app.
 * 
 * @author psenin
 *
 */
public class ClusterJobServerApplication extends Application {

  final static Logger logger = Logger.getLogger(ClusterJobServerApplication.class);

  @Override
  public Restlet createInboundRoot() {

    Router router = new Router(getContext());

    // [1.0] list of jobs
    //
    Restlet clusterJob = new Restlet() {
      @Override
      public void handle(Request request, Response response) {

        // decode the request
        //
        Method method = request.getMethod();
        Form params = request.getResourceRef().getQueryAsForm();
        logger.info("recieved a " + method + " request with parameters " + params.toString());

        // [1.0] POST -- get the new job configuration
        //
        if (Method.POST.equals(method)) {
          processPostNewJobRequest(request, response);
        }
        // [2.0] GET & no params -- report the list of current jobs
        //
        else if (Method.GET.equals(method) && params.isEmpty()) {
          processGetCurrentJobsList(request, response);
        }
        // [3.0] GET & no params -- report the list of current jobs
        //
//        else if (Method.GET.equals(method) && params.isEmpty()) {
//          processGetCurrentJobsList(request, response);
//        }

        // Form params = request.getResourceRef().getQueryAsForm();
        // if params are empty,
        // System.out.println(method + " : " + params.toString());
        // String jobId = String.valueOf(request.getAttributes().get("jobid"));
        //
        // String jobStatus = SGDService.getJobStatus(jobId);
        //
        // //
        // String message = "Job status of job \"" + request.getAttributes().get("jobid") + "\": "
        // + jobStatus;
        // response.setEntity(message, MediaType.TEXT_PLAIN);
      }

      private void processGetCurrentJobsList(Request request, Response response) {

        // in the case of success "qstat -xml" prints the XML of all current jobs
        try {

          //
          // start the process
          Process p = new ProcessBuilder().command("qstat", "-xml").start();
          p.waitFor();

          //
          // intercept the stdout
          StringBuffer xmlStr = new StringBuffer();
          BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line = "";
          while ((line = stdOut.readLine()) != null) {
            xmlStr.append(line);
          }
          stdOut.close();

          //
          // process XML
          XPathFactory xpathFactory = XPathFactory.newInstance();
          XPath xpath = xpathFactory.newXPath();

          DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
          Document document = docBuilder.parse(new StringInputStream(xmlStr.toString()));

          ArrayList<String> responseArray = new ArrayList<String>();

          NodeList nodeList = document.getElementsByTagName("job_list");
          for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

              String jobState = node.getAttributes().getNamedItem("state").getNodeValue();
              String jobId = xpath.evaluate("JB_job_number", node);

              responseArray.add("jobid: " + jobId + "; state: " + jobState);

            }
          }

          // handle the response to the client
          JacksonRepresentation<ArrayList<String>> responseJobRepresentation = new JacksonRepresentation<ArrayList<String>>(
              responseArray);
          response.bufferEntity();
          response.setStatus(Status.SUCCESS_CREATED);
          response.setEntity(responseJobRepresentation);
          response.commit();

        }
        catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (SAXException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch (XPathExpressionException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      private void processPostNewJobRequest(Request request, Response response) {
        // try to decode the POST -- must be a JSONized new job
        try {

          // handle the JSON to ClusterJob conversion
          String jsonRepresentation = request.getEntityAsText();
          ObjectMapper mapper = new ObjectMapper();
          ClusterJob newJob = mapper.readValue(jsonRepresentation, ClusterJob.class);
          logger.info("new job to be executed: " + newJob);

          // create a DB record for the job
          long dbId = ExecutorDB.saveClusterJob(newJob);
          newJob.setId(dbId);

          // execute the job
          SGDService.getInstance().execute(newJob);

          // handle the response to the client
          JacksonRepresentation<ClusterJob> responseJobRepresentation = new JacksonRepresentation<ClusterJob>(
              newJob);
          response.bufferEntity();
          response.setStatus(Status.SUCCESS_CREATED);
          response.setEntity(responseJobRepresentation);
          response.commit();

        }
        catch (JsonParseException e) {
          System.err.println("Unable to parse JSON response: " + StackTrace.toString(e));
        }
        catch (JsonMappingException e) {
          System.err.println("Unable to map JSON from response: " + StackTrace.toString(e));

        }
        catch (IOException e) {
          System.err.println("Another IO exception occured: " + StackTrace.toString(e));
        }
      }

    };
    router.attach("/jobs", clusterJob);

    //
    // [1.0]
    //
    // GET and DELETE processed directly by the resource
    //
    router.attach("/jobs/{jobid}", ClusterJobServerResource.class);

    //
    // voila!
    //
    return router;
  }

}
