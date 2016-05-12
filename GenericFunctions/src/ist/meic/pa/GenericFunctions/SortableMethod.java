package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * The Class SortableMethod.
 */
public class SortableMethod {
	
	/** The arg types. */
	private ArrayList<Class<?>> argTypes = new ArrayList<>();
	
	/** The implementation. */
	private GFMethod implementation = null;
	
	/** The specificity. */
	private int specificity = 0;

	/**
	 * Instantiates a new sortable method.
	 *
	 * @param argTypes the arg types
	 * @param classPrecedences the class precedences
	 */
	public SortableMethod(ArrayList<Class<?>> argTypes, ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		this.argTypes = argTypes;
		computeSpecifity(classPrecedences);
	}

	/**
	 * Compute specifity.
	 *
	 * @param classPrecedences the class precedences
	 */
	private void computeSpecifity(ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		for (int i = 0; i < this.argTypes.size(); i++) {
			ArrayList<Class<?>> types = new ArrayList<>(classPrecedences.get(i));
			this.specificity += types.indexOf(this.argTypes.get(i));
		}
	}

	/**
	 * Gets the method implementation.
	 *
	 * @return the method implementation
	 */
	public GFMethod getMethodImplementation() {
		return this.implementation;
	}

	/**
	 * Sets the method implementation.
	 *
	 * @param implementation the new method implementation
	 */
	public void setMethodImplementation(GFMethod implementation) {
		this.implementation = implementation;
	}

	/**
	 * Gets the method specifity.
	 *
	 * @return the method specifity
	 */
	public int getMethodSpecifity() {
		return this.specificity;
	}
}
