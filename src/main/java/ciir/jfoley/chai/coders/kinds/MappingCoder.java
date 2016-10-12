package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.fn.TransformFn;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jfoley
 */
public class MappingCoder<T, INNER> extends Coder<T> {
  private final Class<?> targetClass;
  private final Coder<INNER> baseCoder;
  private final TransformFn<T, INNER> wrapFn;
  private final TransformFn<INNER, T> unwrapFn;

  public MappingCoder(Class<?> targetClass, Coder<INNER> baseCoder, TransformFn<T,INNER> wrapFn, TransformFn<INNER,T> unwrapFn) {
    this.targetClass = targetClass;
    this.baseCoder = baseCoder;
    this.wrapFn = wrapFn;
    this.unwrapFn = unwrapFn;
  }

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return targetClass;
  }

  @Override
  public boolean knowsOwnSize() {
    return baseCoder.knowsOwnSize();
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(T obj) throws IOException {
    return baseCoder.writeImpl(wrapFn.transform(obj));
  }

  @Nonnull
  @Override
  public T readImpl(InputStream inputStream) throws IOException {
    return unwrapFn.transform(baseCoder.readImpl(inputStream));
  }
}
