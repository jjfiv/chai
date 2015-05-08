package ciir.jfoley.chai.collections.tree;

import ciir.jfoley.chai.fn.Fns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.lang.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * Given classes that implement ChaiTree, here are some common traversal operations.
 *
 * @author jfoley.
 */
public class TreeFns extends Module {
  public static <T extends ChaiTree<T>> List<T> findAllChildren(T start) {
    return findChildren(start, Fns.<T>trueFn());
  }

  /** Entry point to finding matching children. */
  public static <T extends ChaiTree<T>> List<T> findChildren(T start, PredicateFn<T> matcher) {
    ArrayList<T> output = new ArrayList<>();
    findChildrenR(start, output, matcher);
    return output;
  }

  /**
   * Private recursive step, check start and then propagate to start's children.
   */
  static <T extends ChaiTree<T>> void findChildrenR(T start, List<T> output, PredicateFn<T> matcher) {
    if(matcher.test(start)) {
      output.add(start);
    }
    for (T t : start.getChildren()) {
      findChildrenR(t, output, matcher);
    }
  }

  /** Find all ancestors. */
  public static <T extends ChaiTree<T>> List<T> findAncestors(T start) {
    return findAncestors(start, Fns.<T>trueFn());
  }

  /** Find ancestors matching the given predicate. */
  public static <T extends ChaiTree<T>> List<T> findAncestors(T start, PredicateFn<T> matcher) {
    ArrayList<T> output = new ArrayList<>();
    for(T node = start; node != null; node = node.getParent()) {
      if(matcher.test(node)) output.add(node);
    }
    return output;
  }
}
