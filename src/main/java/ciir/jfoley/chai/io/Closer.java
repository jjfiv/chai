package ciir.jfoley.chai.io;

/**
 * This makes an object that may or may not have a close method usable in a try-with-resources block.
 * @author jfoley.
 */
public class Closer<T> implements AutoCloseable {
	private final T obj;

	public Closer(T obj) {
		this.obj = obj;
	}

	public T get() {
		return obj;
	}

	@Override
	public void close() {
		IO.close(obj);
	}

	public static <T> Closer<T> of(T input) {
		return new Closer<>(input);
	}
}
