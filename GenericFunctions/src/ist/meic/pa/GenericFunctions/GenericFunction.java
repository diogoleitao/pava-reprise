package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.Arrays;

import ist.meic.pa.tests.Test3;

public class GenericFunction {
	private String name;
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s.";
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
		EffectiveMethod effectiveMethod = StandardCombination.computeEffectiveMethod(befores, mainMethods, afters, new ArrayList<Object>(Arrays.asList(args)));
		
		for (Object o : args)
			System.out.println(o.getClass());

		for (GFMethod before : effectiveMethod.getBefores())
			before.call(args);

		if (effectiveMethod.getMainMethods().isEmpty())
			throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), listify(args), listify(args)));

		Object returnedObject = effectiveMethod.getMainMethods().get(0).call(args);

		for (GFMethod after : effectiveMethod.getAfters())
			after.call(args);

		return returnedObject;
	}

	private Object listify(Object obj) {
		if (obj instanceof Object[]) {
			return Arrays.deepToString((Object[]) obj);
		} else {
			return obj;
		}
	}
}
