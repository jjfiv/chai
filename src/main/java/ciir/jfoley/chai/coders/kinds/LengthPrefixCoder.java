package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.BufferList;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.io.StreamFns;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public class LengthPrefixCoder<T> extends Coder<T> {
  private final Coder<T> payloadCoder;

  public LengthPrefixCoder(@Nonnull Coder<T> payloadCoder) {
    assert !payloadCoder.knowsOwnSize() : "Should only length-prefix things that need prefixing.";
    this.payloadCoder = payloadCoder;
  }

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return payloadCoder.getTargetClass();
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(T obj) throws IOException {
    DataChunk payload = payloadCoder.writeImpl(obj);

    //TODO: ByteBuilder?
    BufferList bl = new BufferList();
    bl.add(VarUInt.instance, IntMath.fromLong(payload.byteCount()));
    bl.add(payload);
    return bl;
  }

  @Nonnull
  @Override
  public T readImpl(@Nonnull InputStream inputStream) throws IOException {
    int length = VarUInt.instance.readPrim(inputStream);
    byte[] data = StreamFns.readBytes(inputStream, length);
    return payloadCoder.read(ByteBuffer.wrap(data));
  }

  @Nonnull
  public static <V> Coder<V> wrap(@Nonnull Coder<V> inner) {
    if(inner.knowsOwnSize()) {
      return inner;
    } else {
      return new LengthPrefixCoder<>(inner);
    }
  }
}
