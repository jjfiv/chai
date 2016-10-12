package ciir.jfoley.chai.io.streams;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NaiveSkipInputStreamTest {

  @Test
  public void testTell() throws Exception {
    byte[] data = {0,1,2,3,4,5,6,7,8,9};
    SkipInputStream sis = SkipInputStream.wrap(new ByteArrayInputStream(data));
    for (int i = 0; i < 10; i++) {
      assertEquals(i, sis.tell());
      assertEquals(i, sis.read());
    }
  }

  @Test
  public void testSeek() throws Exception {
    byte[] data = {0,1,2,3,4,5,6,7,8,9};
    SkipInputStream sis = SkipInputStream.wrap(new ByteArrayInputStream(data));
    sis.seek(4);
    for (int i = 4; i < 10; i++) {
      assertEquals(i, sis.tell());
      assertEquals(i, sis.read());
    }
  }

  @Test
  public void testReadInBulk() throws Exception {
    byte[] data = {0,1,2,3,4,5,6,7,8,9};
    byte[] buf = new byte[5];
    SkipInputStream sis = SkipInputStream.wrap(new ByteArrayInputStream(data));
    assertEquals(buf.length, sis.read(buf));
    assertEquals(5, sis.tell());
    assertArrayEquals(new byte[]{0,1,2,3,4}, buf);

    assertEquals(2, sis.read(buf, 2, 2));
    assertArrayEquals(new byte[]{0,1,5,6,4}, buf);
  }
}