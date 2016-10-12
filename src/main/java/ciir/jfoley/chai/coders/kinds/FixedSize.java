package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.CoderException;
import ciir.jfoley.chai.coders.data.ByteBufferDataChunk;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.io.StreamFns;
import ciir.jfoley.chai.lang.Module;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public class FixedSize extends Module {
  public static final Coder<Integer> ints = new Coder<Integer>() {
    @Nonnull
    @Override
    public Class<?> getTargetClass() {
      return Integer.class;
    }

    @Override
    public boolean knowsOwnSize() {
      return true;
    }

    @Nonnull
    @Override
    public DataChunk writeImpl(Integer obj) throws IOException {
      ByteBuffer ofInt = ByteBuffer.allocate(4);
      ofInt.putInt(0, obj);
      return ByteBufferDataChunk.of(ofInt);
    }

    public void write(OutputStream out, Integer obj) {
      byte[] data = new byte[4];
      ByteBuffer ofInt = ByteBuffer.wrap(data);
      ofInt.putInt(0, obj);
      try {
        out.write(data);
      } catch (IOException e) {
        throw new CoderException(e, this.getClass());
      }
    }

    @Nonnull
    @Override
    public Integer readImpl(InputStream inputStream) throws IOException {
      return ByteBuffer.wrap(StreamFns.readBytes(inputStream, 4)).getInt();
    }
  };
  public static final Coder<Long> longs = new Coder<Long>() {
    @Nonnull
    @Override
    public Class<?> getTargetClass() {
      return Long.class;
    }

    @Override
    public boolean knowsOwnSize() {
      return true;
    }

    @Nonnull
    @Override
    public DataChunk writeImpl(Long obj) throws IOException {
      ByteBuffer tmp = ByteBuffer.allocate(8);
      tmp.putLong(0, obj);
      return ByteBufferDataChunk.of(tmp);
    }

    @Nonnull
    @Override
    public Long readImpl(InputStream inputStream) throws IOException {
      return ByteBuffer.wrap(StreamFns.readBytes(inputStream, 8)).getLong();
    }
  };
}
