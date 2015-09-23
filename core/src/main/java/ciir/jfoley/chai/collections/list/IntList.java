package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.io.StreamFns;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author jfoley.
 */
public class IntList extends AChaiList<Integer> {
	int fill;
	int size;
	int[] data;

	public IntList() {
		this(16);
	}

	public IntList(int size) {
		this.size = IntMath.nearestPowerOfTwo(size);
		this.data = new int[this.size];
		this.fill = 0;
	}

	public IntList(Collection<Integer> other) {
		this(other.size());
		addAll(other);
	}

	public IntList(int[] backing) {
		this.data = backing;
		this.fill = backing.length;
		this.size = data.length;
	}

	@Override
	public boolean addAll(@Nonnull Collection<? extends Integer> other) {
		reserve(fill + other.size());
		for (Integer value : other) {
			if(value == null) throw new NullPointerException();
			data[fill++] = value;
		}
		return true;
	}

	/**
	 * Resize to exact value.
	 * @param amt the exact number of items this should be able to hold.
	 */
	public void resize(int amt) {
		data = Arrays.copyOf(data, amt);
	}

	/**
	 * Reserve at least amt number of items; uses the nearest power of two
	 * @param amt number of items to reserve
	 */
	public void reserve(int amt) {
		if(amt >= size) {
			size = IntMath.nearestPowerOfTwo(amt);
		}
		data = Arrays.copyOf(data, size);
	}

	@Override
	public boolean add(Integer value) {
		if(value == null) throw new NullPointerException();
		reserve(fill+1);
		data[fill++] = value;
		return true;
	}

	public void push(int x) {
		reserve(fill+1);
		data[fill++] = x;
	}

	@Override
	public void clear() {
		fill = 0;
	}

	@Override
	@Nonnull
	public Integer get(int index) {
		return data[index];
	}

  @Override
	@Nonnull
  public Integer set(int index, @Nonnull Integer value) {
    int prev = data[index];
    data[index] = value;
    return prev;
  }

	@Override
	public int size() {
		return fill;
	}

	@Override
	public boolean equals(Object other) {
		if(other == this) return true;
		if(other instanceof IntList) {
			IntList rhs = (IntList) other;
			if (this.fill != rhs.fill) return false;
			for (int i = 0; i < fill; i++) {
				if (this.data[i] != rhs.data[i]) return false;
			}
		}
		return super.equals(other);
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		for (int i = 0; i < fill; i++) {
			hashCode = 31*hashCode + Integer.hashCode(data[i]);
		}
		return hashCode;
	}

  /** This is toArray, but without the generic problems inherent to Java's toArray */
  public int[] asArray() {
    return Arrays.copyOfRange(data, 0, fill);
  }

  public int[] unsafeArray() {
    return this.data;
  }

  public int getQuick(int key) {
    return data[key];
  }

	public void sort() {
		Arrays.sort(this.data, 0, this.fill);
	}

	public void setSize(int amount) {
		fill = amount;
		if(fill > size) {
			resize(fill);
		}
	}

	public boolean containsInt(int i) {
		for (int index = 0; index < fill; index++) {
			if(data[index] == i) return true;
		}
		return false;
	}
	public int indexOfInt(int i) {
		for (int index = 0; index < fill; index++) {
			if(data[index] == i) return index;
		}
		return -1;
	}

	/**
	 * Encode into a byte[] array with fixed-size count then items; super-fast.
	 * Can get the info back with {@link #decode(InputStream)}
	 * @return array of length (this.size()+1)*4;
	 */
	public byte[] encode() {
		byte[] data = new byte[(fill+1)*4];
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.putInt(0, fill);
		for (int i = 0; i < fill; i++) {
			buf.putInt(4+i*4, this.data[i]);
		}
		return data;
	}

	/**
	 * Decode self from InputStream; opposite of {@link #encode()}
	 * @param in the InputStream to read from.
	 * @return a populated IntList
	 * @throws IOException generally just an EOFException unless your InputStream class is broken.
	 */
	public static IntList decode(InputStream in) throws IOException {
		// Read count first:
		ByteBuffer countBuf = ByteBuffer.wrap(StreamFns.readBytes(in, 4));
		int count = countBuf.getInt();

		// Read data now:
		int bytes = count*4;
		ByteBuffer data = ByteBuffer.wrap(StreamFns.readBytes(in, bytes));
		int[] target = new int[count];
		for (int i = 0; i < count; i++) {
			target[i] = data.getInt(i*4);
		}
		return new IntList(target);
	}

	/**
	 * Create an IntList based upon a slice of an original array:
	 * @param original the array
	 * @param pos the starting point
	 * @param size the size of the slice
	 * @return a copy of the the sub-data.
	 */
	public static IntList clone(int[] original, int pos, int size) {
		return new IntList(Arrays.copyOfRange(original, pos, pos+size));
	}
}
