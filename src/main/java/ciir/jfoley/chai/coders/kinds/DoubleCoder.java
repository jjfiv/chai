package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public class DoubleCoder extends Coder<Double> {
  Coder<Long> bitCoder = FixedSize.longs;

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return Double.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(Double obj) throws IOException {
    return bitCoder.writeImpl(Double.doubleToRawLongBits(obj));
  }

  @Nonnull
  @Override
  public Double readImpl(InputStream inputStream) throws IOException {
    return Double.longBitsToDouble(bitCoder.readImpl(inputStream));
  }
}
