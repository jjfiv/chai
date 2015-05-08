package ciir.jfoley.chai.collections.tree;

/**
 * @author jfoley.
 */
public interface ChaiTree<T extends ChaiTree> {
	Iterable<T> getChildren();
	boolean hasChildren();
	T getParent();
}
