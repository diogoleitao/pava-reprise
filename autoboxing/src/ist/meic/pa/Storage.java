package ist.meic.pa;

import java.util.TreeMap;

public class Storage {

	public static final String SEPARATOR = "separador";

	private static TreeMap<String, Integer> autoboxingCounters = new TreeMap<String, Integer>();

	public static void updateAutoboxingCounter(String completeKey, Object type) {
		System.out.println(type);
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
}
