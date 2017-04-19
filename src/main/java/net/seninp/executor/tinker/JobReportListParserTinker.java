package net.seninp.executor.tinker;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JobReportListParserTinker {

  private static final String fName = "things/job_report_example1.xml";

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();

    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document document = docBuilder.parse(new File(fName));

    NodeList nodeList = document.getElementsByTagName("job_list");
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {

        String jobState = node.getAttributes().getNamedItem("state").getNodeValue();

        String jobId = xpath.evaluate("JB_job_number", node);

        System.out.println("Job " + jobId + " is in state " + jobState);

      }
    }

  }

}
