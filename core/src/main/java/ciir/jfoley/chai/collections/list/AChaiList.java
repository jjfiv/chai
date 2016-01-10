package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.collections.chained.ChaiIterable;

import java.util.AbstractList;
import java.util.Random;

/**
 * @author jfoley.
 */
public abstract class AChaiList<T> extends AbstractList<T> {
	public ChaiIterable<T> chai() {
		return ChaiIterable.create(this);
	}


	public T chooseRandomly(Random rand) {
		return this.get(rand.nextInt(this.size()));
	}
}
