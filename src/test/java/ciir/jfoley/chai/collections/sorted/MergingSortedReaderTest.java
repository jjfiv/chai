package ciir.jfoley.chai.collections.sorted;

import ciir.jfoley.chai.collections.IntRange;
import ciir.jfoley.chai.collections.list.IntList;
import ciir.jfoley.chai.collections.util.ListFns;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class MergingSortedReaderTest {

  static class ListSortedReader<T extends Comparable<T>> implements SortedReader<T> {
    List<T> data;
    int index;

    ListSortedReader(List<T> data) {
      this.data = new ArrayList<>(data);
      Collections.sort(this.data);
      this.index = 0;
    }

    @Override
    public T peek() {
      if(index >= data.size()) {
        return null;
      }
      return data.get(index);
    }

    @Override
    public T next() {
      return data.get(index++);
    }

    @Override
    public void close() throws IOException {
      data = null;
    }

    @Override
    public String toString() {
      return data.subList(index, data.size()).toString();
    }
  }

  @Test
  public void simpleTest() throws IOException {
    List<List<Integer>> data = Arrays.asList(
        IntRange.exclusive(0, 10),
        IntRange.exclusive(3, 10)
    );

    MergingSortedReader<Integer> reader = new MergingSortedReader<>(Integer::compare, ListFns.map(data, ListSortedReader::new));
    IntList collected = new IntList();
    reader.forAll(collected::add);

    MergingSortedReader<Integer> reader2 = new MergingSortedReader<>(Integer::compare, ListFns.map(data, ListSortedReader::new));

    IntList collected2 = new IntList();
    while(reader2.hasNext()) {
      collected2.add(reader2.next());
    }

    IntList normal = new IntList();
    for (List<Integer> integers : data) {
      normal.addAll(integers);
    }
    normal.sort();
    assertEquals(normal, collected2);
    assertEquals(normal, collected);
  }

  @Test
  public void simpleTest2() throws IOException {
    List<List<Integer>> data = Arrays.asList(
        IntRange.exclusive(0, 10),
        IntRange.exclusive(0, 3),
        IntRange.exclusive(1, 20),
        IntRange.exclusive(-1, 1),
        IntRange.exclusive(3, 10)
    );

    MergingSortedReader<Integer> reader = new MergingSortedReader<>(Integer::compare, ListFns.map(data, ListSortedReader::new));
    IntList collected = new IntList();
    reader.forAll(collected::add);

    MergingSortedReader<Integer> reader2 = new MergingSortedReader<>(Integer::compare, ListFns.map(data, ListSortedReader::new));

    IntList collected2 = new IntList();
    while(reader2.hasNext()) {
      collected2.add(reader2.next());
    }

    IntList normal = new IntList();
    for (List<Integer> integers : data) {
      normal.addAll(integers);
    }
    normal.sort();
    assertEquals(normal, collected2);
    assertEquals(normal, collected);
  }
}