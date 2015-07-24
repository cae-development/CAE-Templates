package i5.las2peer.services.$Lower_Resource_Name$;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import i5.las2peer.p2p.LocalNode;
import i5.las2peer.restMapper.RESTMapper;
import i5.las2peer.restMapper.tools.ValidationResult;
import i5.las2peer.restMapper.tools.XMLCheck;
import i5.las2peer.security.ServiceAgent;
import i5.las2peer.security.UserAgent;
import i5.las2peer.services.$Lower_Resource_Name$.$Resource_Name$;
import i5.las2peer.testing.MockAgentFactory;
import i5.las2peer.webConnector.WebConnector;
import i5.las2peer.webConnector.client.ClientResponse;
import i5.las2peer.webConnector.client.MiniClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Test Class providing a basic JUnit test structure.
 *
 */
public class ServiceTest {

  private static final String HTTP_ADDRESS = "http://127.0.0.1";
  private static final int HTTP_PORT = WebConnector.DEFAULT_HTTP_PORT;

  private static LocalNode node;
  private static WebConnector connector;
  private static ByteArrayOutputStream logStream;

  private static UserAgent testAgent;
  private static final String testPass = "adamspass";

  private static final String testTemplateService = $Resource_Name$.class.getCanonicalName();

  private static final String mainPath = "/$Resource_Path$";

  
  /**
   * 
   * Called before the tests start.
   * 
   * Sets up the node and initializes connector and users that can be used throughout the tests.
   * 
   * @throws Exception
   * 
   */
  @BeforeClass
  public static void startServer() throws Exception {

    // start node
    node = LocalNode.newNode();
    node.storeAgent(MockAgentFactory.getAdam());
    node.launch();

    ServiceAgent testService = ServiceAgent.generateNewAgent(testTemplateService, "a pass");
    testService.unlockPrivateKey("a pass");

    node.registerReceiver(testService);

    // start connector
    logStream = new ByteArrayOutputStream();

    connector = new WebConnector(true, HTTP_PORT, false, 1000);
    connector.setLogStream(new PrintStream(logStream));
    connector.start(node);
    Thread.sleep(1000); // wait a second for the connector to become ready
    testAgent = MockAgentFactory.getAdam();

    connector.updateServiceList();
    // avoid timing errors: wait for the repository manager to get all services before continuing
    try {
      System.out.println("waiting..");
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  
  /**
   * 
   * Called after the tests have finished. Shuts down the server and prints out the connector log
   * file for reference.
   * 
   * @throws Exception
   * 
   */
  @AfterClass
  public static void shutDownServer() throws Exception {

    connector.stop();
    node.shutDown();

    connector = null;
    node = null;

    LocalNode.reset();

    System.out.println("Connector-Log:");
    System.out.println("--------------");

    System.out.println(logStream.toString());

  }


  /**
   * 
   * Test the $Microservice_Name$ for valid rest mapping. Important for development.
   * 
   */
  @Test
  public void testDebugMapping() {
    $Resource_Name$ cl = new $Resource_Name$();
    String XML_LOCATION = "./restMapping.xml";
    String xml = cl.getRESTMapping();

    try {
      RESTMapper.writeFile(XML_LOCATION, xml);
    } catch (IOException e) {
      e.printStackTrace();
    }

    XMLCheck validator = new XMLCheck();
    ValidationResult result = validator.validate(xml);

    if (!result.isValid())
      fail();
    }
  }

  $Service_Test_Methods$
  
}
