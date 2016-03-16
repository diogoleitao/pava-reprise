package ist.meic.pa;

import java.util.TreeMap;

public class Storage {

	private TreeMap<String, Integer> boxingCounter = new TreeMap<String, Integer>();

	public void addBoxingMethod(String methodName) {
		boxingCounter.put(methodName, new Integer(0));
	}
	
	public void setBoxingCounter(String methodName, int counter) {
		boxingCounter.put(methodName, new Integer(counter));
	}

	private TreeMap<String, Integer> unboxingCounter = new TreeMap<String, Integer>();

	public void addUnboxingMethod(String methodName) {
		unboxingCounter.put(methodName, new Integer(0));
	}
	
	public void setUnboxingCounter(String methodName, int counter) {
		unboxingCounter.put(methodName, new Integer(counter));
	}

	public void printOutput() {
		//String print = ctMethod.getLongName() + operation + ctClass.getName(); 
		//System.err.println(print);
	}
}
