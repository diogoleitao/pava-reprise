package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

/**
 * The Class EffectiveMethod.
 */
public class EffectiveMethod {
	
	/** The befores. */
	private ArrayList<GFMethod> befores = new ArrayList<>();
	
	/** The main methods. */
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();
	
	/** The afters. */
	private ArrayList<GFMethod> afters = new ArrayList<>();

	/**
	 * Instantiates a new effective method.
	 *
	 * @param befores the befores
	 * @param sortedMainMethods the sorted main methods
	 * @param afters the afters
	 */
	public EffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> sortedMainMethods, ArrayList<GFMethod> afters) {
		this.befores = befores;
		this.mainMethods = sortedMainMethods;
		this.afters = afters;
	}

	/**
	 * Gets the befores.
	 *
	 * @return the befores
	 */
	public ArrayList<GFMethod> getBefores() {
		return this.befores;
	}

	/**
	 * Gets the main methods.
	 *
	 * @return the main methods
	 */
	public ArrayList<GFMethod> getMainMethods() {
		return this.mainMethods;
	}

	/**
	 * Gets the afters.
	 *
	 * @return the afters
	 */
	public ArrayList<GFMethod> getAfters() {
		return this.afters;
	}
}
