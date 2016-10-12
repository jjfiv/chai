package ciir.jfoley.chai.coders.kinds;

import ciir.jfoley.chai.coders.CoderException;

/**
 * @author jfoley
 */
public class ClassNameCoder<T> extends MappingCoder<Class<? extends T>, String> {
  @SuppressWarnings("unchecked")
  public ClassNameCoder(Class<T> classWeExpect) {
    super(
        classWeExpect,
        CharsetCoders.utf8,
        Class::getName,
        (className) -> ofClassName(classWeExpect, className));
  }

  @SuppressWarnings("unchecked")
  private static <T> Class<? extends T> ofClassName(Class<T> classWeExpect, String className) {
    try {
      Class<?> classFound = Class.forName(className);
      if(!classWeExpect.isAssignableFrom(classFound)) {
        throw new CoderException("Expected a class compatible with: "+classWeExpect+" but found: "+classFound);
      }
      return (Class<? extends T>) classFound;
    } catch (ClassNotFoundException e) {
      throw new CoderException(e, ClassNameCoder.class);
    }
  }
}
