package net.seninp.executor.tinker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JobReportParserTinker {

  private static final String fName = "things/job_report_example0.xml";

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

    InputSource source = new InputSource(new FileInputStream(new File(fName)));

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(source);

    XPathFactory xpathFactory = XPathFactory.newInstance();
    XPath xpath = xpathFactory.newXPath();

    String jobId = xpath.evaluate("/detailed_job_info/djob_info/element/JB_job_number", document);
    String jobOwner = xpath.evaluate("/detailed_job_info/djob_info/element/JB_owner", document);

    System.out.println("jobId = " + jobId + "; jobOwner = " + jobOwner + "; jobStatus = ");

  }

}
