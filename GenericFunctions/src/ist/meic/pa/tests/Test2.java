package ist.meic.pa.tests;

import java.util.Arrays;
import java.util.List;

import ist.meic.pa.GenericFunctions.ClassUtils;
import ist.meic.pa.GenericFunctions.GFMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;

public class Test2 {
	public static void main(String[] args) {
		final GenericFunction add = new GenericFunction("add");
		add.addMethod(new GFMethod() {
			Object call(Object[] a, Object b) {
				Object[] ba = new Object[a.length];
				Arrays.fill(ba, b);
				return add.call(a, ba);
			}
		});

		add.addMethod(new GFMethod() {
			Object call(Object a, Object b[]) {
				Object[] aa = new Object[b.length];
				Arrays.fill(aa, a);
				return add.call(aa, b);
			}
		});

		add.addMethod(new GFMethod() {
			Object call(String a, Object b) {
				return add.call(Integer.decode(a), b);
			}
		});

		add.addMethod(new GFMethod() {
			Object call(Object a, String b) {
				return add.call(a, Integer.decode(b));
			}
		});

		add.addMethod(new GFMethod() {
			Object call(Object[] a, List b) {
				return add.call(a, b.toArray());
			}
		});

		ClassUtils.println(add.call(new Object[] { 1, 2 }, 3));
		ClassUtils.println(add.call(1, new Object[][] { { 1, 2 }, { 3, 4 } }));
		ClassUtils.println(add.call("12", "34"));
		ClassUtils.println(add.call(new Object[] { "123", "4" }, 5));
		ClassUtils.println(add.call(new Object[] { 1, 2, 3 }, Arrays.asList(4, 5, 6)));
	}
}
