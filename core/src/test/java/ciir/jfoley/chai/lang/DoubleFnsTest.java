package ciir.jfoley.chai.lang;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author jfoley
 */
public class DoubleFnsTest {

  @Test
  public void testEquals() throws Exception {
    assertTrue(DoubleFns.equals(0.2, 0.2, 0.01));
    assertFalse(DoubleFns.equals(0.4, 0.2, 0.1));

    try {
      assertFalse(DoubleFns.equals(0.2, 0.2, -0.01));
      fail("Expected assertion for negative epsilon.");
    } catch (AssertionError ignored) {
      // success!
    }
  }

}