package ist.meic.pa.GenericFunctions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * The Class StandardCombination allows to choose the main,
 * before and after methods that are applicable.
 */
public class StandardCombination {
	
	/** The class precedences. */
	private ArrayList<LinkedHashSet<Class<?>>> classPrecedences = new ArrayList<>();
	
	/** The interfaces precedences. */
	private LinkedHashSet<Class<?>> interfacesPrecedences = new LinkedHashSet<>();

	/**
	 * This method computes the effective methods with all the generic functions.
	 * Creates an ArrayList to add the parameter type, and creates a LinkedHashSet
	 * for each argument to compute the precedences.
	 * Removes all the generic functions whose arguments are not applicable.
	 * After removing the non applicable methods, the returned arrays are sorted.
	 *
	 * @param befores: all the before methods
	 * @param mainMethods: all the main methods
	 * @param afters: all the afters
	 * @param callerArgs the caller args
	 * @return the effective method containing all the befores, afters and main methods that are applicable
	 */
	public EffectiveMethod computeEffectiveMethod(ArrayList<GFMethod> befores, ArrayList<GFMethod> mainMethods, ArrayList<GFMethod> afters, ArrayList<Object> callerArgs) {
		ArrayList<Class<?>> parameterTypes = new ArrayList<>();
		for (Object arg : callerArgs) {
			parameterTypes.add(arg.getClass());
			this.classPrecedences.add(new LinkedHashSet<Class<?>>());
		}

		ArrayList<GFMethod> applicableBefores = removeNonApplicable(befores, parameterTypes);
		ArrayList<GFMethod> applicableMethods = removeNonApplicable(mainMethods, parameterTypes);
		ArrayList<GFMethod> applicableAfters = removeNonApplicable(afters, parameterTypes);

		ArrayList<GFMethod> sortedBefores = sortMostToLeast(applicableBefores);
		ArrayList<GFMethod> sortedMainMethods = sortMostToLeast(applicableMethods);
		ArrayList<GFMethod> sortedAfters = sortLeastToMost(applicableAfters);

		return new EffectiveMethod(sortedBefores, sortedMainMethods, sortedAfters);
	}

	/**
	 * This method computes the arguments' class precedences.
	 * Adds the arguments' class and all of the super classes to the Precedences'
	 * data structure.
	 * If the class is abstract, it will add all the interfaces in the data structure.
	 * If the the class of the argument implements interfaces it calls the method 
	 * to compute precedences' interface.
	 *
	 * @param clazz the class of the argument called
	 * @param callerArgIndex the caller arguments index
	 */
	private void computeClassPrecedences(Class<?> clazz, int callerArgIndex) {
		if (clazz.isInterface()) {
			this.interfacesPrecedences.add(clazz);
			computeInterfacesPrecedences(clazz);
		} else if (Modifier.isAbstract(clazz.getModifiers())) {
			this.classPrecedences.get(callerArgIndex).addAll(Arrays.asList(clazz.getInterfaces()));
		}
		
		if (clazz.equals(Object.class)) {
			this.classPrecedences.get(callerArgIndex).add(clazz);
		} else if (clazz.getComponentType() != null) {
			this.classPrecedences.get(callerArgIndex).add(clazz);
			computeClassPrecedences(clazz.getComponentType(), callerArgIndex);
		} else {
			this.classPrecedences.get(callerArgIndex).add(clazz);
			computeClassPrecedences(clazz.getSuperclass(), callerArgIndex);
		} if (clazz.getInterfaces().length > 0) {
			computeInterfacesPrecedences(clazz);
		}
	}

	/**
	 * This method computes the precedences' interfaces once. // TODO
	 *
	 * @param clazz the clazz
	 */
	private void computeInterfacesPrecedences(Class<?> clazz) {
		Class<?>[] implementedInterfaces = clazz.getInterfaces();
		for (int i = 0; i < implementedInterfaces.length; i++)
			this.interfacesPrecedences.add(implementedInterfaces[i]);
	}

	/**
	 * Gets the call method parameter types.
	 *
	 * @param method the method
	 * @return the call method parameter types
	 */
	private ArrayList<Class<?>> getCallMethodParameterTypes(GFMethod method) {
		Method call = method.getClass().getDeclaredMethods()[0];
		return new ArrayList<>(Arrays.asList(call.getParameterTypes()));
	}

