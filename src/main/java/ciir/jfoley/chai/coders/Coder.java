package ciir.jfoley.chai.coders;

import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.coders.kinds.LengthPrefixCoder;
import ciir.jfoley.chai.io.StreamFns;
import ciir.jfoley.chai.io.streams.StaticStream;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author jfoley
 */
public abstract class Coder<T> {
  @Nonnull
  public abstract Class<?> getTargetClass();

  /** Reading from a byte[] in memory. */
  @Nonnull
  public T read(byte[] data) {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
      return readImpl(bais);
    } catch (IOException e) {
      throw new CoderException(e, this.getClass());
    }
  }
  /** Reading from a ByteBuffer in memory. */
  @Nonnull
  public T read(ByteBuffer buf) {
    try (InputStream bais = StreamFns.fromByteBuffer(buf)) {
      return readImpl(bais);
    } catch (IOException e) {
      throw new CoderException(e, this.getClass());
    }
  }

  /** Reading from a DataChunk. */
  @Nonnull
  public T read(DataChunk data) {
    return read(data.asInputStream());
  }

  /** Reading from an InputStream. */
  @Nonnull
  public T read(InputStream is) {
    try {
      return readImpl(is);
    } catch (AssertionError | IOException e) {
      System.err.println("InputStreamClass for error: "+is.getClass());
      throw new CoderException(e, this.getClass());
    }
  }

  /** Write to a new ByteBuffer in memory. */
  @Nonnull
  public ByteBuffer write(T input) {
    try {
      return writeImpl(input).asByteBuffer();
    } catch (IOException e) {
      throw new CoderException(e, this.getClass());
    }
  }

  @Nonnull
  public DataChunk writeData(T input) {
    try {
      return writeImpl(input);
    } catch (IOException e) {
      throw new CoderException(e, this.getClass());
    }
  }
  /** Reading of something that can be read again. */
  @Nonnull
  public T read(StaticStream streamFn) throws IOException {
    return readImpl(streamFn.getNewStream());
  }

  /** If false, always wrap in a LengthPrefixCodec for the ability to read it when you're streaming things! */
  public abstract boolean knowsOwnSize();

  /** Writing, abstract, may throw IOException. */
  @Nonnull
  public abstract DataChunk writeImpl(T obj) throws IOException;
  /** Reading, abstract, may throw IOException. */
  @Nonnull
  public abstract T readImpl(InputStream inputStream) throws IOException;

  public void write(OutputStream out, T elem) {
    try {
      DataChunk chunk = writeImpl(elem);
      chunk.write(out);
    } catch (IOException ioe) {
      throw new CoderException(ioe, this.getClass());
    }
  }

  @Nonnull
  public Coder<T> lengthSafe() {
    return LengthPrefixCoder.wrap(this);
  }
}
