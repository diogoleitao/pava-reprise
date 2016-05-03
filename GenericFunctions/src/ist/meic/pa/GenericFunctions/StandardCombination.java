package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(befores);
		ArrayList<GFMethod> sortedMethods = sortMostToLeast(methods);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(afters);

		for (GFMethod before : sortedBefores)
			before.call(callerArgs);

		for (GFMethod method : sortedMethods)
			method.call(callerArgs);

		for (GFMethod after : sortedAfters)
			after.call(callerArgs);

		return effectiveMethod;
	}

	private void removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
		for (int i = 0; i < callerArgTypes.size(); i++) {
			for (GFMethod gfImplementation : gfImplementations) {
				Method call = gfImplementation.getClass().getDeclaredMethods()[0];
				ArrayList<Class<?>> callImplementationArgTypes = new ArrayList<Class<?>>(Arrays.asList(call.getParameterTypes()));

				getClassPrecedences(callerArgTypes.get(i));

				if (!classPrecedences.contains(callImplementationArgTypes.get(i)))
					gfImplementations.remove(gfImplementation);
			}
			classPrecedences = new ArrayList<Class<?>>();
		}
	}

	private ArrayList<GFMethod> sortMostToLeast(ArrayList<GFMethod> methods) {

		return methods;
	}

	private ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods) {

		return methods;
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
}
