package ciir.jfoley.chai.kinds;

import ciir.jfoley.chai.coders.Coder;
import ciir.jfoley.chai.coders.kinds.ClassNameCoder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author jfoley
 */
public class ClassNameCoderTest {
  @Test
  public void testClassNameCoder() {
    Class<Iterable> iterableClass = Iterable.class;
    Class<List> listClass = List.class;

    Coder<Class<? extends Iterable>> coder = new ClassNameCoder<>(iterableClass);
    Class<? extends Iterable> foundClass = coder.read(coder.write(listClass));
    assertEquals(listClass, foundClass);
  }

}