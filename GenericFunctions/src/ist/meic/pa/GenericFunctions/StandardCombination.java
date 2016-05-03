package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StandardCombination {
	private ArrayList<Class<?>> classPrecedences = new ArrayList<Class<?>>();

	public Method computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> methods, ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		Method effectiveMethod = null;

		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Object arg : callerArgs)
			parameterTypes.add(arg.getClass());

		removeNonApplicable(befores, parameterTypes);
		removeNonApplicable(methods, parameterTypes);
		removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(befores, parameterTypes);
		ArrayList<GFMethod> sortedMethods = sortMostToLeast(methods, parameterTypes);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(afters, parameterTypes);

		for (GFMethod before : sortedBefores)
			before.call(callerArgs);

		for (GFMethod method : sortedMethods)
			method.call(callerArgs);

		for (GFMethod after : sortedAfters)
			after.call(callerArgs);

		return effectiveMethod;
	}

	private void getClassPrecedences(Class<?> clazz) {
		Class<?> superClass = clazz.getSuperclass();
		if (clazz.isPrimitive()) {
			classPrecedences.add(clazz);
		} else if (superClass.equals(Object.class)) {
			classPrecedences.add(clazz);
			classPrecedences.add(superClass);
		} else {
			classPrecedences.add(clazz);
			getClassPrecedences(superClass);
		}
	}

	private ArrayList<Class<?>> getParametersTypesFromMethod(GFMethod method) {
		Method call = method.getClass().getDeclaredMethods()[0];
		return new ArrayList<Class<?>>(Arrays.asList(call.getParameterTypes()));
	}

	private void removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
		for (int i = 0; i < callerArgTypes.size(); i++) {
			for (GFMethod gfImplementation : gfImplementations) {
				ArrayList<Class<?>> callImplementationArgTypes = getParametersTypesFromMethod(gfImplementation);

				getClassPrecedences(callerArgTypes.get(i));

				if (!classPrecedences.contains(callImplementationArgTypes.get(i)))
					gfImplementations.remove(gfImplementation);
			}
			classPrecedences = new ArrayList<Class<?>>();
		}
	}

	private ArrayList<GFMethod> sort(ArrayList<ArrayList<Class<?>>> arraysToSort, ArrayList<Class<?>> callerArgTypes, ArrayList<GFMethod> methods) {
		for (int i = 0; i + 1 < arraysToSort.size(); i++) {
			ArrayList<Class<?>> firstElement = arraysToSort.get(i);
			ArrayList<Class<?>> secondElement = arraysToSort.get(i + 1);

			for (int j = callerArgTypes.size() - 1; j > -1; j--) {
				getClassPrecedences(callerArgTypes.get(j));
				int firstIndex = classPrecedences.indexOf(firstElement.get(j));
				int secondIndex = classPrecedences.indexOf(secondElement.get(j));

				if (firstIndex > secondIndex)
					Collections.swap(arraysToSort, i, i+1);
			}
		}
		
		ArrayList<GFMethod> sortedMethods = new ArrayList<GFMethod>();
		for (ArrayList<Class<?>> types : arraysToSort) {
			for (GFMethod method : methods) {
				if (types.equals(getParametersTypesFromMethod(method))) {
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
			toSort.add(getParametersTypesFromMethod(gfMethod));

		return sort(toSort, callerArgTypes, methods);
	}

	private ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>(); 

		for (GFMethod gfMethod : methods)
			toSort.add(getParametersTypesFromMethod(gfMethod));

		ArrayList<GFMethod> sortedMethods = sort(toSort, callerArgTypes, methods);
		Collections.reverse(sortedMethods);

		return sortedMethods;
	}
}
