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

		ArrayList<GFMethod> applicableBefores = removeNonApplicable(befores, parameterTypes);
		ArrayList<GFMethod> applicableMethods = removeNonApplicable(mainMethods, parameterTypes);
		ArrayList<GFMethod> applicableAfters = removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(applicableBefores, parameterTypes);
		ArrayList<GFMethod> sortedMainMethods = sortMostToLeast(applicableMethods, parameterTypes);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(applicableAfters, parameterTypes);

		return new EffectiveMethod(sortedBefores, sortedMainMethods.get(0), sortedAfters);
	}

	private static void getClassPrecedences(Class<?> clazz) {
		if (clazz.equals(Object.class)) {
			classPrecedences.add(clazz);
		} else if (clazz.getComponentType() != null) {
			classPrecedences.add(clazz.getComponentType());
			getClassPrecedences(clazz.getComponentType().getSuperclass());
		} else {
			classPrecedences.add(clazz);
			getClassPrecedences(clazz.getSuperclass());
		}
	}

	private static ArrayList<Class<?>> getMethodParameterTypes(GFMethod method) {
		Method call = method.getClass().getDeclaredMethods()[0];
		return new ArrayList<Class<?>>(Arrays.asList(call.getParameterTypes()));
	}

	private static ArrayList<GFMethod> removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<GFMethod> applicableMethods = gfImplementations;

		for (int i = 0; i < applicableMethods.size(); i++) {
			for (Class<?> argType : callerArgTypes) {
				GFMethod applicableMethod = applicableMethods.get(i);
				ArrayList<Class<?>> callImplementationArgTypes = getMethodParameterTypes(applicableMethod);

				getClassPrecedences(argType);

				for (Class<?> c : callImplementationArgTypes) {
					if (!classPrecedences.contains(c))
						applicableMethods.remove(applicableMethod);
					break;
				}

				/*
				 * if (classPrecedences.contains(callImplementationArgTypes)) {
				 * applicableMethods.remove(applicableMethod); Test3.println(
				 * "qwerqwer " + applicableMethods); }
				 */

			}
			classPrecedences.clear();
		}

		return applicableMethods;
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
					Collections.swap(arraysToSort, i, i + 1);
			}
		}

		ArrayList<GFMethod> sortedMethods = new ArrayList<GFMethod>();
		for (ArrayList<Class<?>> types : arraysToSort) {
			for (GFMethod method : methods) {
				if (types.equals(getMethodParameterTypes(method))) {
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
			toSort.add(getMethodParameterTypes(gfMethod));

		return sort(toSort, callerArgTypes, methods);
	}

	private static ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods, ArrayList<Class<?>> callerArgTypes) {
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<ArrayList<Class<?>>>();

		for (GFMethod gfMethod : methods)
			toSort.add(getMethodParameterTypes(gfMethod));

		ArrayList<GFMethod> sortedMethods = sort(toSort, callerArgTypes, methods);
		Collections.reverse(sortedMethods);

		return sortedMethods;
	}
}
