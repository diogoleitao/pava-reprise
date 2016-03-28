package ist.meic.pa;

import java.util.TreeMap;

public class Storage {

	public static final String SEPARATOR = "separador";

	private static TreeMap<String, Integer> autoboxingCounters = new TreeMap<String, Integer>();

	public static void updateAutoboxingCounter(String completeKey) {
		if (!autoboxingCounters.containsKey(completeKey))
			autoboxingCounters.put(completeKey, new Integer(1));
		else {
			int currentCounter = autoboxingCounters.get(completeKey);
			autoboxingCounters.put(completeKey, new Integer(++currentCounter));
		}
	}

	public static void printOutput() {
		for (String methodName : autoboxingCounters.keySet()) {
			String[] split = methodName.split(SEPARATOR);
			String method = split[0];
			String parameterClass = split[1];
			String boxing = split[2];
			System.err.println(method + boxing + autoboxingCounters.get(methodName) + " " + parameterClass);
		}

	}

	private static boolean primitiveType(String type, String clazz) {
		return clazz.equalsIgnoreCase(type);
	}

	public static boolean primitiveType(int i, Object o) {
		return primitiveType("integer", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(double d, Object o) {
		return primitiveType("double", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(short s, Object o) {
		return primitiveType("short", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(float f, Object o) {
		return primitiveType("float", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(long l, Object o) {
		return primitiveType("long", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(byte b, Object o) {
		return primitiveType("byte", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(char c, Object o) {
		return primitiveType("character", o.getClass().getSimpleName());
	}

	public static boolean primitiveType(boolean b, Object o) {
		return primitiveType("boolean", o.getClass().getSimpleName());
	}
	
	public static boolean primitiveType(Object o1, Object o2) {
		return true;
	}
}
