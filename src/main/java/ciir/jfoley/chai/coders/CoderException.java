package ciir.jfoley.chai.coders;

/**
 * @author jfoley
 */
public class CoderException extends RuntimeException {
  public CoderException(Throwable e, Class<? extends Coder> klazz) {
    super("CodecException in class="+klazz.getName(), e);
  }

  public CoderException(String msg) {
    super(msg);
  }
}
