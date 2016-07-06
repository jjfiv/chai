package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.io.StreamFns;
import gnu.trove.procedure.TIntProcedure;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author jfoley.
 */
public final class IntList extends AChaiList<Integer> {
	int fill;
	private int[] data;

	public IntList() {
		this(16);
	}

	public IntList(int size) {
		size = IntMath.nearestPowerOfTwo(size);
		this.data = new int[size];
		this.fill = 0;
	}

	public IntList(Collection<Integer> other) {
		this(other.size());
		addAll(other);
	}

	public IntList(int[] backing) {
		this.data = backing;
		this.fill = backing.length;
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

	public boolean pushAll(IntList right) {
		final int size = right.size();
		reserve(fill + size);
		final int[] rhs = right.unsafeArray();
		System.arraycopy(rhs, 0, data, fill, size);
		fill += size;
		return true;
	}

	/**
	 * Resize to exact value.
	 * @param amt the exact number of items this should be able to hold.
	 */
	public void resize(int amt) {
		data = Arrays.copyOf(data, amt);
		fill = amt;
	}

	/**
	 * Reserve at least amt number of items; uses the nearest power of two
	 * @param amt number of items to reserve
	 */
	public final void reserve(int amt) {
		int size = data.length;
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

	public final void push(int x) {
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
		if(fill > data.length) {
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
	public boolean removeInt(int i) {
		int pos = indexOfInt(i);
		if(pos == -1) return false;
		for (int j = pos+1; j < fill; j++) {
			data[j-1] = data[j];
		}
		fill--;
		return true;
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

		if(count == 0) {
			return new IntList();
		}

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

	public void shuffle() {
		Collections.shuffle(this);
	}

	public void swap(int i, int j) {
		int tmp = data[i];
		data[i] = data[j];
		data[j] = tmp;
	}

	public void setQuick(int word, int val) {
		data[word] = val;
	}

	public void unsafeSetFill(int fill) {
		this.fill = fill;
	}

	public int capacity() {
		return data.length;
	}


	public void forEach(TIntProcedure apply) {
		for (int each : data) {
			if (!apply.execute(each)) { break; }
		}
	}
}
