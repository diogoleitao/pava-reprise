package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericFunction {
	private String name;
	private ArrayList<GFMethod> methods = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public GenericFunction(String name) {
		this.name = name;
	}

	public void addMethod(GFMethod gfm) {
		methods.add(gfm);
	}

	public void addAfterMethod(GFMethod gfm) {
		afters.add(gfm);
	}

	public void addBeforeMethod(GFMethod gfm) {
		befores.add(gfm);
	}

	public Object call(Object... args) {
		new StandardCombination().computeEffectiveMethod(this.befores, this.methods, this.afters, new ArrayList<Object>(Arrays.asList(args)));
		return null;
	}
}
