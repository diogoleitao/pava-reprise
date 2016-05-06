package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

public class EffectiveMethod {
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private GFMethod mainMethod = null;
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public EffectiveMethod(ArrayList<GFMethod> befores, GFMethod sortedMainMethod, ArrayList<GFMethod> afters) {
		this.befores = befores;
		this.mainMethod = sortedMainMethod;
		this.afters = afters;
	}

	public ArrayList<GFMethod> getBefores() {
		return befores;
	}

	public GFMethod getMainMethod() {
		return mainMethod;
	}

	public ArrayList<GFMethod> getAfters() {
		return afters;
	}
}
