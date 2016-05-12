package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

/**
 * The Class EffectiveMethod is used to invoke the effective methods, and all
 * the before and after that are applicable to the call. Before and after
 * methods are invoked before and after the primary method, respectively.
 */
public class EffectiveMethod {

	/** The before methods. */
	private ArrayList<GFMethod> befores = new ArrayList<>();

	/** The main methods. */
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();

	/** The after methods. */
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