	/**
	 * This method computes the class precedences of the caller's argument type, 
	 * and removes all the generic functions that are not assignable, which means,
	 * removes all the generic functions that don't have the arguments in the class 
	 * precedence of the call that was made.
	 *
	 * @param gfImplementations the generic function implementations
	 * @param callerArgTypes the caller arguments types
	 * @return the array list
	 */
	private ArrayList<GFMethod> removeNonApplicable(ArrayList<GFMethod> gfImplementations, ArrayList<Class<?>> callerArgTypes) {
		if (gfImplementations.isEmpty())
			return new ArrayList<>();

		ArrayList<GFMethod> applicableMethods = new ArrayList<>(gfImplementations);

		for (int c = 0; c < callerArgTypes.size(); c++)
			computeClassPrecedences(callerArgTypes.get(c), c);

		for (int i = 0; i < applicableMethods.size(); i++) {
			boolean jumpToNextMethod = false;
			GFMethod applicableMethod = applicableMethods.get(i);
			for (int j = 0; j < callerArgTypes.size(); j++) {
				ArrayList<Class<?>> callImplementationArgTypes = getCallMethodParameterTypes(applicableMethod);

				for (int k = 0; k < callImplementationArgTypes.size(); k++) {
					k = (j > k) ? j : k;

					boolean isApplicable = false;
					for (Class<?> clazz : this.classPrecedences.get(j)) {
						if (callImplementationArgTypes.get(k).isAssignableFrom(clazz))
							isApplicable = true;
					}

					if (!isApplicable && applicableMethods.contains(applicableMethod)) {
						applicableMethods.remove(applicableMethod);
						jumpToNextMethod = true;
						i--;
						break;
					}
					j++;
				}

				if (jumpToNextMethod)
					break;
			}
		}

		return applicableMethods;
	}

	/**
	 * This method orders the applicable methods depending on their specifity,
	 * that is computed in the SortableMethod class.
	 *
	 * @param applicableMethods the applicable methods
	 * @param methods the methods
	 * @return the array list
	 */
	private ArrayList<GFMethod> sort(ArrayList<ArrayList<Class<?>>> applicableMethods, ArrayList<GFMethod> methods) {
		HashMap<ArrayList<Class<?>>, GFMethod> methodsToParameterTypes = new HashMap<>();
		for (GFMethod method : methods) {
			Method call = method.getClass().getDeclaredMethods()[0];
			ArrayList<Class<?>> types = new ArrayList<>(Arrays.asList(call.getParameterTypes()));
			methodsToParameterTypes.put(types, method);
		}

		ArrayList<SortableMethod> sortableMethods = new ArrayList<>();
		for (int i = 0; i < applicableMethods.size(); i++) {
			SortableMethod sortableMethod = new SortableMethod(applicableMethods.get(i), this.classPrecedences);
			sortableMethod.setMethodImplementation(methodsToParameterTypes.get(applicableMethods.get(i)));
			sortableMethods.add(sortableMethod);
		}

		ArrayList<GFMethod> sortedMethods = new ArrayList<>();
		for (int c = 0; c < sortableMethods.size(); c++) {
			for (int i = 0; i + 1 < sortableMethods.size(); i++) {
				if (sortableMethods.get(i).getMethodSpecifity() > sortableMethods.get(i+1).getMethodSpecifity())
					Collections.swap(sortableMethods, i, i + 1);
			}
		}

		for (SortableMethod method : sortableMethods)
			sortedMethods.add(method.getMethodImplementation());

		return sortedMethods;
	}

	/**
	 * This method sorts the methods from most to least specific
	 * to compute the correct order for the before and main methods.
	 *
	 * @param methods the methods
	 * @return the array list
	 */
	private ArrayList<GFMethod> sortMostToLeast(ArrayList<GFMethod> methods) {
		if(methods.isEmpty())
			return new ArrayList<>();
		
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<>();

		for (GFMethod gfMethod : methods)
			toSort.add(getCallMethodParameterTypes(gfMethod));

		return sort(toSort, methods);
	}

	/**
	 * This method sorts the methods from least to more specific
	 * to compute the correct order for the afters methods.
	 *
	 * @param methods the methods
	 * @return the array list
	 */
	private ArrayList<GFMethod> sortLeastToMost(ArrayList<GFMethod> methods) {
		if(methods.isEmpty())
			return new ArrayList<>();
		
		ArrayList<ArrayList<Class<?>>> toSort = new ArrayList<>();

		for (GFMethod gfMethod : methods)
			toSort.add(getCallMethodParameterTypes(gfMethod));

		ArrayList<GFMethod> sortedMethods = sort(toSort, methods);
		Collections.reverse(sortedMethods);

		return sortedMethods;
	}
}
