package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * The SortableMethod class is used to sort generic function implementations.
 */
public class SortableMethod {

	/** The argument types. */
	private ArrayList<Class<?>> argTypes = new ArrayList<>();

	/** The method's implementation. */
	private GFMethod implementation = null;

	/**
	 * The specificity translates the "distance" of a GFMethod's arguments to a
	 * given function call: the lower the value (closer to 0), the more specific
	 * the GFMethod is.
	 */
	private int specificity;

	public SortableMethod(ArrayList<Class<?>> argTypes, ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		this.argTypes = argTypes;
		computeSpecifity(classPrecedences);
	}

	/**
	 * Computes the specifity of the generic function's arguments types using the
	 * class's index of precedence of the method that's being invoked.
	 *
	 * @param classPrecedences:
	 *            the class precedences
	 */
	private void computeSpecifity(ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		for (int i = 0; i < this.argTypes.size(); i++) {
			ArrayList<Class<?>> types = new ArrayList<>(classPrecedences.get(i));
			this.specificity += types.indexOf(this.argTypes.get(i));
		}
	}

	public GFMethod getMethodImplementation() {
		return this.implementation;
	}

	public void setMethodImplementation(GFMethod implementation) {
		this.implementation = implementation;
	}

	public int getMethodSpecifity() {
		return this.specificity;
	}
}
