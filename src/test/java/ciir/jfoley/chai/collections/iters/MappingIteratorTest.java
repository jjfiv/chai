package ciir.jfoley.chai.collections.iters;

import ciir.jfoley.chai.collections.util.IterableFns;
import ciir.jfoley.chai.fn.TransformFn;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class MappingIteratorTest {

  @Test
  public void testSimpleMapping() {
    List<Integer> data = Arrays.asList(1,2,3,4,5);
    Iterator<String> x = new MappingIterator<>(data.iterator(), new TransformFn<Integer, String>() {
      @Override public String transform(Integer input) { return Integer.toString(input); }
    });
    assertEquals(Arrays.asList("1", "2", "3", "4", "5"), IterableFns.intoList(new OneShotIterable<>(x)));
  }
}