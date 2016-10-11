package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.codec.Codec;

import java.util.*;

/**
 * Untested for now.
 * @author jfoley.
 */
@SuppressWarnings("NullableProblems")
public class RWMapView<OUTERK, INNERK, OUTERV, INNERV> implements Map<OUTERK, OUTERV> {

	private final Codec<INNERK, OUTERK> keyCodec;
	private final Codec<INNERV, OUTERV> valCodec;
	private final Map<INNERK, INNERV> inner;

	public RWMapView(Map<INNERK, INNERV> inner, Codec<INNERK, OUTERK> keyCodec, Codec<INNERV, OUTERV> valCodec) {
		this.inner = inner;
		this.keyCodec = keyCodec;
		this.valCodec = valCodec;
	}

	@SuppressWarnings("unchecked")
	public INNERK innerKey(Object obj) {
		return keyCodec.unwrap((OUTERK) obj);
	}
	@SuppressWarnings("unchecked")
	public INNERV innerValue(Object obj) {
		return valCodec.unwrap((OUTERV) obj);
	}

	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public boolean isEmpty() {
		return !inner.isEmpty();
	}

	@Override
	public boolean containsKey(Object o) {
		return inner.containsKey(innerKey(o));
	}

	@Override
	public boolean containsValue(Object o) {
		return inner.containsValue(innerValue(o));
	}

	@Override
	public OUTERV get(Object o) {
		return valCodec.wrap(inner.get(innerKey(o)));
	}

	@Override
	public OUTERV put(OUTERK outerk, OUTERV outerv) {
		return valCodec.wrap(inner.put(keyCodec.unwrap(outerk), valCodec.unwrap(outerv)));
	}

	@Override
	public OUTERV remove(Object o) {
		return valCodec.wrap(inner.remove(innerKey(o)));
	}

	@Override
	public void putAll(Map<? extends OUTERK, ? extends OUTERV> map) {
		for (Entry<? extends OUTERK, ? extends OUTERV> kv : map.entrySet()) {
			inner.put(keyCodec.unwrap(kv.getKey()), valCodec.unwrap(kv.getValue()));
		}
	}

	@Override
	public void clear() {
		inner.clear();
	}

	@Override
	public Set<OUTERK> keySet() {
		HashSet<OUTERK> outer = new HashSet<>();
		for (INNERK innerk : inner.keySet()) {
			outer.add(keyCodec.wrap(innerk));
		}
		return outer;
	}

	@Override
	public Collection<OUTERV> values() {
		List<OUTERV> outer = new ArrayList<>();
		for (INNERV innerv : inner.values()) {
			outer.add(valCodec.wrap(innerv));
		}
		return outer;
	}

	@Override
	public Set<Entry<OUTERK, OUTERV>> entrySet() {
		HashSet<Entry<OUTERK, OUTERV>> kvs = new HashSet<>();
		for (Entry<INNERK, INNERV> kv : inner.entrySet()) {
			kvs.add(Pair.of(keyCodec.wrap(kv.getKey()), valCodec.wrap(kv.getValue())));
		}
		return kvs;
	}
}
