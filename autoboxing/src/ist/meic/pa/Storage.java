package ist.meic.pa;

import java.util.TreeMap;

public class Storage {
	
	private static Storage instance = null;

	public final String SEPARATOR = "separador";

	private TreeMap<String, Integer> autoboxingCounters = new TreeMap<String, Integer>();
	
	private Storage() {}
	
	public static Storage getInstance() {
		if (instance == null)
			instance = new Storage();
		return instance;
	}

	public void addAutoboxingMethod(String completeKey) {
		autoboxingCounters.put(completeKey, new Integer(1));
	}

	public void updateAutoboxingCounter(String completeKey) {
		int currentCounter = autoboxingCounters.get(completeKey);
		autoboxingCounters.put(completeKey, new Integer(++currentCounter));
	}

	public void printOutput() {
		System.out.println(autoboxingCounters);
		/*
		for (String methodName : autoboxingCounters.keySet()) {
			String[] split = methodName.split(SEPARATOR);
			String method = split[0];
			String parameterClass = split[1];
			String boxing = split[2];
			System.err.println(method + boxing + autoboxingCounters.get(methodName) + " " + parameterClass);
		}
		*/
	}
}
