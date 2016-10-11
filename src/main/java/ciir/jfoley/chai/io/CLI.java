package ciir.jfoley.chai.io;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author jfoley.
 */
public class CLI {

  /**
   * Prints the prompt to stdout and reads response from stdin.
   * @param prompt question to ask, e.g. "Enter Command: "
   * @return the string typed by the user
   */
  @Nullable
  public static String readString(String prompt) {
    System.out.print(prompt);
    StringBuilder sb = new StringBuilder();
    while(true) {
      int ch = 0;
      try {
        ch = System.in.read();
      } catch (IOException e) {
        return null;
      }
      if(ch == -1) return null;
      if(ch == '\n' || ch == '\r') break;
      sb.append((char) ch);
    }
    return sb.toString();
  }

}
