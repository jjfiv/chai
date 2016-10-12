package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.CoderException;
import ciir.jfoley.chai.coders.data.BufferList;
import ciir.jfoley.chai.coders.kinds.CharsetCoders;
import ciir.jfoley.chai.io.StreamFns;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class UTF8Test {

  @Test
  public void testWithoutLengthPrefix() {
    Coder<String> c = CharsetCoders.utf8;
    assertFalse(c.knowsOwnSize());

    BufferList output = new BufferList();
    output.add(c, "this will ");
    output.add(c, "get concatenated on read!");
    assertEquals("this will get concatenated on read!", c.read(output.asByteBuffer()));
  }

  @Test
  public void testWithLengthPrefix() throws IOException {
    Coder<String> c = CharsetCoders.utf8.lengthSafe();
    assertTrue(c.knowsOwnSize());

    BufferList output = new BufferList();
    output.add(c, "this will not ");
    output.add(c, "get concatenated on read!");

    // Make sure the first byte has a vbyte ending marker.
    assert((output.getByte(0) & 0x80) > 0);

    InputStream input = StreamFns.fromByteBuffer(output.asByteBuffer());
    assertEquals("this will not ", c.read(input));
    assertEquals("get concatenated on read!", c.read(input));

    try {
      String ignored = c.read(input);
      assertNull(ignored);
      fail("Shouldn't get here.");
    } catch (CoderException ex) {
      assertTrue(ex.getCause() instanceof EOFException);
    }
  }

}