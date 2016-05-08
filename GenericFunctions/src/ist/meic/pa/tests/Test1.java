package ist.meic.pa.tests;

import ist.meic.pa.GenericFunctions.ClassUtils;
import ist.meic.pa.GenericFunctions.GFMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;

public class Test1 {
	public static void main(String[] args) {
		final GenericFunction add = new GenericFunction("add");
		add.addMethod(new GFMethod() {
			Object call(Integer a, Integer b) {
				return a + b;
			}
		});

		add.addMethod(new GFMethod() {
			Object call(Object[] a, Object[] b) {
				Object[] r = new Object[a.length];
				for (int i = 0; i < a.length; i++) {
					r[i] = add.call(a[i], b[i]);
				}
				return r;
			}
		});

		ClassUtils.println(add.call(1, 3));
		ClassUtils.println(add.call(new Object[] { 1, 2, 3 }, new Object[] { 4, 5, 6 }));
		ClassUtils.println(add.call(new Object[] { new Object[] { 1, 2 }, 3 }, new Object[] { new Object[] { 3, 4 }, 5 }));
		ClassUtils.println(add.call(new Object[] { 1, 2 }, 3));
	}
}
