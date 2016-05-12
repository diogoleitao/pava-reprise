package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The GenericFunction class 
 */
public class GenericFunction {

	/** The function's name. */
	private String name;

	/** The exception message. */
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s.";

	/** The main methods. */
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();

	/** The before methods */
	private ArrayList<GFMethod> befores = new ArrayList<>();

	/** The after methods. */
	private ArrayList<GFMethod> afters = new ArrayList<>();

	public GenericFunction(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addMethod(GFMethod gfm) {
		this.mainMethods.add(gfm);
	}

	public void addAfterMethod(GFMethod gfm) {
		this.afters.add(gfm);
	}

	public void addBeforeMethod(GFMethod gfm) {
		this.befores.add(gfm);
	}

	/**
	 * The call method will trigger the computation of the effective method.
	 * Once it's done, it will invoke the before methods, then the primary
	 * method, and finally it will invoke the after methods. All the methods are
	 * invoked via Java reflection. If there aren't any applicable primary
	 * methods to a particular set of arguments, an IllegalArgumentException is
	 * thrown.
	 * 
	 * The checked exceptions that Java reflection forces us to deal with are
	 * not handled (given the absence of code in the catch block), since the
	 * IllegalAccessException is prevented by making the accessed method public
	 * and the InvocationTargetException is also avoided, since the receiver
	 * object has always the same type (same class) and the arguments are
	 * checked by the StandardCombination class.
	 *
	 * @param args:
	 *            the call arguments
	 * @return the object
	 */
	public Object call(Object... args) {
		Object returnedObject = null;
		ArrayList<Class<?>> parameterTypes = new ArrayList<>();

		for (Object arg : args)
			parameterTypes.add(arg.getClass());

		EffectiveMethod effectiveMethod = new StandardCombination().computeEffectiveMethod(new ArrayList<>(this.befores), new ArrayList<>(this.mainMethods), new ArrayList<>(this.afters), new ArrayList<>(Arrays.asList(args)));
		if (effectiveMethod.getMainMethods().isEmpty())
			throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), Utils.listify(args), Utils.getTypesFromArgs(args)));

		for (GFMethod before : effectiveMethod.getBefores()) {
			Method[] declaredMethods = before.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call")) {
					try {
						method.invoke(before, args);
					} catch (IllegalAccessException | InvocationTargetException e) {}
				}
			}
			break;
		}

		Method mainMethod = effectiveMethod.getMainMethods().get(0).getClass().getDeclaredMethods()[0];
		mainMethod.setAccessible(true);
		if (mainMethod.getName().equals("call")) {
			try {
				returnedObject = mainMethod.invoke(effectiveMethod.getMainMethods().get(0), args);
			} catch (IllegalAccessException |InvocationTargetException e) {}
		}

		for (GFMethod after : effectiveMethod.getAfters()) {
			Method[] declaredMethods = after.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call")) {
					try {
						method.invoke(after, args);
					} catch (IllegalAccessException | InvocationTargetException e) {}
				}
			}
			break;
		}
		return returnedObject;
	}
}
