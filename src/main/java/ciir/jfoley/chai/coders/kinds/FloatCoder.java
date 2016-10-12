package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author jfoley
 */
public class FloatCoder extends Coder<Float> {
  Coder<Integer> bitsCoder = FixedSize.ints;

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return Float.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(@Nonnull Float obj) throws IOException {
    return bitsCoder.writeImpl(Float.floatToRawIntBits(obj));
  }

  public void write(OutputStream out, Float obj) {
    bitsCoder.write(out, Float.floatToRawIntBits(obj));
  }

  @Nonnull
  @Override
  public Float readImpl(InputStream inputStream) throws IOException {
    return Float.intBitsToFloat(FixedSize.ints.read(inputStream));
  }
}
