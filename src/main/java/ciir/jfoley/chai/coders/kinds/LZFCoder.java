package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.CoderException;
import ciir.jfoley.chai.coders.data.ByteBuilder;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.io.StreamFns;
import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFOutputStream;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * LZF is one of the fastest pure-java compression libraries.
 * This wraps any coder and gives it quick compression capabilities.
 * Note that this *does* length-prefix so its size is known.
 * @author jfoley
 */
public class LZFCoder<T> extends Coder<T> {
  final Coder<T> innerCoder;
  final Coder<Integer> sizeCoder = VarUInt.instance;

  public LZFCoder(Coder<T> innerCoder) {
    this.innerCoder = innerCoder;
  }

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return innerCoder.getTargetClass();
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(T obj) throws IOException {
    // compress:
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (LZFOutputStream lzfo = new LZFOutputStream(baos)) {
      innerCoder.write(lzfo, obj);
    }

    // length-prefix:
    ByteBuilder output = new ByteBuilder();
    byte[] compressed = baos.toByteArray();
    output.add(sizeCoder, compressed.length);
    output.add(compressed);
    return output;
  }

  public void write(OutputStream out, T elem) {
    try {
      // compress:
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try (LZFOutputStream lzfo = new LZFOutputStream(baos)) {
        innerCoder.write(lzfo, elem);
      }

      // length-prefix:
      sizeCoder.write(out, baos.size());
      baos.writeTo(out);
    } catch (IOException ioe) {
      throw new CoderException(ioe, this.getClass());
    }
  }

  @Nonnull
  @Override
  public T readImpl(InputStream inputStream) throws IOException {
    int compressedSize = sizeCoder.readImpl(inputStream);
    return innerCoder.read(
        LZFDecoder.decode(
            StreamFns.readBytes(inputStream, compressedSize)));
  }
}
