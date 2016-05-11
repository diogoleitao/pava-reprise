package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class GFMethod {
	private ArrayList<Class<?>> argTypes = new ArrayList<Class<?>>();
	private int specificity = 0;
	
	public void computeSpecifity(ArrayList<LinkedHashSet<Class<?>>> classPrecedences) {
		for (int i = 0; i < argTypes.size(); i++) {
			ArrayList<Class<?>> types = new ArrayList<Class<?>>(classPrecedences.get(i));
			specificity += types.indexOf(argTypes.get(i));
		}
	}

	public int getMethodSpecifity() {
		return this.specificity;
	}
}
