package web.json;

import ciir.jfoley.chai.collections.util.ListFns;
import web.Handler;
import web.ServerErr;
import web.WebServer;
import org.lemurproject.galago.utility.Parameters;
import org.lemurproject.galago.utility.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Needs the utility package from org.lemurproject.galago in order to parse JSON.
 * Super-simple JSON server.
 * @author jfoley
 */
public class JSONAPI {
  public static boolean appendRequestTime = true;

  public static WebServer start(int port, Map<String, JSONMethod> methods) {
    // check for silly mistakes.
    for (String s : methods.keySet()) {
      if(!s.startsWith("/")) {
        throw new IllegalArgumentException("API method that will never match on a path: <<"+s+">> does not start with /");
      }
    }
    return WebServer.create(port, new JSONHandler(methods)).start();
  }
  public static WebServer start(Parameters argp, Map<String, JSONMethod> methods) {
    // check for silly mistakes.
    for (String s : methods.keySet()) {
      if(!s.startsWith("/")) {
        throw new IllegalArgumentException("API method that will never match on a path: <<"+s+">> does not start with /");
      }
    }
    return WebServer.create(argp, new JSONHandler(methods)).start();
  }

  public static class JSONHandler implements Handler {
    public final Map<String, JSONMethod> methods;

    public JSONHandler(Map<String, JSONMethod> methods) {
      this.methods = methods;
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long startTime = System.currentTimeMillis();

      String method = req.getMethod();
      switch (method) {
        case "POST":
        case "GET":
          break;
        default:
          WebServer.sendErrorMsg(resp, "Bad method="+method, 400);
          return;
      }

      String path = req.getPathInfo();
      JSONMethod m = methods.get(path);
      if(m == null) {
        WebServer.sendErrorMsg(resp, "No such API method="+method, 404);
        return;
      }

      Parameters jreq = Parameters.create();
      if("POST".equals(method)) {
        String contentType = WebServer.getContentType(req);
        switch (contentType) {
          case "application/json":
          case "text/json":
            String body = WebServer.readBody(req);
            try {
              jreq = Parameters.parseString(body);
            } catch (Throwable jsonParseError) {
              System.err.println(body); // for debugging
              throw new ServerErr(400, "Bad Request - JSON Parse Error!");
            }
            break;
          default:
            throw new ServerErr(400, "Bad Request!");
        }
      }

      // allow any GET parameters to override body parameters
      jreq.copyFrom(parseGetParameters(req));

      Parameters jresp = m.process(jreq);
      if(jresp == null) {
        jresp = Parameters.create();
      }

      long endTime = System.currentTimeMillis();
      if(appendRequestTime) {
        jresp.put("requestTime", (endTime - startTime));
      }

      resp.setContentType("application/json");

      try (PrintWriter out = resp.getWriter()) {
        out.println(jresp.toString());
        resp.setStatus(200);
      }
    }
  }

  public static Parameters parseGetParameters(HttpServletRequest req) {
    Parameters out = Parameters.create();
    Map<String, List<String>> getP = WebServer.parseGetParameters(req);
    for (Map.Entry<String, List<String>> kv : getP.entrySet()) {
      List<String> vals = kv.getValue();
      if(vals.size() == 0) continue;
      if(vals.size() == 1) {
        out.put(kv.getKey(), JSONUtil.parseString(vals.get(0)));
      } else {
        out.put(kv.getKey(), ListFns.map(vals, JSONUtil::parseString));
      }
    }
    return out;
  }

}
