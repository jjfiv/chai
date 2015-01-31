package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.io.IO;

/**
* @author jfoley.
*/
public class UntilNullIterator<T> extends ReadOnlyIterator<T> {
	public final UntilNullGenerator<T> generator;
	public T nextValue;

	public UntilNullIterator(UntilNullGenerator<T> generator) {
		this.generator = generator;
		this.nextValue = generator.next();
	}

	@Override
	public boolean hasNext() {
		return nextValue != null;
	}

	@Override
	public T next() {
    T prevValue = nextValue;
    nextValue = generator.next();
    return prevValue;
	}

	@Override
	public void close() throws Exception {
		IO.close(generator);
	}
}
