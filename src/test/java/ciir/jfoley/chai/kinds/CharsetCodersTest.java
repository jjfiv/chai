package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.kinds.ASCII;
import ciir.jfoley.chai.coders.kinds.CharsetCoders;
import ciir.jfoley.chai.random.Sample;
import ciir.jfoley.chai.string.StrUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class CharsetCodersTest {
  @Test
  public void testStrings() throws IOException {
    Coder<String> utf8 = CharsetCoders.utf8;
    for (String str : Sample.strings(new Random(), 1000)) {
      assertEquals(str, utf8.read(utf8.writeImpl(str)));
    }
  }

  @Test
  public void testASCII() throws IOException {
    Coder<String> coder = new ASCII.NullTerminated();
    for (String str : Sample.letters(new Random(), 1000)) {
      assertEquals(str, coder.read(coder.writeImpl(str)));
    }
  }

  @Test
  public void testFixedASCII() throws IOException {
    Coder<String> coder = new ASCII.FixedLength(8);
    for (String orig : Sample.letters(new Random(), 1000)) {
      String str = StrUtil.slice(orig, 0, 8);
      assertEquals(str, coder.read(coder.writeImpl(str)));
    }
  }
}