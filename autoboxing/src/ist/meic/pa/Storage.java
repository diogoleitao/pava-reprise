package ist.meic.pa;

import java.util.TreeMap;

/**
 * Class containing a TreeMap with the methods needed to store the autoboxing
 * operations. The keys for the TreeMap structure consist of the name of the
 * method that forced the autoboxing operation, the class of the parameter being
 * autoboxed and the type of the autoboxing operation, in that order, separated
 * with the 'SEPARATOR' token.
 */
public class Storage {

	public static final String SEPARATOR = "separador";

	private static TreeMap<String, Integer> autoboxingCounters = new TreeMap<>();

	/**
	 * If the string "method + parameter class + box/unbox operation" used as
	 * the TreeMap key is not present, adds an entry with its counter set to 1.
	 * Otherwise, it just increments the respective counter
	 */
	public static void updateAutoboxingCounter(String methodKey) {
		if (!autoboxingCounters.containsKey(methodKey))
			autoboxingCounters.put(methodKey, new Integer(1));
		else {
			int currentCounter = autoboxingCounters.get(methodKey).intValue();
			autoboxingCounters.put(methodKey, new Integer(++currentCounter));
		}
	}

	/**
	 * Prints the output of the profiler. The output is printed in the same
	 * order as the keys are stored in the TreeMap, which is the expected order
	 * of the output.
	 */
	public static void printOutput() {
		for (String methodName : autoboxingCounters.keySet()) {
			String[] split = methodName.split(SEPARATOR);
			String method = split[0];
			String parameterClass = split[1];
			String operation = split[2];
			System.err.println(method + operation + autoboxingCounters.get(methodName) + " " + parameterClass);
		}
	}

	/**
	 * Methods used to assert the autoboxing compatibility of the types
	 * involved, so as to prevent false positives when the types are not
	 * compatible (autoboxing should only be accounted for between a primitive
	 * type and its wrapper equivalent).
	 */

	private static boolean checkTypeCompatibility(String type, String clazz) {
		return clazz.equalsIgnoreCase(type);
	}

	public static boolean checkTypeCompatibility(int i, Object o) {
		return checkTypeCompatibility("integer", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(double d, Object o) {
		return checkTypeCompatibility("double", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(short s, Object o) {
		return checkTypeCompatibility("short", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(float f, Object o) {
		return checkTypeCompatibility("float", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(long l, Object o) {
		return checkTypeCompatibility("long", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(byte b, Object o) {
		return checkTypeCompatibility("byte", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(char c, Object o) {
		return checkTypeCompatibility("character", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(boolean b, Object o) {
		return checkTypeCompatibility("boolean", o.getClass().getSimpleName());
	}

	public static boolean checkTypeCompatibility(Object o1, Object o2) {
		return true;
	}
}
