package ciir.jfoley.chai.io;

import java.util.Arrays;

/**
 * @author jfoley
 */
public class ClassDataDirectory {
  public static Directory getForObject(Object forWhat) {
    return getForClass(forWhat.getClass());
  }
  public static Directory getForClass(Class<?> forWhatClass) {
    System.out.println(Arrays.toString(forWhatClass.getCanonicalName().split("\\.")));
    System.out.println(forWhatClass.getCanonicalName());
    Directory data = new Directory("data");

    for (String classPathPart : forWhatClass.getCanonicalName().split("\\.")) {
      data = data.childDir(classPathPart);
    }
    return data;
  }

  public static void main(String[] args) {
    System.out.println(getForClass(ClassDataDirectory.class));
  }
}
