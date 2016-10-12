package ciir.jfoley.chai.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class CLITest {
  @Test
  public void testCLI() {
    ByteArrayInputStream bais = new ByteArrayInputStream("hello\n".getBytes());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    String input = CLI.readString(new PrintStream(baos), bais, "prompt>");

    assertEquals("prompt>", new String(baos.toByteArray()));
    assertEquals("hello", input);
  }

}