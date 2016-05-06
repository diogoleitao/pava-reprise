package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

public class EffectiveMethod {
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> mainMethods = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public EffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> sortedMainMethods, ArrayList<GFMethod> afters) {
		this.befores = befores;
		this.mainMethods = sortedMainMethods;
		this.afters = afters;
	}

	public ArrayList<GFMethod> getBefores() {
		return befores;
	}

	public ArrayList<GFMethod> getMainMethods() {
		return mainMethods;
	}

	public ArrayList<GFMethod> getAfters() {
		return afters;
	}
}
