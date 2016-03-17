package ist.meic.pa;

import java.util.TreeMap;

/**
 * The Class Storage.
 */
public class Storage {
	
	private static String separator = "separador";

	/** The boxing counters. */
	private static TreeMap<String, Integer> boxingCounters = new TreeMap<String, Integer>();

	/** The unboxing counters. */
	private static TreeMap<String, Integer> unboxingCounters = new TreeMap<String, Integer>();

	/**
	 * Adds the boxing method.
	 *
	 * @param methodName the method name
	 * @param wrapperClass the wrapper class
	 */
	public static void addBoxingMethod(String methodName, String wrapperClass) {
		String key = methodName + separator + wrapperClass;
		boxingCounters.put(key, new Integer(1));
	}

	/**
	 * Update boxing counter.
	 *
	 * @param methodName the method name
	 */
	public static void updateBoxingCounter(String methodName) {
		int currentCounter = boxingCounters.get(methodName);
		boxingCounters.put(methodName, new Integer(++currentCounter));
	}

	/**
	 * Adds the unboxing method.
	 *
	 * @param methodName the method name
	 * @param wrapperClass the wrapper class
	 */
	public static void addUnboxingMethod(String methodName, String wrapperClass) {
		String key = methodName + separator + wrapperClass;
		unboxingCounters.put(key, new Integer(1));
	}

	/**
	 * Update unboxing counter.
	 *
	 * @param methodName the method name
	 */
	public static void updateUnboxingCounter(String methodName) {
		int currentCounter = unboxingCounters.get(methodName);
		unboxingCounters.put(methodName, new Integer(++currentCounter));
	}

	/**
	 * Prints the output.
	 */
	public static void printOutput() {
		for (String methodName : boxingCounters.keySet()) {
			String[] split = methodName.split(separator);
			String method = split[0];
			String parameterClass = split[1];
			System.err.println(method + " boxed " + boxingCounters.get(methodName) + " " + parameterClass);
		}
		for (String methodName : unboxingCounters.keySet()) {
			String[] split = methodName.split(separator);
			String method = split[0];
			String parameterClass = split[1];
			System.err.println(method + " unboxed " + unboxingCounters.get(methodName) + " " + parameterClass);
		}
	}
}
