package ciir.jfoley.chai.collections.list;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author jfoley.
 */
public class IntList extends AbstractList<Integer> {
	int fill;
	int size;
	int[] data;

	public IntList() {
		this(16);
	}

	public IntList(int size) {
		this.data = new int[size];
		this.size = size;
		this.fill = 0;
	}

	public IntList(Collection<Integer> other) {

	}

	/*public void reserve(int amt) {
		if(amt >= size) {

		}
	}*/

	@Override
	public boolean add(Integer value) {
		if(value == null) throw new NullPointerException();
		if(fill >= size) {
			expand();
		}
		data[fill++] = value;
		return true;
	}

	@Override
	public void clear() {
		fill = 0;
	}

	private void expand() {
		size *= 2;
		data = Arrays.copyOf(data, size);
	}

	@Override
	public Integer get(int index) {
		return data[index];
	}

	@Override
	public int size() {
		return fill;
	}
}
