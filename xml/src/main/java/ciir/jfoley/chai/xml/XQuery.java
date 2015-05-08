package ciir.jfoley.chai.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

/**
 *
 * @author jfoley
 */
public class XQuery {

  public static String nodeAsString(Node n) {
    if(n == null) throw new RuntimeException("Node is null!");
    if(n.getNodeType() == Node.TEXT_NODE || n.getNodeType() == Node.CDATA_SECTION_NODE) {
      return n.getTextContent();
    } else if(n.getNodeType() == Node.ATTRIBUTE_NODE) {
      return n.getTextContent();
    }
    throw new RuntimeException("Not a terminal node! "+n);
  }

  public static String childNodesAsString(Node n) {
    NodeList children = n.getChildNodes();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < children.getLength(); i++) {
      sb.append(nodeAsString(children.item(i)));
    }
    return sb.toString();
  }

  public static List<Node> findChildrenByTagName(Node root, final String tagName) {
    List<Node> output = new ArrayList<>();
    recursivelyFindChildrenByMatcher(output, root, new NodeQueryFn() {
      @Override
      public boolean matches(Node node) {
        return Objects.equals(node.getNodeName(), tagName);
      }
    });
    return output;
  }

  private static void recursivelyFindChildrenByMatcher(List<Node> output, Node root, NodeQueryFn matcher) {
		if(matcher.matches(root)) {
			output.add(root);
		}
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			recursivelyFindChildrenByMatcher(output, children.item(i), matcher);
		}
	}

  public static Map<String,String> getAttributes(Node xmlNode) {
    Map<String,String> output = new HashMap<>();
    NamedNodeMap attributes = xmlNode.getAttributes();
    for (int i = 0; i < attributes.getLength(); i++) {
      Node item = attributes.item(i);
      assert item.getNodeType() == Node.ATTRIBUTE_NODE : "Attribute map should have attribute nodes!";
      output.put(item.getNodeName(), item.getTextContent());
    }
    return output;
  }
}
