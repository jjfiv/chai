package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.collections.tree.ChaiTree;
import ciir.jfoley.chai.collections.tree.TreeFns;
import ciir.jfoley.chai.fn.PredicateFn;
import ciir.jfoley.chai.xml.list.NodeListWrapper;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author jfoley.
 */
public class XNode implements ChaiTree<XNode> {
	private final Node node;

	public XNode(Node node) {
		this.node = node;
	}

	public Map<String,String> attrs() {
		return XQuery.getAttributes(node);
	}

  public String attr(String type) {
    Node namedItem = node.getAttributes().getNamedItem(type);
    if(namedItem == null) return null;
    assert(namedItem.getNodeType() == Node.ATTRIBUTE_NODE);
    return namedItem.getTextContent();
  }

	public boolean hasChildren() {
		return node.hasChildNodes();
	}

  @Override
  public List<XNode> getChildren() {
		return new NodeListWrapper(node.getChildNodes());
	}

	public String getTag() {
		return node.getNodeName();
	}

	public String getText() {
    if(isTextNode()) {
      return node.getTextContent();
    } else {
      return XQuery.childNodesAsString(node);
    }
	}

  public List<XNode> selectByTag(final String tagName) {
    return TreeFns.findChildren(this, new PredicateFn<XNode>() {
      @Override
      public boolean test(XNode input) {
        return Objects.equals(input.getTag(), tagName);
      }
    });
  }

  public boolean isTextNode() {
    return node.getNodeType() == Node.TEXT_NODE ||
           node.getNodeType() == Node.CDATA_SECTION_NODE;
  }

  public List<XNode> findTextChildren() {
    List<XNode> collection = new ArrayList<>();
    for (XNode xNode : getChildren()) {
      if(xNode.isTextNode()) {
        collection.add(xNode);
      } else {
        collection.addAll(xNode.findTextChildren());
      }
    }
    return collection;
  }

  @Override
  public String toString() {
    return "ciir.jfoley.chai.xml.XNode: "+this.getTag();
  }

  @Override
  public XNode getParent() {
    Node parentNode = this.node.getParentNode();
    if(parentNode == null) {
      return null;
    }
    return new XNode(parentNode);
  }

  /**
   * This code prints the XML of the subtree starting at the given node.
   *
   * This code is heavily based upon the <a href="http://stackoverflow.com/questions/2325388">answer</a> by Bozho &amp; edited by Krige. (accessed: May 8, 2015)
   */
  public String xmlString() {
    Transformer tf = null;
    try {
      tf = TransformerFactory.newInstance().newTransformer();
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException("XML Transformers not configured?", e);
    }
    tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    tf.setOutputProperty(OutputKeys.METHOD, "xml");
    tf.setOutputProperty(OutputKeys.INDENT, "yes");
    tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      tf.transform(new DOMSource(this.node),
        new StreamResult(baos));
      return new String(baos.toByteArray(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Can't live without UTF-8");
    } catch (TransformerException e) {
      throw new RuntimeException("XML Serialization issue", e);
    }
  }

  public Node getImplNode() {
    return this.node;
  }

  public static XNode wrap(Node xmlDoc) {
    return new XNode(xmlDoc);
  }

}

