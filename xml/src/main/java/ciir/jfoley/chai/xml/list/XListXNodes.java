package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.xml.XNode;

import java.util.List;

/**
 * @author jfoley
 */
public class XListXNodes extends AXNodes {
  List<XNode> inner;

  public XListXNodes(List<XNode> data) {
    assert(data != null);
    this.inner = data;
  }

  @Override
  public XNode get(int index) {
    return inner.get(index);
  }

  @Override
  public int size() {
    return inner.size();
  }
}
