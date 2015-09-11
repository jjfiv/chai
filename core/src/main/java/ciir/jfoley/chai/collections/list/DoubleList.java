package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.IntMath;
import ciir.jfoley.chai.math.StreamingStats;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author jfoley
 */
public class DoubleList extends AChaiList<Double> {
  int fill;
  int size;
  double[] data;

  public DoubleList() {
    this(16);
  }
  public DoubleList(int size) {
    this.size = IntMath.nearestPowerOfTwo(size);
    this.data = new double[this.size];
    this.fill = 0;
  }

  public DoubleList(Collection<Double> other) {
    this(other.size());
    addAll(other);
  }

  public DoubleList(double[] backing) {
    this.data = backing;
    this.fill = backing.length;
    this.size = data.length;
  }


  @Override
  public boolean addAll(@Nonnull Collection<? extends Double> other) {
    reserve(fill + other.size());
    for (Double value : other) {
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
  public boolean add(Double value) {
    if(value == null) throw new NullPointerException();
    reserve(fill+1);
    data[fill++] = value;
    return true;
  }

  public void push(double x) {
    reserve(fill+1);
    data[fill++] = x;
  }

  @Override
  public void clear() {
    fill = 0;
  }

  @Override
  @Nonnull
  public Double get(int index) {
    return data[index];
  }

  @Override
  @Nonnull
  public Double set(int index, @Nonnull Double value) {
    double prev = data[index];
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
    if(other instanceof DoubleList) {
      DoubleList rhs = (DoubleList) other;
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
      hashCode = 31*hashCode + Double.hashCode(data[i]);
    }
    return hashCode;
  }

  /** This is toArray, but without the generic problems inherent to Java's toArray */
  public double[] asArray() {
    return Arrays.copyOfRange(data, 0, fill);
  }

  public double[] unsafeArray() {
    return this.data;
  }

  public double getQuick(int key) {
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
  
  public double mean() {
    if(fill == 0) return 0;
    double sum = 0;
    for (int i = 0; i < fill; i++) {
      sum += data[i];
    }
    return sum / (double) fill;
  }
  public double sum() {
    double sum = 0;
    for (int i = 0; i < fill; i++) {
      sum += data[i];
    }
    return sum;
  }

  public void toStats(StreamingStats target) {
    for (int i = 0; i < fill; i++) {
      target.push(data[i]);
    }
  }
}
