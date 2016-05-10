package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

public class StandardCombination {
	private ArrayList<LinkedHashSet<Class<?>>> classPrecedences = new ArrayList<LinkedHashSet<Class<?>>>();
	private LinkedHashSet<Class<?>> interfacesPrecedences = new LinkedHashSet<Class<?>>();

	public EffectiveMethod computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> mainMethods,
			ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Object arg : callerArgs) {
			parameterTypes.add(arg.getClass());
			classPrecedences.add(new LinkedHashSet<Class<?>>());
		}

		ArrayList<GFMethod> applicableBefores = removeNonApplicable(befores, parameterTypes);
		ArrayList<GFMethod> applicableMethods = removeNonApplicable(mainMethods, parameterTypes);
		ArrayList<GFMethod> applicableAfters = removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(applicableBefores, parameterTypes);
		ArrayList<GFMethod> sortedMainMethods = sortMostToLeast(applicableMethods, parameterTypes);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(applicableAfters, parameterTypes);

		return new EffectiveMethod(sortedBefores, sortedMainMethods, sortedAfters);
	}

	private void getClassPrecedences(Class<?> clazz, int callerArgIndex) {
	 	if (clazz.isInterface()) {
//			interfacesPrecedences.add(clazz);
	 		getInterfacesPrecedences(clazz); 
//	 	} else if (clazz.getInterfaces().length > 0) {
//	 		classPrecedences.get(callerArgIndex).add(clazz);
//	 		getInterfacesPrecedences(clazz);	 		
	 	} else if (clazz.equals(Object.class)) {
			classPrecedences.get(callerArgIndex).add(clazz);
	 	} else if (clazz.getComponentType() != null) {
			classPrecedences.get(callerArgIndex).add(clazz);
			getClassPrecedences(clazz.getComponentType(), callerArgIndex);
		} else {
			classPrecedences.get(callerArgIndex).add(clazz);
			getClassPrecedences(clazz.getSuperclass(), callerArgIndex);
		}
	 	
	}

	private void getInterfacesPrecedences(Class<?> clazz) {
		Class<?>[] implementedInterfaces = clazz.getInterfaces();
		for (int i = 0; i < implementedInterfaces.length; i++) {
			interfacesPrecedences.add(implementedInterfaces[i]);
			if (implementedInterfaces[i].getInterfaces().length != 0)
				getInterfacesPrecedences(implementedInterfaces[i]);
		}
	}

	private ArrayList<Class<?>> getCallMethodParameterTypes(GFMethod method) {
		Method call = method.getClass().getDeclaredMethods()[0];
		return new ArrayList<Class<?>>(Arrays.asList(call.getParameterTypes()));
	}

	private ArrayList<GFMethod> removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
		if(gfImplementations.isEmpty())
			return new ArrayList<GFMethod>();
		
		ArrayList<GFMethod> applicableMethods = new ArrayList<GFMethod>(gfImplementations);
		
		for (int c = 0; c < callerArgTypes.size(); c++)		
			getClassPrecedences(callerArgTypes.get(c), c);
		
		classPrecedences.add(interfacesPrecedences);
		
		for (int i = 0; i < applicableMethods.size(); i++) {
			boolean jumpToNextMethod = false;
			GFMethod applicableMethod = applicableMethods.get(i);
			int j;
			for (j = 0; j < callerArgTypes.size(); j++) {
				ArrayList<Class<?>> callImplementationArgTypes = getCallMethodParameterTypes(applicableMethod);

				for (int k = 0; k < callImplementationArgTypes.size(); k++) {
					
					k = (j > k) ? j : k;
					
					if (!(classPrecedences.get(j).contains(callImplementationArgTypes.get(k)))
							&& applicableMethods.contains(applicableMethod)) {
						applicableMethods.remove(applicableMethod);
						jumpToNextMethod = true;
						i--;
						break;
					} else { 
						j++;
					}
				}

				if (jumpToNextMethod)
					break;
			}
		}

		return applicableMethods;
	}

	private ArrayList<GFMethod> sort(ArrayList<ArrayList<Class<?>>> arraysToSort, ArrayList<Class<?>> callerArgTypes,
			ArrayList<GFMethod> methods) {
		for (int i = 0; i + 1 < arraysToSort.size(); i++) {
			ArrayList<Class<?>> firstElement = arraysToSort.get(i);
			ArrayList<Class<?>> secondElement = arraysToSort.get(i + 1);

			for (int j = callerArgTypes.size() - 1; j > -1; j--) {
				getClassPrecedences(callerArgTypes.get(j), j);
				int firstIndex = classPrecedences.indexOf(firstElement.get(j));
				int secondIndex = classPrecedences.indexOf(secondElement.get(j));

				if (firstIndex > secondIndex)
					Collections.swap(arraysToSort, i, i + 1);
			}
		}

		ArrayList<GFMethod> sortedMethods = new ArrayList<GFMethod>();
		for (ArrayList<Class<?>> types : arraysToSort) {
			for (GFMethod method : methods) {
				if (types.equals(getCallMethodParameterTypes(method))) {
					sortedMethods.add(method);
					methods.remove(method);
					break;
				}
			}
		}

		return sortedMethods;
	}

	private ArrayList<GFMethod> sortMostToLeast(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>();

		for (GFMethod gfMethod : methods)
			toSort.add(getCallMethodParameterTypes(gfMethod));

		return sort(toSort, callerArgTypes, methods);
	}

	private ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>();

		for (GFMethod gfMethod : methods)
			toSort.add(getCallMethodParameterTypes(gfMethod));

		ArrayList<GFMethod> sortedMethods = sort(toSort, callerArgTypes, methods);
		Collections.reverse(sortedMethods);

		return sortedMethods;
	}
}
