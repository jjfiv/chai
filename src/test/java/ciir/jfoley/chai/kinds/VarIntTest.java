package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.kinds.VarInt;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class VarIntTest {

  @Test
  public void testNegative() {
    VarInt coder = new VarInt();
    assertEquals(-8, coder.read(coder.write(-8)).intValue());
  }

  @Test
  public void testImpl() throws Exception {
    VarInt coder = new VarInt();
    Random rand = new Random();
    for (int i = 0; i < 1000; i++) {
      int x = rand.nextInt();
      assertEquals(x, coder.read(coder.write(x)).intValue());
    }

    // Check edge-case values:
    assertEquals(0, coder.read(coder.write(0)).intValue());
    assertEquals(1, coder.read(coder.write(1)).intValue());
    assertEquals(-1, coder.read(coder.write(-1)).intValue());
  }

}