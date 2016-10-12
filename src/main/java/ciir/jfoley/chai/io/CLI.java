package ciir.jfoley.chai.io;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

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
    return readString(System.out, System.in, prompt);
  }

  /**
   * Prints the prompt to stdout and reads response from stdin.
   * @param stdout Usually System.out
   * @param stdin Usually System.in
   * @param prompt question to ask, e.g. "Enter Command: "
   * @return the string typed by the user
   */
  @Nullable
  static String readString(PrintStream stdout, InputStream stdin, String prompt) {
    stdout.print(prompt);
    StringBuilder sb = new StringBuilder();
    while(true) {
      int ch;
      try {
        ch = stdin.read();
      } catch (IOException e) {
        return null;
      }
      if(ch == -1) return null;
      if(ch == '\n' || ch == '\r') break;
      sb.append((char) ch);
    }
    return sb.toString();
  }

  /**
   * Echo cmdline.
   * @param args ignored
   */
  public static void main(String[] args) {
    while(true) {
      String input = CLI.readString("> ");
      if (input == null) break;
      System.out.println(input);
    }
  }

}
