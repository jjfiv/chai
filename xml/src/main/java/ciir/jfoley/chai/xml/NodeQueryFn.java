package ciir.jfoley.chai.xml;

import org.w3c.dom.Node;

/**
 * @author jfoley.
 */
public interface NodeQueryFn {
  boolean matches(Node input);

  NodeQueryFn Any = new NodeQueryFn() {
    @Override
    public boolean matches(Node input) {
      return true;
    }
  };
}
