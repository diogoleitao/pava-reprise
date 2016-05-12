package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The GenericFunction class 
 */
public class GenericFunction {
	
	/** The name. */
	private String name;
	
	/** The exception message. */
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s.";
	
	/** The main methods. */
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();
	
	/** The befores. */
	private ArrayList<GFMethod> befores = new ArrayList<>();
	
	/** The afters. */
	private ArrayList<GFMethod> afters = new ArrayList<>();

	/**
	 * Instantiates a new generic function.
	 *
	 * @param name the name
	 */
	public GenericFunction(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Adds the generic functions of main methods.
	 *
	 * @param gfm the gfm
	 */
	public void addMethod(GFMethod gfm) {
		this.mainMethods.add(gfm);
	}

	/**
	 * Adds the generic function of after methods.
	 *
	 * @param gfm the gfm
	 */
	public void addAfterMethod(GFMethod gfm) {
		this.afters.add(gfm);
	}

	/**
	 * Adds the generic function of before methods.
	 *
	 * @param gfm the gfm
	 */
	public void addBeforeMethod(GFMethod gfm) {
		this.befores.add(gfm);
	}

	/**
	 * The Call method will compute the effective methods.
	 * First it will invoke the before methods.
	 * Second it will invoke the first main method, that will be the primary method.
	 * And finally it will invoke the after methods.
	 * All the methods will be invoked using java reflection.
	 * If there aren't any applicable methods to a particular set of arguments it 
	 * will throw an IllegalArgumentException.
	 *
	 * @param args the args
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
