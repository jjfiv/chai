package web;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jfoley.
 */
public final class JettyHandlerer extends AbstractHandler {
  final Handler handler;

  public JettyHandlerer(Handler handler) {
    this.handler = handler;
  }

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    baseRequest.setHandled(true);

    // CORS-stuff
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    if ("OPTIONS".equals(request.getMethod())) {
      response.setStatus(200);
      response.setContentType("text/plain");
      return;
    }

    // now dispatch to handler:
    try {
      handler.handle(request, response);
    } catch (ServerErr e) {
      WebServer.sendErrorMsg(response, e.msg, e.code, e);
    } catch (Exception e) {
      WebServer.sendErrorMsg(response, e.getMessage(), 500, e);
    }
  }
}
