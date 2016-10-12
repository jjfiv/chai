package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.data.ByteBuilder;
import ciir.jfoley.chai.coders.data.DataChunk;
import ciir.jfoley.chai.collections.ArrayListMap;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Encode a map in traversal order: size, (KV)+
 * @author jfoley
 */
public class InterleavedMapCoder<K,V> extends Coder<Map<K,V>> {
  private final Coder<Integer> countCoder;
  private final Coder<K> keyCoder;
  private final Coder<V> valCoder;

  public InterleavedMapCoder(Coder<K> keyCoder, Coder<V> valCoder) {
    this(VarUInt.instance, keyCoder, valCoder);
  }
  public InterleavedMapCoder(Coder<Integer> countCoder, Coder<K> keyCoder, Coder<V> valCoder) {
    this.countCoder = countCoder.lengthSafe();
    this.keyCoder = keyCoder.lengthSafe();
    this.valCoder = valCoder.lengthSafe();
  }

  @Nonnull
  @Override
  public Class<?> getTargetClass() {
    return Map.class;
  }

  @Override
  public boolean knowsOwnSize() {
    return true;
  }

  @Nonnull
  @Override
  public DataChunk writeImpl(Map<K, V> obj) throws IOException {
    ByteBuilder output = new ByteBuilder();
    output.add(countCoder, obj.size());
    for (Map.Entry<K, V> kvEntry : obj.entrySet()) {
      output.add(keyCoder, kvEntry.getKey());
      output.add(valCoder, kvEntry.getValue());
    }
    return output;
  }

  @Nonnull
  @Override
  public Map<K, V> readImpl(InputStream inputStream) throws IOException {
    int count = countCoder.read(inputStream);
    Map<K,V> output = new ArrayListMap<>(count);
    for (int i = 0; i < count; i++) {
      K key = keyCoder.read(inputStream);
      V val = valCoder.read(inputStream);
      output.put(key, val);
    }
    return output;
  }
}
