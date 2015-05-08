package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.xml.XNode;
import ciir.jfoley.chai.xml.XNodes;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley.
 */
public abstract class AXNodes extends AbstractList<XNode> implements XNodes {
	@Override
	public XNodes selectByTag(String tagName) {
		List<XNode> output = new ArrayList<XNode>();
		for (XNode xNode : this) {
			if(xNode.getTag().equals(tagName)) {
				output.add(xNode);
			}
		}
		// recurse?
		return new XListXNodes(output);
	}

  @Override
  public XNode first() {
    return this.get(0);
  }
}
