package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class SortableMethod {
	private ArrayList<Class<?>> argTypes = new ArrayList<>();
	private GFMethod implementation = null;
	private int specificity = 0;

	public SortableMethod(ArrayList<Class<?>> argTypes, ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		this.argTypes = argTypes;
		computeSpecifity(classPrecedences);
	}

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
