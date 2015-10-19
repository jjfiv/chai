package ciir.jfoley.chai.collections.list;

import ciir.jfoley.chai.io.StreamFns;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

/**
 * @author jfoley
 */
public class NibbleList extends AChaiList<Integer> {
  private int size;
  int fill = 0;
  IntList data;


  public NibbleList() {
    this(16);
  }
  public NibbleList(int size) {
    this.data = new IntList(numWordsNeeded(size));
  }

  public NibbleList(int[] target, int count) {
    this.data = new IntList(target);
    this.fill = count;
  }

  public static int numWordsNeeded(int numNibbles) {
    int bnw = numNibbles / 4;
    if(numNibbles % 4 > 0) {
      return bnw+1;
    }
    return bnw;
  }

  @Override
  public Integer get(int index) {
    return getQuick(index);
  }

  public int getQuick(int index) {
    if(index >= fill || index < 0) throw new NoSuchElementException("NibbleList["+index+"]");
    int nibble = index % 8;
    int word = index / 8;
    //System.err.printf("backing[%d]=%08x\n", word, data.getQuick(word));
    return (data.getQuick(word) >>> (nibble * 4)) & 0x0f;
  }

  public Integer set(int index, Integer element) {
    return setQuick(index, element);
  }

  public int setQuick(int index, int element) {
    if(index >= fill || index < 0) throw new NoSuchElementException("NibbleList["+index+"]");
    int nibble = index % 8;
    int word = index / 8;

    int[] back = data.unsafeArray();

    int shift = nibble*4;
    // pull prev bits:
    int prev = (back[word] >>> shift) & 0x0f;
    // clear bits:
    back[word] &= ~(0x0f << shift);
    // set new nibble
    back[word] |= (element & 0x0f) << shift;
    return prev;
  }

  public void push(int element) {
    int target = fill;
    int word = target / 8;
    // if it does not have a word prepared:
    if(data.fill <= word) {
      data.push(0);
    }
    int nibble = target % 8;

    int[] back = data.unsafeArray();
    int mask = ((element & 0x0f) << (nibble*4));
    //System.err.printf("Mask: nibble[%d; %d:%d] = 0x%08x\n",target, word, nibble, mask);
    back[word] |= mask;
    fill++;
  }

  @Override
  public int size() {
    return fill;
  }

  /**
   * Resize to exact value.
   * @param amt the exact number of items this should be able to hold.
   */
  public void resize(int amt) {
    data.resize(numWordsNeeded(amt));
  }

  /**
   * Reserve at least amt number of items; uses the nearest power of two
   * @param amt number of items to reserve
   */
  public void reserve(int amt) {
    data.reserve(numWordsNeeded(amt));
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

    int fillWords = numWordsNeeded(fill);
    int backing[] = this.data.unsafeArray();

    for (int i = 0; i < fillWords; i++) {
      buf.putInt(4+i*4, backing[i]);
    }
    return data;
  }

  /**
   * Decode self from InputStream; opposite of {@link #encode()}
   * @param in the InputStream to read from.
   * @return a populated IntList
   * @throws IOException generally just an EOFException unless your InputStream class is broken.
   */
  public static NibbleList decode(InputStream in) throws IOException {
    // Read count first:
    ByteBuffer countBuf = ByteBuffer.wrap(StreamFns.readBytes(in, 4));
    int count = countBuf.getInt();
    int countWords = numWordsNeeded(count);

    if(countWords == 0) {
      return new NibbleList();
    }

    // Read data now:
    int bytes = countWords*4;
    ByteBuffer data = ByteBuffer.wrap(StreamFns.readBytes(in, bytes));
    int[] target = new int[countWords];
    for (int i = 0; i < countWords; i++) {
      target[i] = data.getInt(i*4);
    }
    return new NibbleList(target, count);
  }

}
