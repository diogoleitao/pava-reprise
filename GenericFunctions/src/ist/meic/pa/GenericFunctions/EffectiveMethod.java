package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

public class EffectiveMethod {
	private ArrayList<GFMethod> befores = new ArrayList<>();
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();
	private ArrayList<GFMethod> afters = new ArrayList<>();

	public EffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> sortedMainMethods, ArrayList<GFMethod> afters) {
		this.befores = befores;
		this.mainMethods = sortedMainMethods;
		this.afters = afters;
	}

	public ArrayList<GFMethod> getBefores() {
		return this.befores;
	}

	public ArrayList<GFMethod> getMainMethods() {
		return this.mainMethods;
	}

	public ArrayList<GFMethod> getAfters() {
		return this.afters;
	}
}
