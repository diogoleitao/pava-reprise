package ist.meic.pa.GenericFunctions;

import java.util.ArrayList;

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
		return null;
	}

	class GFMethodOO extends GFMethod {

		@Override
		public Object call() {
			return call(new Object());
		}
	}

	class GFMethodSS extends GFMethod {

		@Override
		public Object call() {
			return call(new String());
		}
	}
}
