package ciir.jfoley.chai.web;

/**
 * @author jfoley
 */
public class JettyError extends RuntimeException {
  public JettyError(Exception e) {
    super(e);
  }
}
