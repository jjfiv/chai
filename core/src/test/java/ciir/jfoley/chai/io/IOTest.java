package ciir.jfoley.chai.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley.
 */
public class IOTest {

  @Test
  public void testReadAll() throws Exception {
    String testData = "hello world!";
    assertEquals(testData, IO.readAll(IO.stringReader(testData)));
    // small buffer size:
    assertEquals(testData, IO.readAll(IO.stringReader(testData), 2));
  }
}