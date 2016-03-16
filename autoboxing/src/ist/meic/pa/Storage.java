package ist.meic.pa;

import java.util.Hashtable;
import java.util.TreeMap;

public class Storage {

	private static TreeMap<String, Integer> boxingCounter = new TreeMap<String, Integer>();
	private static Hashtable<String, String> boxingClasses = new Hashtable<String, String>();

	private static TreeMap<String, Integer> unboxingCounter = new TreeMap<String, Integer>();
	private static Hashtable<String, String> unboxingClasses = new Hashtable<String, String>();
	
	public static void addBoxingMethod(String methodName, String wrapperClass) {
		boxingCounter.put(methodName, new Integer(0));
		boxingClasses.put(methodName, wrapperClass);
	}

	public static void setBoxingCounter(String methodName, int counter) {
		boxingCounter.put(methodName, new Integer(counter));
	}

	public static void addUnboxingMethod(String methodName, String wrapperClass) {
		unboxingCounter.put(methodName, new Integer(0));
		unboxingClasses.put(methodName, wrapperClass);
	}

	public static void setUnboxingCounter(String methodName, int counter) {
		unboxingCounter.put(methodName, new Integer(counter));
	}

	public void printOutput() {
		final String output = "";
		System.err.println(output);
	}
}
