package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class GenericFunction {
	private String name;
//	private static String BEFORE_EXCEPTION_MESSAGE = "No before methods for generic function %s with args %s of classes %s.";
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s.";
//	private static String AFTER_EXCEPTION_MESSAGE = "No after methods for generic function %s with args %s of classes %s.";
	private ArrayList<GFMethod> mainMethods = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> befores = new ArrayList<GFMethod>();
	private ArrayList<GFMethod> afters = new ArrayList<GFMethod>();

	public GenericFunction(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addMethod(GFMethod gfm) {
		mainMethods.add(gfm);
	}

	public void addAfterMethod(GFMethod gfm) {
		afters.add(gfm);
	}

	public void addBeforeMethod(GFMethod gfm) {
		befores.add(gfm);
	}

	public Object call(Object... args) throws IllegalArgumentException {
		Object returnedObject = null;
		ArrayList<Class<?>> parameterTypes = new ArrayList<Class<?>>();

		for (Object arg : args)
			parameterTypes.add(arg.getClass());

		EffectiveMethod effectiveMethod = new StandardCombination().computeEffectiveMethod(new ArrayList<GFMethod>(befores), new ArrayList<GFMethod>(mainMethods), new ArrayList<GFMethod>(afters), new ArrayList<Object>(Arrays.asList(args)));
		if (effectiveMethod.getMainMethods().isEmpty())
			throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), Utils.listify(args), Utils.getTypesFromArgs(args)));

		for (GFMethod before : effectiveMethod.getBefores()) {
			Method[] declaredMethods = before.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call")) {
					try {
						method.invoke(before, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), Utils.listify(args), Utils.getTypesFromArgs(args)));
					}
				}
			}
			break;
		}

		Method mainMethod = effectiveMethod.getMainMethods().get(0).getClass().getDeclaredMethods()[0];
		mainMethod.setAccessible(true);
		if (mainMethod.getName().equals("call")) {
			try {
				returnedObject = mainMethod.invoke(effectiveMethod.getMainMethods().get(0), args);
			} catch (IllegalAccessException | IllegalArgumentException |InvocationTargetException e) {
				System.out.println("Puff...");
				//throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), Utils.listify(args), Utils.getTypesFromArgs(args)));
			}
		}

		for (GFMethod after : effectiveMethod.getAfters()) {
			Method[] declaredMethods = after.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call")) {
					try {
						method.invoke(after, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new IllegalArgumentException(String.format(EXCEPTION_MESSAGE, getName(), Utils.listify(args), Utils.getTypesFromArgs(args)));
					}
				}
			}
			break;
		}
		return returnedObject;
	}
}
