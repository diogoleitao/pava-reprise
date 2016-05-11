package ist.meic.pa.GenericFunctions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class GenericFunction {
	private String name;
	private static String EXCEPTION_MESSAGE = "No methods for generic function %s with args %s of classes %s."; //$NON-NLS-1$
	private ArrayList<GFMethod> mainMethods = new ArrayList<>();
	private ArrayList<GFMethod> befores = new ArrayList<>();
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
				if (method.getName().equals("call")) { //$NON-NLS-1$
						try {
							method.invoke(before, args);
						} catch (IllegalAccessException | InvocationTargetException e) {}
				}
			}
			break;
		}

		Method mainMethod = effectiveMethod.getMainMethods().get(0).getClass().getDeclaredMethods()[0];
		mainMethod.setAccessible(true);
		if (mainMethod.getName().equals("call")) { //$NON-NLS-1$
			try {
				returnedObject = mainMethod.invoke(effectiveMethod.getMainMethods().get(0), args);
			} catch (IllegalAccessException |InvocationTargetException e) {}
		}

		for (GFMethod after : effectiveMethod.getAfters()) {
			Method[] declaredMethods = after.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				method.setAccessible(true);
				if (method.getName().equals("call")) { //$NON-NLS-1$
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
