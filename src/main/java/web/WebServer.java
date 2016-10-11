package web;

import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.string.StrUtil;
import org.eclipse.jetty.server.Server;
import org.lemurproject.galago.utility.Parameters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jfoley
 */
public class WebServer {
  public static Logger logger = Logger.getLogger("WebServer");
  private final Server jetty;

  public WebServer(Server jetty) {
    this.jetty = jetty;
  }

  public WebServer start() {
    try {
      logger.log(Level.INFO, "WebServer started on "+getURL());
      jetty.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public void join() {
    try {
      jetty.join();
    } catch (InterruptedException ignored) {
      logger.log(Level.WARNING, ignored.getMessage(), ignored);
    }
  }

  public static WebServer create(int port, Handler handler) {
    Server jetty = new Server(port);
    jetty.setHandler(new JettyHandlerer(handler, jetty));
    return new WebServer(jetty);
  }

  public static WebServer create(Parameters argp, Handler handler) {
    Server jetty;
    if(argp.isString("host")) {
      jetty = new Server(new InetSocketAddress(argp.getString("host"), argp.getInt("port")));
    } else {
      jetty = new Server(argp.getInt("port"));
    }
    jetty.setHandler(new JettyHandlerer(handler, jetty));
    return new WebServer(jetty);
  }

  public String getURL() {
    return jetty.getURI().toASCIIString();
  }

  public static Map<String,List<String>> parseGetParameters(HttpServletRequest req) {
    HashMap<String,List<String>> request = new HashMap<>();

    Enumeration args = req.getParameterNames();
    while(args.hasMoreElements()) {
      String arg = (String) args.nextElement();
      List<String> vals = new ArrayList<>();
      Collections.addAll(vals, req.getParameterValues(arg));
      request.put(arg, vals);
    }

    return request;
  }

  @Nonnull
  public static String getContentType(@Nonnull HttpServletRequest req) {
    String contentType = req.getContentType();
    return StrUtil.takeBefore(contentType, ";");
  }

  public static void sendErrorMsg(HttpServletResponse response, String message, int code) {
    sendErrorMsg(response, message, code, null);
  }
  public static void sendErrorMsg(HttpServletResponse response, String message, int code, @Nullable Exception e) {
    if(e != null) {
      logger.log(Level.WARNING, code + ": " + message, e);
    } else {
      logger.log(Level.WARNING, code + ": " + message);
    }
    if(!response.isCommitted()) {
      response.setStatus(code);
      try (PrintWriter out = response.getWriter()) {
        response.setContentType("text/plain");
        out.println(message);
      } catch (IOException e1) {
        logger.log(Level.WARNING, "exception while writing error.", e1);
      }
    }
  }

  public static String readBody(HttpServletRequest request) {
    try (ServletInputStream sis = request.getInputStream()) {
      return IO.slurp(sis);
    } catch (IOException e) {
      throw new ServerErr(400, e);
    }
  }

}
