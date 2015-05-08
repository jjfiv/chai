package ciir.jfoley.chai.xml;

import ciir.jfoley.chai.collections.chained.ChaiIterable;

import java.util.List;

/**
 * @author jfoley.
 */
public interface XNodes extends List<XNode> {
	XNodes selectByTag(String tagName);
  XNode first();
  ChaiIterable<XNode> chai();
}
