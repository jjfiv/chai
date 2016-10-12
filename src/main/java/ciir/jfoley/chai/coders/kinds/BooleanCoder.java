package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteArray;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public class BooleanCoder extends Coder<Boolean> {
  public static final ByteArray TRUE = new ByteArray(new byte[] {1});
  public static final ByteArray FALSE = new ByteArray(new byte[] {0});

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return Boolean.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(@Nonnull Boolean obj) throws IOException {
    return obj ? TRUE : FALSE;
  }

  @Nonnull
  @Override
  public Boolean readImpl(InputStream inputStream) throws IOException {
    int x = inputStream.read();
    if(x == -1) throw new EOFException();
    return x == 1;
  }
}
