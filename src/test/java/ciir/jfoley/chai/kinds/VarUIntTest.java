package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.data.ByteBuilder;
import ciir.jfoley.chai.coders.kinds.VarUInt;
import ciir.jfoley.chai.collections.list.IntList;
import org.junit.Test;

import java.io.InputStream;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author jfoley
 */
public class VarUIntTest {

  @Test
  public void testNegative() {
    VarUInt coder = new VarUInt();
    try {
      assertEquals(-8, coder.read(coder.write(-8)).intValue());
      fail("Shouldn't even try to encode a negative number.");
    } catch (AssertionError e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testImpl() throws Exception {
    VarUInt coder = new VarUInt();
    Random rand = new Random();
    for (int i = 0; i < 100000; i++) {
      int x = Math.abs(rand.nextInt());
      assertEquals(x, coder.read(coder.write(x)).intValue());
    }

    // Check edge-case values:
    assertEquals(0, coder.read(coder.write(0)).intValue());
    assertEquals(1, coder.read(coder.write(1)).intValue());
    assertEquals(2, coder.read(coder.write(2)).intValue());
    assertEquals(Integer.MAX_VALUE, coder.read(coder.write(Integer.MAX_VALUE)).intValue());
  }

  @Test
  public void testStreamImpl() throws Exception {
    VarUInt coder = new VarUInt();
    Random rand = new Random();
    int N = 10000;
    IntList data = new IntList(N+4);

    // Check edge-case values:
    data.add(0);
    data.add(1);
    data.add(2);
    data.add(Integer.MAX_VALUE);
    for (int i = 0; i < N; i++) {
      int x = Math.abs(rand.nextInt());
      data.add(x);
    }

    ByteBuilder builder = new ByteBuilder();
    for (int i = 0; i < data.size(); i++) {
      int val = data.getQuick(i);
      coder.writePrim(builder.asOutputStream(), val);
    }

    InputStream stream = builder.asInputStream();
    for(int i=0; i<data.size(); i++) {
      assertEquals(data.getQuick(i), coder.readPrim(stream));
    }
  }
}