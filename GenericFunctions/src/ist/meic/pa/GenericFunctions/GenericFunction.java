package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.Arrays;

import ist.meic.pa.tests.Test3;

public class GenericFunction {
	private String name;
	private ArrayList<GFMethod> mainMethods = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public GenericFunction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addMethod(GFMethod gfm) {
		mainMethods.add(gfm);
	}

	public void addAfterMethod(GFMethod gfm) {
		afters.add(gfm);
	}

	public void addBeforeMethod(GFMethod gfm) {
		befores.add(gfm);
	}

	public Object call(Object... args) {
		Test3.println(args);
		EffectiveMethod effectiveMethod = StandardCombination.computeEffectiveMethod(befores, mainMethods, afters,
				new ArrayList<Object>(Arrays.asList(args)));
		for (GFMethod before : effectiveMethod.getBefores())
			before.call(args);

		Object returnedObject = effectiveMethod.getMainMethod().call(args);

		for (GFMethod after : effectiveMethod.getAfters())
			after.call(args);

		return returnedObject;
	}
}
