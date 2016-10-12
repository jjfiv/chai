package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteArray;
import ciir.jfoley.chai.coders.data.DataChunk;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley
 */
public class ListCoder<T> extends Coder<List<T>> {

  private final Coder<Integer> countCoder;
  private final Coder<T> itemCoder;

  public ListCoder(Coder<T> itemCoder) {
    this(VarUInt.instance, itemCoder);
  }
  public ListCoder(Coder<Integer> countCoder, Coder<T> itemCoder) {
    this.countCoder = countCoder.lengthSafe();
    this.itemCoder = itemCoder.lengthSafe();
  }

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return List.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(List<T> obj) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int count = obj.size();
    countCoder.write(baos, count);
    for (T t : obj) {
      itemCoder.write(baos, t);
    }
    baos.close();
    return new ByteArray(baos.toByteArray());
  }

  @Nonnull
  @Override
  public List<T> readImpl(InputStream inputStream) throws IOException {
    int amount = countCoder.read(inputStream);
    List<T> output = new ArrayList<>(amount);
    for (int i = 0; i < amount; i++) {
      output.add(itemCoder.read(inputStream));
    }
    return output;
  }

  @Override
  public void write(OutputStream out, List<T> obj) {
    int count = obj.size();
    countCoder.write(out, count);
    for (T t : obj) {
      itemCoder.write(out, t);
    }
  }
}
