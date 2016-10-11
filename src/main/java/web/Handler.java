package web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jfoley.
 */
public interface Handler {
  void handle(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
