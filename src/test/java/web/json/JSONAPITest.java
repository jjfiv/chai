package web.json;

import org.junit.Test;
import org.lemurproject.galago.utility.Parameters;
import web.HTTPUtil;
import web.WebServer;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class JSONAPITest {
  private static final Map<String, JSONMethod> testMethods = new HashMap<>();
  static {
    JSONAPI.supportsQuit = true;
    testMethods.put("/null", p -> null);
    testMethods.put("/empty", p -> Parameters.create());
  }

  private static final AtomicBoolean started = new AtomicBoolean(false);

  @Test
  public void testSimpleJSONAPI() throws InterruptedException, IOException {
    final int port = 12345;
    Runnable bg = () -> {
      WebServer start = JSONAPI.start(port, testMethods);
      started.set(true);
      start.join();
    };
    Thread bgThread = new Thread(bg);
    bgThread.start();

    for (int i = 0; !started.get() && i < 1000; i++) {
      System.out.println("Waiting for started=true");
      Thread.sleep(500);
    }
    String server = "http://localhost:"+port;

    HTTPUtil.Response response = HTTPUtil.get(server, "/empty", Parameters.create());
    assertEquals(200, response.status);
    assertEquals(Parameters.create(), Parameters.parseString(response.body));

    // send quit message:
    try {
      HTTPUtil.get(server, "/quit", Parameters.create());
    } catch (ConnectException err) {
      // maybe connection-refused here:
    }
    bgThread.join();
  }

}