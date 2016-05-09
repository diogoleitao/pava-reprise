package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StandardCombination {
	private ArrayList<Class<?>> classPrecedences = new ArrayList<Class<?>>();
	private ArrayList<Class<?>> interfacesPrecedences = new ArrayList<Class<?>>();

	public EffectiveMethod computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> mainMethods, ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Object arg : callerArgs)
			parameterTypes.add(arg.getClass());

		ArrayList<GFMethod> applicableBefores = removeNonApplicable(befores, parameterTypes);
		ArrayList<GFMethod> applicableMethods = removeNonApplicable(mainMethods, parameterTypes);
		ArrayList<GFMethod> applicableAfters = removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(applicableBefores, parameterTypes);
		ArrayList<GFMethod> sortedMainMethods = sortMostToLeast(applicableMethods, parameterTypes);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(applicableAfters, parameterTypes);

		return new EffectiveMethod(sortedBefores, sortedMainMethods, sortedAfters);
	}

	private void getClassPrecedences(Class<?> clazz) {
		if (clazz.isInterface())
			getInterfacesPrecedences(clazz);
		else if (clazz.equals(Object.class))
			classPrecedences.add(clazz);
		else if (clazz.getComponentType() != null) {
			classPrecedences.add(clazz);
			if (!clazz.getComponentType().equals(Object.class))
				getClassPrecedences(clazz.getComponentType().getSuperclass());
		} else {
			classPrecedences.add(clazz);
			getClassPrecedences(clazz.getSuperclass());
		}
	}

	private void getInterfacesPrecedences(Class<?> clazz) {
		interfacesPrecedences.add(clazz);
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
		ArrayList<GFMethod> applicableMethods = gfImplementations;
		for (int i = 0; i < applicableMethods.size(); i++) {
			GFMethod applicableMethod = applicableMethods.get(i);
			for (Class<?> argType : callerArgTypes) {
				ArrayList<Class<?>> callImplementationArgTypes = getCallMethodParameterTypes(applicableMethod);

				getClassPrecedences(argType);
				classPrecedences.addAll(interfacesPrecedences);

				for (Class<?> c : callImplementationArgTypes) {
					if (!classPrecedences.contains(c)) {
						applicableMethods.remove(applicableMethod);
						if (callerArgTypes.size() == 1)
							i--;
						break;
					}
				}
			}
			classPrecedences.clear();
		}

		return applicableMethods;
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
