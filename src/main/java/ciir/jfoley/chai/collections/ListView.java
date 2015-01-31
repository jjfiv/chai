package ciir.jfoley.chai.collections;

import ciir.jfoley.chai.Checked;
import ciir.jfoley.chai.codec.Codec;

import java.util.*;

/**
 * @author jfoley.
 */
public class ListView<INNER, OUTER> extends AbstractList<OUTER> {
	private final List<INNER> inner;
	private final Codec<INNER, OUTER> codec;

	public ListView(List<INNER> inner, Codec<INNER, OUTER> codec) {
		assert (inner != null);
		assert (codec != null);
		this.inner = inner;
		this.codec = codec;
	}

	@Override
	public OUTER get(int i) {
		return codec.wrap(inner.get(i));
	}

	@Override
	public OUTER set(int i, OUTER outer) {
		INNER prev = inner.set(i, codec.unwrap(outer));
		return codec.wrap(prev);
	}

	@Override
	public void add(int i, OUTER outer) {
		inner.add(i, codec.unwrap(outer));
	}

	@Override
	public OUTER remove(int i) {
		return codec.wrap(inner.remove(i));
	}

	@Override
	public int indexOf(Object o) {
		return inner.indexOf(codec.unwrap(Checked.<OUTER>cast(o)));
	}

	@Override
	public int lastIndexOf(Object o) {
		return inner.lastIndexOf(codec.unwrap(Checked.<OUTER>cast(o)));
	}

	@Override
	public int size() {
		return inner.size();
	}
}

