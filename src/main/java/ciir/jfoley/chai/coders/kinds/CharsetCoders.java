package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteBufferDataChunk;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.io.IO;
import ciir.jfoley.chai.lang.Module;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @author jfoley
 */
public class CharsetCoders extends Module {
  public static final Charset utf8Charset = Charset.forName("UTF-8");

  /**
   * This is a UTF-8 string coder. If you want a length-prefixed version, call .lengthSafe() on it.
   */
  public static final Coder<String> utf8 = new Coder<String>() {
    @Nonnull
    @Override
    public DataChunk writeImpl(String obj) throws IOException {
      return ByteBufferDataChunk.of(obj.getBytes(utf8Charset));
    }

    @Nonnull
    @Override
    public String readImpl(InputStream inputStream) throws IOException {
      InputStreamReader reader = new InputStreamReader(inputStream, utf8Charset);
      return IO.readAll(reader);
    }

    @Nonnull
    @Override
    public Class<String> getTargetClass() {
      return String.class;
    }

    @Nonnull
    @Override
    public String read(ByteBuffer buf) {
      return new String(buf.array(), buf.arrayOffset(), buf.limit(), utf8Charset);
    }

    @Override
    public boolean knowsOwnSize() {
      return false;
    }
  };
}
