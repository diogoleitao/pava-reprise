package ist.meic.pa.tests;

import java.util.*;
import ist.meic.pa.GenericFunctions.*;

public class TestB {

	public static void main(String args[]) {

		final GenericFunction explain = new GenericFunction("explain");

		explain.addMethod(new GFMethod() {
			Object call(Integer entity) {
				System.err.printf("%s is a integer", entity);
				return "";
			}});

		explain.addMethod(new GFMethod() {
			Object call(Number entity) {
				System.err.printf("%s is a number", entity);
				return "";
			}});

		explain.addMethod(new GFMethod() {
			Object call(String entity) {
				System.err.printf("%s is a string", entity);
				return "";
			}});

		explain.addAfterMethod(new GFMethod() {
			void call(Integer entity) {
				System.err.printf(" (in hexadecimal, is %x)", entity);
			}});

		explain.addBeforeMethod(new GFMethod() {
			void call(Number entity) {
				System.err.printf("The number ", entity);
			}});

		println(explain.call(123));
		println(explain.call("Hi"));
		println(explain.call(3.14159));
	}

	public static void println(Object obj) {

		if (obj instanceof Object[]) {
			System.err.println(Arrays.deepToString((Object[])obj));
		} else {
			System.err.println(obj);
		}
	}
}
