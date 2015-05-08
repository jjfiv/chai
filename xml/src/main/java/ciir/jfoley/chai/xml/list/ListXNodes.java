package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.xml.XNode;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jfoley.
 */
public class ListXNodes extends AXNodes {
	List<Node> nodes;

	public ListXNodes() {
		nodes = new ArrayList<>();
	}
	public ListXNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public XNode get(int i) {
		return new XNode(nodes.get(i));
	}

	@Override
	public int size() {
		return nodes.size();
	}
}
