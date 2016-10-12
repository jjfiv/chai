package ciir.jfoley.chai.web;

/**
 * @author jfoley
 */
public class ServerErr extends RuntimeException {
  public final int code;
  public final String msg;

  public ServerErr(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ServerErr(String msg) {
    this(500, msg);
  }

  public ServerErr(int code, Exception e) {
    this.code = code;
    this.msg = e.getMessage();
  }
  public ServerErr(String msg, Exception e) {
    super(msg, e);
    this.code = 500;
    this.msg = msg;
  }

  public ServerErr(Exception e) {
    this(500, e);
  }
}
