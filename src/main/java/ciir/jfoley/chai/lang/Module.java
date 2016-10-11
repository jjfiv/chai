package ciir.jfoley.chai.lang;

/**
 * This marks a "Module" class that just has functions, and only a private constructor.
 * @author jfoley.
 */
public abstract class Module {
	/** This constructor crashes. Don't make instances of these classes. */
	protected Module() {
		throw new IllegalStateException("Modules should never be instantiated!");
	}
}
