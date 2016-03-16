package ciir.jfoley.chai.lang;

import ciir.jfoley.chai.IntMath;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author jfoley
 */
public class MString implements CharSequence, Appendable {
  private static final char[] empty = new char[0];
  char[] data;
  private int fill = 0;
  private int hash = 0;

  public MString() {
    this.data = empty;
  }

  public int size() {
    return fill;
  }

  public MString(int capacity) {
    this.data = new char[capacity];
  }
  public MString(char[] backing) {
    this.data = backing;
    this.fill = backing.length;
    this.hash = 0;
  }
  public MString(String input) {
    this(input.length()+16);
    append(input);
  }
  /**
   * Resize to exact value.
   * @param amount the exact number of items this should be able to hold.
   */
  public void resize(int amount) {
    setLength(amount);
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
  public int length() {
    return fill;
  }

  @Override
  public char charAt(int index) {
    return data[index];
  }

  @Override
  public MString subSequence(int start, int end) {
    if(start < 0) throw new StringIndexOutOfBoundsException(start);
    if(start > end) throw new StringIndexOutOfBoundsException(end - start);
    if(end > fill) throw new StringIndexOutOfBoundsException(end);
    return new MString(Arrays.copyOfRange(this.data, start, end));
  }

  public int capacity() {
    return data.length;
  }

  @Override
  public MString append(CharSequence csq) {
    int extra = csq.length();
    reserve(fill+extra);
    for (int i = 0; i < extra; i++) {
      data[fill+i] = csq.charAt(i);
    }
    fill += extra;
    this.hash = 0;
    return this;
  }

  public MString append(char[] csq) {
    int extra = csq.length;
    reserve(fill+extra);
    System.arraycopy(csq, 0, data, fill, extra);
    fill += extra;
    this.hash = 0;
    return this;
  }

  @Override
  public Appendable append(CharSequence s, int start, int end) {
    if (s == null)
      s = "null";
    if ((start < 0) || (start > end) || (end > s.length()))
      throw new IndexOutOfBoundsException(
          "start " + start + ", end " + end + ", s.length() "
              + s.length());

    int extra = end - start;
    reserve(fill+extra);
    for (int i = 0; i < extra; i++) {
      data[fill+i] = s.charAt(start+i);
    }
    fill += extra;
    this.hash = 0;
    return this;
  }

  public final MString append(char ch) {
    reserve(fill+1);
    data[fill++] = ch;

    // build from empty...
    if(fill <= 1 || hash != 0) {
      // update hash as we go.
      hash = 31 * hash + ch;
    }
    return this;
  }

  public void setCharAt(int i, char c) {
    if ((i < 0) || (i >= fill))
      throw new StringIndexOutOfBoundsException(i);
    data[i] = c;
    hash = 0;
  }


  public void setLength(int i) {
    if (i < 0)
      throw new StringIndexOutOfBoundsException(i);
    reserve(i);
    if(fill < i) {
      Arrays.fill(data, fill, i, '\0');
    }
    fill = i;
    hash = 0;
  }

  @Nonnull
  @Override
  public String toString() {
    return new String(this.data, 0, fill);
  }

  @Override
  public int hashCode() {
    if(hash == 0) rehash();
    return hash;
  }

  @Override
  public boolean equals(Object other) {
    if(this == other) return true;
    if(other instanceof MString) {
      MString rhs = (MString) other;
      if(this.fill != rhs.fill) return false;

      for (int i = 0; i < fill; i++) {
        if(data[i] != rhs.data[i]) return false;
      }

      return true;
    } else if(other instanceof CharSequence) {
      CharSequence rhs = (CharSequence) other;
      if(this.fill != rhs.length()) return false;

      for (int i = 0; i < fill; i++) {
        if(data[i] != rhs.charAt(i)) return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Unrolled hashing taken from: http://lemire.me/blog/2015/10/22/faster-hashing-without-effort/
   */
  private void rehash() {
    char[] val = data;
    int len = fill;
    int h = 0;
    int i = 0;
    for (; i + 3 < len; i += 4) {
      h = 31 * 31 * 31 * 31 * h
          + 31 * 31 * 31 * val[i]
          + 31 * 31 * val[i + 1]
          + 31 * val[i + 2]
          + val[i + 3];
    }
    for (; i < len; i++) {
      h = 31 * h + val[i];
    }
    /*for (int i = 0; i < fill; i++) {
      h = 31 * h + data[i];
    }*/
    hash = h;
  }

  public void setBuffer(char[] buffer, int length) {
    this.data = buffer;
    this.fill = length;
    hash = 0;
  }

  public MString toLowerCase() {
    for (int i = 0; i < fill; i++) {
      data[i] = Character.toLowerCase(data[i]);
    }
    return this;
  }

  public void copyFrom(MString root) {
    this.data = Arrays.copyOf(root.data, root.fill);
    this.fill = root.fill;
    this.hash = 0;
  }
}
