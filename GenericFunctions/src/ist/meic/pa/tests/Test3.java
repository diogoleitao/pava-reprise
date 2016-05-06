package ist.meic.pa.tests;

import java.util.Arrays;

import ist.meic.pa.GenericFunctions.GFMethod;
import ist.meic.pa.GenericFunctions.GenericFunction;

public class Test3 {
	public static void println(Object obj) {
		if (obj instanceof Object[]) {
			System.out.println(Arrays.deepToString((Object[]) obj));
		} else {
			System.out.println(obj);
		}
	}

	public static void main(String[] args) {
		final GenericFunction explain = new GenericFunction("explain");
		explain.addMethod(new GFMethod() {
			Object call(Integer entity) {
				System.out.printf("%s is an integer", entity);
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(Number entity) {
				System.out.printf("%s is a number", entity);
				return "";
			}
		});
		explain.addMethod(new GFMethod() {
			Object call(String entity) {
				System.out.printf("%s is a string", entity);
				return "";
			}
		});
		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.out.printf(" (in hexadecimal, is %x)", entity);
			}
		});
		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.out.printf("The number ", entity);
			}
		});
		println(explain.call(123));
		//println(explain.call("Hi"));
		println(explain.call(3.14159));
	}
}
