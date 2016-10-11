package ciir.jfoley.chai.collections.list;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class NibbleListTest {

  @Test
  public void testSimple() {
    int N = 237;
    IntList larger = new IntList(N);
    NibbleList compact = new NibbleList(N);
    Random rand = new Random(13);
    for (int i = 0; i < N; i++) {
      int nibble = rand.nextInt(0xf);
      //System.err.printf("n[%d]=%d\n", i, nibble);
      larger.push(nibble);
      compact.push(nibble);
    }

    for (int i = 0; i < larger.size(); i++) {
      int lhs = larger.getQuick(i);
      int rhs = compact.getQuick(i);
      //System.err.printf("%d == %d \n", lhs, rhs);
      assertEquals(lhs, rhs);
    }
  }

  @Test
  public void testSimpleSet() {
    int N = 237;
    IntList larger = new IntList(N);
    NibbleList compact = new NibbleList(N);
    Random rand = new Random(13);

    for (int i = 0; i < N; i++) {
      compact.push(7);
    }

    for (int i = 0; i < N; i++) {
      int nibble = rand.nextInt(0xf);
      //System.err.printf("n[%d]=%d\n", i, nibble);
      larger.push(nibble);
      compact.set(i, nibble);
    }

    for (int i = 0; i < larger.size(); i++) {
      int lhs = larger.getQuick(i);
      int rhs = compact.getQuick(i);
      //System.err.printf("%d == %d \n", lhs, rhs);
      assertEquals(lhs, rhs);
    }
  }

  @Test
  public void testSimpleDecode() throws IOException {
    for (int N : Arrays.asList(237, 15, 7, 0)){
      NibbleList compact = new NibbleList(N);
      Random rand = new Random(13);
      for (int i = 0; i < N; i++) {
        int nibble = rand.nextInt(0xf);
        compact.push(nibble);
      }

      byte[] data = compact.encode();
      NibbleList decode = NibbleList.decode(new ByteArrayInputStream(data));

      assertEquals(decode, compact);
      for (int i = 0; i < compact.size(); i++) {
        assertEquals(compact.getQuick(i), decode.getQuick(i));
      }
    }
  }

  @Test
  public void testDecodeAndThen() throws IOException {
    for (int N : Arrays.asList(237, 15, 7, 0)){
      NibbleList compact = new NibbleList(N);
      Random rand = new Random(13);
      for (int i = 0; i < N; i++) {
        int nibble = rand.nextInt(0xf);
        compact.push(nibble);
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(compact.encode());
      baos.write(1);
      baos.write(2);
      baos.write(3);
      baos.write(4);

      byte[] data = baos.toByteArray();
      InputStream input = new ByteArrayInputStream(data);

      NibbleList decode = NibbleList.decode(input);
      assertEquals(1, input.read());
      assertEquals(2, input.read());
      assertEquals(3, input.read());
      assertEquals(4, input.read());


      assertEquals(decode, compact);
      for (int i = 0; i < compact.size(); i++) {
        assertEquals(compact.getQuick(i), decode.getQuick(i));
      }
    }
  }


}
