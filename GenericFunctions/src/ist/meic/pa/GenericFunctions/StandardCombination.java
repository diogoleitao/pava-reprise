package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StandardCombination {
	private static ArrayList<Class<?>> classPrecedences = new ArrayList<Class<?>>();

	public static EffectiveMethod computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> mainMethods, ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Object arg : callerArgs)
			parameterTypes.add(arg.getClass());

		removeNonApplicable(befores, parameterTypes);
		removeNonApplicable(mainMethods, parameterTypes);
		removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(befores, parameterTypes);
		ArrayList<GFMethod> sortedMainMethods = sortMostToLeast(mainMethods, parameterTypes);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(afters, parameterTypes);

		if (sortedMainMethods.isEmpty())
			throw new IllegalArgumentException("");
		else
			return new EffectiveMethod(sortedBefores, sortedMainMethods.get(0), sortedAfters);
	}

	private static void getClassPrecedences(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			// TODO: modify this to check the condition properly
			classPrecedences.add(clazz);
		} else if (clazz.getSuperclass().equals(Object.class)) {
			classPrecedences.add(clazz);
			classPrecedences.add(clazz.getSuperclass());
		} else if (clazz.getComponentType() != null) {
			classPrecedences.add(clazz.getComponentType());
			getClassPrecedences(clazz.getComponentType().getSuperclass());
		} else {
			classPrecedences.add(clazz);
			getClassPrecedences(clazz.getSuperclass());
		}
	}

	private static ArrayList<Class<?>> getParametersTypesFromMethod(GFMethod method) {
		Method call = method.getClass().getDeclaredMethods()[0];
		return new ArrayList<Class<?>>(Arrays.asList(call.getParameterTypes()));
	}

	private static void removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
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

	private static ArrayList<GFMethod> sort(ArrayList<ArrayList<Class<?>>> arraysToSort, ArrayList<Class<?>> callerArgTypes, ArrayList<GFMethod> methods) {
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

	private static ArrayList<GFMethod> sortMostToLeast(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>(); 

		for (GFMethod gfMethod : methods)
			toSort.add(getParametersTypesFromMethod(gfMethod));

		return sort(toSort, callerArgTypes, methods);
	}

	private static ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>(); 

		for (GFMethod gfMethod : methods)
			toSort.add(getParametersTypesFromMethod(gfMethod));

		ArrayList<GFMethod> sortedMethods = sort(toSort, callerArgTypes, methods);
		Collections.reverse(sortedMethods);

		return sortedMethods;
	}
}
