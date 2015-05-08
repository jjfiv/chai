package ciir.jfoley.chai.xml.list;

import ciir.jfoley.chai.collections.list.AChaiList;
import ciir.jfoley.chai.xml.XNode;
import ciir.jfoley.chai.xml.XNodes;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author jfoley.
 */
public abstract class AXNodes extends AChaiList<XNode> implements XNodes {
	@Override
	public XNodes selectByTag(String tagName) {
		ListXNodes output = new ListXNodes();

		for (XNode xNode : this) {
			if(xNode.getTag().equals(tagName)) {
				output.addImplNode(xNode.getImplNode());
			}
		}
		// recurse?
		return output;
	}

  @Override
  public XNode first() {
    return this.get(0);
  }


	public static XNodes ofList(List<Node> domNodes) {
		return new ListXNodes(domNodes);
	}
}
