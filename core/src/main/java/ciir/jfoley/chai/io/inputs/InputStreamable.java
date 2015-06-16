package ciir.jfoley.chai.io.inputs;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public interface InputStreamable {
  String getName();

  InputStream getInputStream() throws IOException;
}
