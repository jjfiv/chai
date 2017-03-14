package ciir.jfoley.chai.web.json;

import ciir.jfoley.chai.collections.IntRange;
import ciir.jfoley.chai.collections.util.ListFns;
import org.junit.Test;
import org.lemurproject.galago.utility.Parameters;
import ciir.jfoley.chai.web.HTTPUtil;
import ciir.jfoley.chai.web.WebServer;

import java.util.ArrayList;
import java.util.Arrays;
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
    JSONAPI.appendRequestTime = false;
    testMethods.put("/null", p -> null);
    testMethods.put("/empty", p -> Parameters.create());
    testMethods.put("/debug", p -> p);
  }

  private static final AtomicBoolean started = new AtomicBoolean(false);
  private static WebServer server = null;

  @Test
  public void testSimpleJSONAPI() throws Exception {
    final int port = 12345;
    Runnable bg = () -> {
      server = JSONAPI.start(port, testMethods);
      server.join();
    };
    Thread bgThread = new Thread(bg);
    bgThread.start();

    for (int i = 0; server == null && i < 1000; i++) {
      System.out.println("Waiting for server start");
      Thread.sleep(500);
    }
    String serverURL = "http://localhost:"+port;

    HTTPUtil.Response response = HTTPUtil.get(serverURL, "/empty", Parameters.create());
    assertEquals(200, response.status);
    assertEquals(Parameters.create(), Parameters.parseString(response.body));
    assertEquals(Parameters.create(), HTTPUtil.get(serverURL, "/null", Parameters.create()).toJSON());

    Parameters tricky = Parameters.create();
    tricky.put("hello", 7L);
    tricky.put("what", "there");
    tricky.put("y", Arrays.asList(1L,2L));
    assertEquals(200, response.status);

    // POST or GET accepted:
    assertEquals(tricky, HTTPUtil.get(serverURL, "/debug", tricky).toJSON());
    assertEquals(tricky, HTTPUtil.postJSON(serverURL, "/debug", tricky).toJSON());


    ArrayList<Long> big = new ArrayList<>(ListFns.map(IntRange.exclusive(0,10000), Long::valueOf));
    Parameters bigP = Parameters.create();
    bigP.put("hello", 7L);
    bigP.put("what", "there");
    bigP.put("y", Arrays.asList(1L,2L));
    bigP.put("large", big);
    assertEquals(200, response.status);

    // POST or GET accepted:
    //GET not accepted: URI is too large
    //assertEquals(bigP, HTTPUtil.get(serverURL, "/debug", bigP).toJSON());
    Parameters found = HTTPUtil.postJSON(serverURL, "/debug", bigP).toJSON();
    assertEquals(bigP.keySet(), found.keySet());
    assertEquals(bigP.get("hello"), found.get("hello"));
    assertEquals(bigP.get("what"), found.get("what"));
    assertEquals(bigP.get("y"), found.get("y"));
    assertEquals(bigP.get("large"), found.get("large"));


    server.stop();
    bgThread.join();
  }

}