package ciir.jfoley.chai.xml;

import java.util.List;

/**
 * @author jfoley.
 */
public interface XNodes extends List<XNode> {
	public XNodes selectByTag(String tagName);
  public XNode first();
}
