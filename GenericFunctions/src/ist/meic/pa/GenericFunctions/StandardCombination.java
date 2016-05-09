package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StandardCombination {
	private ArrayList<ArrayList<Class<?>>> classPrecedences = new ArrayList<ArrayList<Class<?>>>();
	private ArrayList<Class<?>> interfacesPrecedences = new ArrayList<Class<?>>();

	public EffectiveMethod computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> mainMethods, ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Object arg : callerArgs) {
			parameterTypes.add(arg.getClass());
			classPrecedences.add(new ArrayList<Class<?>>());
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
		/*if (clazz.isInterface())
			getInterfacesPrecedences(clazz);
		else */if (clazz.equals(Object.class))
			classPrecedences.get(callerArgIndex).add(clazz);
		else if (clazz.getComponentType() != null) {
			classPrecedences.get(callerArgIndex).add(clazz);
			getClassPrecedences(clazz.getComponentType(), callerArgIndex);
		} else {
			classPrecedences.get(callerArgIndex).add(clazz);
			getClassPrecedences(clazz.getSuperclass(), callerArgIndex);
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
		ArrayList<GFMethod> applicableMethods = new ArrayList<GFMethod>();
		for (int i = 0; i < gfImplementations.size(); i++) {
			System.out.println("i " + i);
			GFMethod applicableMethod = gfImplementations.get(i);
			int j;
			for (j = 0; j < callerArgTypes.size(); j++) {
				System.out.println("\tj " + j);
				ArrayList<Class<?>> callImplementationArgTypes = getCallMethodParameterTypes(applicableMethod);

				if (classPrecedences.get(j).isEmpty())
					getClassPrecedences(callerArgTypes.get(j), j);

				for (Class<?> c : callImplementationArgTypes) {
					Utils.println("\t" + c);
					Utils.println("\t" + classPrecedences.get(j));
					if (classPrecedences.get(j).contains(c)) {
						applicableMethods.add(applicableMethod);
						if (callerArgTypes.size() == 1)
							i--;
						break;
					}
				}
			}
			classPrecedences.get(--j).clear();
		}

		return applicableMethods;
	}

	private ArrayList<GFMethod> sort(ArrayList<ArrayList<Class<?>>> arraysToSort, ArrayList<Class<?>> callerArgTypes, ArrayList<GFMethod> methods) {
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
