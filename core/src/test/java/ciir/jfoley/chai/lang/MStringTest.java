package ciir.jfoley.chai.lang;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class MStringTest {

  @Test
  public void testMString() {
    String jstr = "hello";
    MString str = new MString(jstr);

    assertEquals(jstr.hashCode(), str.hashCode());
    str.append('!');
    assertEquals((jstr+"!").hashCode(), str.hashCode());
  }
}