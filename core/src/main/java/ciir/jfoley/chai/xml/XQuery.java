package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.string.StrUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

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

  /** One-deep set of children converted to strings with spaces between them. */
  public static String childNodesAsString(Node n) {
    NodeList children = n.getChildNodes();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < children.getLength(); i++) {
      sb.append(nodeAsString(children.item(i))).append(' ');
    }
    return StrUtil.compactSpaces(sb);
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
