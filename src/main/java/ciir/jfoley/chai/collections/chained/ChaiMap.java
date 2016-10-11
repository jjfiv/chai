package ciir.jfoley.chai.collections.chained;

import ciir.jfoley.chai.collections.MapWrapper;
import ciir.jfoley.chai.collections.Pair;
import ciir.jfoley.chai.collections.util.MapFns;
import ciir.jfoley.chai.fn.TransformFn;
import ciir.jfoley.chai.io.IO;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jfoley.
 */
public class ChaiMap<K,V> extends MapWrapper<K,V> implements AutoCloseable {
  ChaiMap(Map<K,V> inner) {
    super(inner);
  }

  @Override
  public void close() throws Exception {
    IO.close(inner);
  }

  public V getOrElse(K key, V otherwise) {
    return MapFns.getOrElse(inner, key, otherwise);
  }

  @Nonnull
  public ChaiIterable<Pair<K,V>> pairs() {
    return ChaiIterable.create(MapFns.pairs(inner));
  }

  @Nonnull
  public ChaiIterable<K> keys() {
    return ChaiIterable.create(inner.keySet());
  }

  @Nonnull
  public ChaiIterable<V> vals() {
    return ChaiIterable.create(inner.values());
  }

  @Nonnull
  public ChaiMap<K,V> readOnly() {
    return create(Collections.unmodifiableMap(this.inner));
  }

  @Nonnull
  public ChaiMap<V,K> invert() {
    return create(MapFns.invert(inner));
  }
  @Nonnull
  public ChaiMap<V,K> invert(Map<V,K> builder) {
    return create(MapFns.invert(inner, builder));
  }

  @Nonnull
  public <NV> ChaiMap<K,NV> mapValues(TransformFn<V, NV> valueMapFn) {
    return create(MapFns.mapValues(inner, valueMapFn));
  }

  @Nonnull
  public static <A,B> ChaiMap<A,B> create(Map<A,B> input) {
    return new ChaiMap<>(input);
  }
  @Nonnull
  public static <A,B> ChaiMap<A,B> create() {
    return create(new HashMap<A, B>());
  }

  @Nonnull
  public static <A,B> ChaiMap<A,B> create(Iterable<? extends Pair<A,B>> input, Map<A,B> builder) {
    for (Pair<A, B> kv : input) {
      builder.put(kv.left, kv.right);
    }
    return new ChaiMap<>(builder);
  }

  @Nonnull
  public static <A,B> ChaiMap<A,B> create(Iterable<? extends Pair<A,B>> input) {
    return create(input, new HashMap<A, B>());
  }

  @SafeVarargs
  @Nonnull
  public static <A,B> ChaiMap<A,B> create(Pair<A,B>... input) {
    return create(Arrays.asList(input));
  }

}
