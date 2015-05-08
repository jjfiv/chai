package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.xml.XNode;
import org.w3c.dom.NodeList;

/**
 * @author jfoley.
 */
public class NodeListXNodes extends AXNodes {
	NodeList nodelist;
	public NodeListXNodes(NodeList nl) {
		this.nodelist = nl;
	}
	@Override
	public XNode get(int i) {
		return new XNode(nodelist.item(i));
	}

	@Override
	public int size() {
		return nodelist.getLength();
	}
}
